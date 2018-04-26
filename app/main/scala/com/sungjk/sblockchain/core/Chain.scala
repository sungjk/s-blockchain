package com.sungjk.sblockchain.core

sealed trait Chain {
    val size: Int
    val blockHash: String
    val block: Block
    val proof: Long

    def ::(block: Block): Chain = ???
    override def toString: String = ???
}

case object EmptyChain extends Chain {
    val size = ???
    val blockHash = ???
    val block = ???
    val proof = ???
}

object Chain {
    // TODO apply
}
