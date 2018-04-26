package com.sungjk.sblockchain

import java.util.concurrent.Executors

import com.sungjk.sblockchain.netty.HttpRequestHandler
import com.sungjk.sblockchain.protocols.HttpRequestProcessor
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.handler.codec.http.{HttpObjectAggregator, HttpServerCodec}

import scala.concurrent.ExecutionContext

trait Configure {
	val clientPortNumber: Int
}

class MainRequires(
	val configure: Configure,
	val ecNetty: ExecutionContext,
	val ecClient: ExecutionContext
)

class Main(val requires: MainRequires) {
	val clientHttpRequestProcessor = new HttpRequestProcessor()(requires.ecClient)

	def start(): Unit = {
		val (clientBossGroup, clientWorkerGroup) = (new NioEventLoopGroup(), new NioEventLoopGroup())
		try {
			val clientBootstrap = new ServerBootstrap()
			clientBootstrap.group(clientBossGroup, clientWorkerGroup)
				.channel(classOf[NioServerSocketChannel])
				.childHandler(new ChannelInitializer[SocketChannel]() {
					override def initChannel(ch: SocketChannel): Unit = {
						val pipeline = ch.pipeline()
						pipeline.addLast(new HttpServerCodec())
						pipeline.addLast(new HttpObjectAggregator(65536))
						pipeline.addLast(new HttpRequestHandler(clientHttpRequestProcessor)(requires.ecNetty))
					}
				})
				.option[java.lang.Integer](ChannelOption.SO_BACKLOG, 128)
				.childOption[java.lang.Boolean](ChannelOption.SO_KEEPALIVE, true)

			val clientFuture = clientBootstrap.bind(requires.configure.clientPortNumber).sync()
			println(s"(Client) Listening to port ${requires.configure.clientPortNumber}...")
			clientFuture.channel().closeFuture().sync()
		} finally {
			clientBossGroup.shutdownGracefully()
			clientWorkerGroup.shutdownGracefully()
		}
	}
}

object Main {
	def main(args: Array[String]): Unit = {
		val configure = new Configure {
			val clientPortNumber = 8443
		}

		val nettyExecutors = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))
		val clientExecutors = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(30))

		val requires = new MainRequires(
			configure = configure,
			ecNetty = nettyExecutors,
			ecClient = clientExecutors
		)

		new Main(requires).start()
	}
}
