package com.sungjk.sblockchain.core

import com.sungjk.sblockchain.common.Timestamp

case class BlockMessage(data: String)

object GenesisBlock extends Block(
    index = 0,
    previousHash = "",
    timestamp = Timestamp.current,
    messages = Seq(BlockMessage("Genesis")),
    nonce = 0,
    hash = Crypto.sha256Hash("Genesis")
)

case class Block(
    index: Long,
    previousHash: String,
    timestamp: Timestamp,
    messages: Seq[BlockMessage],
    nonce: Long,
    hash: String
)
