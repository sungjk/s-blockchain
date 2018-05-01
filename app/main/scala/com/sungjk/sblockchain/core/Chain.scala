package com.sungjk.sblockchain.core

import com.sungjk.sblockchain.common.Timestamp
import com.sungjk.sblockchain.utils.StringUtils

sealed trait Chain {
	val size: Int
	val hash: String
	val block: Block
	val nonce: Long

	def ::(block: Block): Chain = ChainLink(this.size + 1, block, hash, this)

	override def toString: String = s"$size:$hash:${block.toContentString}:$nonce"
}

case class ChainLink(
	index: Int,
	block: Block,
	previousHash: String,
	tail: Chain,
	timestamp: Timestamp = Timestamp.current
) extends Chain {
	val size = 1 + tail.size
	val hash = StringUtils.sha256Hex(this.toString)
	val nonce = block.nonce
}

case object EmptyChain extends Chain {
	val size = 0
	val hash = "1"
	val block = null
	val nonce = 100L
}

object Chain {
	def apply(blocks: ChainLink*): Chain = {
		if (blocks.isEmpty) EmptyChain else {
			val chainLink = blocks.head
			ChainLink(chainLink.index, chainLink.block, chainLink.previousHash, apply(blocks.tail: _*))
		}
	}
}
