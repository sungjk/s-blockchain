package com.sungjk.sblockchain.core

import java.net.URL

import com.sungjk.sblockchain.common.Timestamp

import scala.collection.mutable.ArrayBuffer

class BlockChain(chain: Chain = EmptyChain) {
	val messages : ArrayBuffer[BlockMessage] = ArrayBuffer.empty
	val nodes : ArrayBuffer[URL] = ArrayBuffer.empty

	def registerNode(address: String): Unit = nodes += new URL(address)

	def addMessage(data: String) : Int = {
		messages += BlockMessage(data)
		chain.size + 1
	}

	def checkPoWSolution(lastHash: String, proof: Long) : Boolean =
		ProofOfWork.validateProof(lastHash, proof)

	def addBlock(nonce: Long, previousHash: String = "") : BlockChain = {
		val block = Block(chain.size + 1, previousHash, Timestamp.current, messages.toList, nonce)
		messages.clear()
		new BlockChain(block :: chain)
	}

	def findProof() : Long = ProofOfWork.proofOfWork(getLastHash)

	def getLastBlock: Block = chain.block

	def getLastHash: String = chain.hash

	def getLastIndex: Int = this.chain.size

	def getChain: Chain = this.chain

}
