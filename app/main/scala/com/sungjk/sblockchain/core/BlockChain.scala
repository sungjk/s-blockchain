package com.sungjk.sblockchain.core

import scala.util.Try

sealed trait Chain {
    val size: Int
    val blockHash: String
    val block: Block
    val proof: Long
}

class BlockChain(chain: Chain = EmptyChain) {

    def firstBlock: Block = ???

    def latestBlock: Block = ???

    def contains(blockMessage: BlockMessage): Boolean = ???

    def appendBlocks( newBlocks: Seq[Block] ): Try[BlockChain] = ???

    def addBlock(newBlock: Block): Try[ BlockChain ] = ???

    def addMessage(data: String, nonce: Long = 0): Try[BlockChain] =
        addBlock(generateNextBlock(Seq(BlockMessage(data)), nonce))

    def generateNextBlock(blockMessages: Seq[BlockMessage], nonce: Long): Block = ???

    override def toString: String = ???
}
