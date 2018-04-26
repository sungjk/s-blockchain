package com.sungjk.sblockchain.netty

import com.sungjk.sblockchain.protocols.WebsocketRequestProcessor
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

import scala.concurrent.ExecutionContext

class WebsocketHandler(val clientExecutors: WebsocketRequestProcessor)(implicit ec: ExecutionContext) extends ChannelInboundHandlerAdapter {

	override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {

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
}
