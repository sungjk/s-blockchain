package com.sungjk.sblockchain.core

import com.sungjk.sblockchain.common.Timestamp
import org.json4s.JsonDSL._
import org.json4s._

case class BlockMessage(data: String) {
	def toJson: JObject = "data" -> data
}

case class Block(
	index: Long,
	previousHash: String,
	timestamp: Timestamp,
	messages: List[BlockMessage],
	nonce: Long
) {
	def toJson: JObject =
		("index" -> index) ~
		("previousHash" -> previousHash) ~
		("timestamp" -> timestamp.timestamp) ~
		("messages" -> (messages map { _.toJson })) ~
		("nonce" -> nonce)

	def toContentString: String =
		s"$index:${previousHash.toString}:${timestamp.timestamp}:${messages map { _.data } mkString ":"}:$nonce"
}
