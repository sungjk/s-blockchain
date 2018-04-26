package com.sungjk.sblockchain.core

import java.math.BigInteger
import java.security.MessageDigest

object Crypto {
	def sha256Hash(text: String) : String = String.format("%064x",
		new BigInteger(1, MessageDigest.getInstance("SHA-256").digest(text.getBytes("UTF-8"))))
}
