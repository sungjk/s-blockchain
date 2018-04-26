package com.sungjk.sblockchain.protocols

import com.sungjk.sblockchain.common.Errors
import org.json4s.JsonAST.JObject
import org.json4s.JsonDSL._

trait Response {
	def toJson(): JObject
	def baseJson(resultCode: Int): JObject = "result" -> resultCode
}

case object NoResponse extends Response {
	def toJson(): JObject = JObject()
}

case class ErrorResponse(error: Errors.Error, detail: Option[String] = None) extends Response {
	def toJson(): JObject =
		baseJson(error.code) ~
		("error" -> error.toString) ~
		("detail" -> detail)
}

case object SuccessResponse extends Response {
	def toJson(): JObject = baseJson(200)
}
