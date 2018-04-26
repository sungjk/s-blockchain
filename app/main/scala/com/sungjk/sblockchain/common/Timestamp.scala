package com.sungjk.sblockchain.common

case class Timestamp(timestamp: Long)

object Timestamp {
	def current = Timestamp(System.currentTimeMillis())

	val zero = Timestamp(0)
}