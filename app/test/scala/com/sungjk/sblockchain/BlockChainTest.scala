package com.sungjk.sblockchain

import com.sungjk.sblockchain.common.Timestamp
import com.sungjk.sblockchain.core._
import org.scalatest.FunSuite

class BlockChainTest extends FunSuite {

	test("Chain.apply") {
		val empty = Chain()
		assert(empty.size == 0)

		val message = BlockMessage("Jeremy is awesome!")
		val block = Block(0, "", Timestamp.current, List(message), 1)
		val link = ChainLink(0, block, "abc", empty)
		val justOne = Chain(link)
		assert(justOne.size == 1)
	}

	test("ProofOfWork.validateProof") {
		val lastHash = "abcdefg"
		assert(ProofOfWork.validateProof(lastHash, 46276L))
	}
}
