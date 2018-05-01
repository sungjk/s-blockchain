package com.sungjk.sblockchain.protocols

import com.sungjk.sblockchain.common.Errors.{InvalidAction, InvalidRequest}
import com.sungjk.sblockchain.core.BlockMessage
import org.json4s.JsonAST.JObject

sealed trait Request

object BlockChainRequest extends Request {
	case class NewBlock(nonce: Long)
	case class MineBlock()
	case class GetChains()
	case class GetNodes()
	case class GetBlocks()
	case class AddMessages(messages: Seq[BlockMessage])
	case class RegisterNode()
}

object Request {
	def fromGet(action: String, query: Option[JObject]): Either[InvalidRequest, Request] = {
		action match {
			case "/block" => ??? // NewBlock
			case "/mine" => ??? // MineBlock
			case "/chain" => ??? // GetChains
			case "/nodes" => ??? // GetNodes
			case "/blocks" => ??? // GetBlocks
			case _ =>
				Left(InvalidAction)
		}
	}

	def fromPost(action: String, json: JObject): Either[InvalidRequest, Request] = {
		action match {
			case "/messages/new" => ??? // AddMessages
			case "/nodes/join" => ??? // RegisterNode
			case _ =>
				Left(InvalidAction)
		}
	}
}
