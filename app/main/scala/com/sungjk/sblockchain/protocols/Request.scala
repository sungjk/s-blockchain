package com.sungjk.sblockchain.protocols

import com.sungjk.sblockchain.common.Errors.{InvalidAction, InvalidRequest}
import org.json4s.JsonAST.JObject

sealed trait Request

object Request {
	def fromGet(action: String, query: Option[JObject]): Either[InvalidRequest, Request] = {
		action match {
			case _ =>
				Left(InvalidAction)
		}
	}

	def fromPost(action: String, json: JObject): Either[InvalidRequest, Request] = {
		action match {
			case _ =>
				Left(InvalidAction)
		}
	}

}
