package com.sungjk.sblockchain.netty

import java.net.URI

import com.sungjk.sblockchain.protocols.Response
import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.handler.codec.http._
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import io.netty.util.CharsetUtil
import org.json4s.{JNothing, JObject}
import org.json4s.native.JsonMethods.{compact, parse, render}

case class HttpRequestTarget(
	method: HttpMethod,
	path: String,
	queryParams: String,
	body: JObject
)

class NettyModelConversion {

	type QueryString = Map[String, Seq[String]]

	def paramsToMap(query: String): QueryString = {
		val decoded = java.net.URLDecoder.decode(query, "UTF-8")
		Option(decoded).fold(Map.empty[String, Seq[String]]) {
			_.split("&").map {
				_.span(_ != '=') match {
					case (key, v) => key -> v.drop(1)
				}
			}.groupBy(_._1).mapValues(_.toSeq.map(_._2))
		}
	}

	def createRequestTarget(request: HttpRequest): HttpRequestTarget = {
		val (unsafePath, parsedQueryString) = parsePathAndQuery(request.uri)
		val method: HttpMethod = request.method()
		val parsedPath: String = new URI(unsafePath).getRawPath
		val queryString: String = parsedQueryString.stripPrefix("?")
		val httpContent: HttpContent = request.asInstanceOf[HttpContent]
		val content = parse(httpContent.content.toString(CharsetUtil.UTF_8))
		val body: JObject = content match {
			case JNothing => JObject()
			case _ => content.asInstanceOf[JObject]
		}
		if (httpContent.refCnt() > 0) {
			httpContent.release()
		}

		HttpRequestTarget(method, parsedPath, queryString, body)
	}

	def convertRequestBody(request: HttpRequest): Option[HttpContent] = {
		request match {
			case full: FullHttpRequest =>
				Some(full.asInstanceOf[HttpContent])
			case _ =>
				None
		}
	}

	def convertResult(response: Response): FullHttpResponse = {
		val data = compact(render(response.toJson()))
		val httpResponse: FullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, stringToByteBuf(data))
		httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8")
		httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes())
		httpResponse.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED)
		httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
		httpResponse.headers().add("Access-Control-Allow-Origin", "*")
		httpResponse.headers().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE")
		httpResponse.headers().add("Access-Control-Allow-Headers", "*")
		httpResponse
	}

	private def stringToByteBuf(str: String): ByteBuf = {
		if (str.isEmpty) {
			Unpooled.EMPTY_BUFFER
		} else {
			Unpooled.wrappedBuffer(str.getBytes(CharsetUtil.UTF_8))
		}
	}

	private def parsePathAndQuery(uri: String): (String, String) = {
		val withoutHost = uri.dropWhile(_ != '/')
		val queryEndPos = Some(withoutHost.indexOf('#')).filter(_ != -1).getOrElse(withoutHost.length)
		val pathEndPos = Some(withoutHost.indexOf('?')).filter(_ != -1).getOrElse(queryEndPos)
		val path = withoutHost.substring(0, pathEndPos)
		val queryString = withoutHost.substring(pathEndPos, queryEndPos)
		(path, queryString)
	}
}
