package com.sungjk.sblockchain.netty

import java.io.IOException

import com.sungjk.sblockchain.common.Errors
import com.sungjk.sblockchain.protocols._
import io.netty.channel.{ChannelFuture, ChannelFutureListener, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.handler.codec.TooLongFrameException
import io.netty.handler.codec.http._
import org.json4s.JsonDSL._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class HttpRequestHandler(val clientExecutors: HttpRequestProcessor)(implicit ec: ExecutionContext) extends ChannelInboundHandlerAdapter {

	private def modelConversion: NettyModelConversion = new NettyModelConversion

	override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {
		msg match {
			case req: HttpRequest =>
				val responseFuture: Future[HttpResponse] = handleAction(req) flatMap {
					case response: Response =>
						Future.successful(modelConversion.convertResult(response))
					case _ =>
						Future.failed(Errors.InternalServerError)
				}

				responseFuture onComplete {
					case Success(response) =>
						ctx.channel().writeAndFlush(response)
					case Failure(error) =>
						ctx.channel().writeAndFlush(errorHandler(error))
				}
			case _ =>
				sendSimpleErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST)
		}
	}

	override def channelActive(ctx: ChannelHandlerContext): Unit = {
		super.channelActive(ctx)
	}

	override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
		ctx.flush()
	}

	override def channelInactive(ctx: ChannelHandlerContext): Unit = {
		super.channelInactive(ctx)
	}

	override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
		println(cause.toString)
		// catch your error
		cause match {
			case _: IOException =>
				ctx.channel().close()
			case _: TooLongFrameException =>
				sendSimpleErrorResponse(ctx, HttpResponseStatus.REQUEST_URI_TOO_LONG)
			case e: IllegalArgumentException if Option(e.getMessage).exists(_.contains("Header value contains a prohibited character")) =>
				sendSimpleErrorResponse(ctx, HttpResponseStatus.BAD_REQUEST)
			case _ =>
				ctx.channel().close()
		}
	}

	private def handleAction(request: HttpRequest): Future[Response] = {
		val requestTarget = modelConversion.createRequestTarget(request)
		val (
			method,
			path,
			queryParams,
			body
			) = (requestTarget.method, requestTarget.path, requestTarget.queryParams, requestTarget.body)

		val requestOptTry = method match {
			case HttpMethod.GET =>
				val params = modelConversion.paramsToMap(queryParams)
				Try(Request.fromGet(path, Some(params)))
			case _ =>
				method match {
					case HttpMethod.POST =>
						Try(Request.fromPost(path, body))
					case _ =>
						Failure(Errors.MethodNotAllowed)
				}
		}

		requestOptTry match {
			case Success(requestOpt) =>
				requestOpt match {
					case Right(request: HttpRequest) =>
						clientExecutors.process(request)
					case Left(error: Errors.Error) =>
						Future.successful(ErrorResponse(error))
				}
			case Failure(error) =>
				Future.successful(ErrorResponse(Errors.from(error)))
		}
	}

	private def errorHandler(error: Throwable): HttpResponse = {
		println(error)
		modelConversion.convertResult(ErrorResponse(Errors.from(error)))
	}

	private def sendSimpleErrorResponse(ctx: ChannelHandlerContext, status: HttpResponseStatus): ChannelFuture = {
		val response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status)
		response.headers().set(HttpHeaderNames.CONNECTION, "close")
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, "0")
		val f = ctx.channel().writeAndFlush(response)
		f.addListener(ChannelFutureListener.CLOSE)
		f
	}
}
