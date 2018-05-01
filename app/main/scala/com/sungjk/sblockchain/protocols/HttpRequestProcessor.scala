package com.sungjk.sblockchain.protocols

import com.sungjk.sblockchain.common.Timestamp

import scala.concurrent.{ExecutionContext, Future}

class HttpRequestProcessor()(implicit val ec: ExecutionContext) {

	def currentTimestamp: Timestamp = Timestamp.current

	def process(request: Request): Future[Response] = {
		request match {
			case _ =>
				Future.successful(SuccessResponse)
		}
	}
}
