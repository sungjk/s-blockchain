package com.sungjk.sblockchain.core

import com.sungjk.sblockchain.utils.StringUtils

object ProofOfWork {
	private def proofHelper(lastHash: String, nonce: Long): Long = {
		if (validateProof(lastHash, nonce)) nonce else proofHelper(lastHash, nonce + 1)
	}

	def proofOfWork(lastHash: String): Long = {
		proofHelper(lastHash, 0)
	}

	def validateProof(lastHash: String, nonce: Long): Boolean = {
		val candidate = s"$lastHash$nonce"
		val candidateHash = StringUtils.sha256Hex(candidate)
		(candidateHash take 4) == "0000"
	}
}
