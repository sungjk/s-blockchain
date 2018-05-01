package com.sungjk.sblockchain.utils

import java.security.MessageDigest

import com.sungjk.sblockchain.core.Block

object StringUtils {
	private val digest = MessageDigest.getInstance("SHA-256")

	def sha256Hex(str: String): String = {
		val bytes = digest.digest(str.getBytes("UTF-8"))
		toHexString(bytes)
	}

	def hash(block: Block): String = sha256Hex(block.toContentString)

	def toHexString(value: Array[Byte]): String = (value map { x => f"$x%02x" }).mkString

	def fromHexString(hexString: String): Array[Byte] = {
		(hexString.grouped(2).toSeq map { x =>
			if (x.length != 2) {
				throw new Exception("Invalid hex string")
			}
			val y = x.toLowerCase
			def num(c: Char): Int =
				if ('0' <= c && c <= '9') c - '0'
				else if ('a' <= c && c <= 'f') c - 'a' + 10
				else throw new Exception(s"Invalid hex string: $c")
			(num(y(0)) * 16 + num(y(1))).toByte
		}).toArray
	}
}
