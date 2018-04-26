package com.sungjk.sblockchain.core

import scala.annotation.tailrec

object ProofOfWork {
    @tailrec
    private def proofHelper(lastHash: String, proof: Long): Long = {
        if (validateProof(lastHash, proof)) proof else proofHelper(lastHash, proof + 1)
    }

	def proofOfWork(lastHash: String): Long = {
        proofHelper(lastHash, 0)
    }

	def validateProof(lastHash: String, proof: Long): Boolean = {
		val candidate: String = lastHash ++ proof.toString
        val candidateHash = Crypto.sha256Hash(candidate)
        (candidateHash take 2) == "00"
	}
}
