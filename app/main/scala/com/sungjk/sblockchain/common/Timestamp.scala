package com.sungjk.sblockchain.common

case class Timestamp(timestamp: Long) extends AnyVal {
	def >(other: Timestamp): Boolean = this.timestamp > other.timestamp
	def <(other: Timestamp): Boolean = this.timestamp < other.timestamp
	def >=(other: Timestamp): Boolean = this.timestamp >= other.timestamp
	def <=(other: Timestamp): Boolean = this.timestamp <= other.timestamp
}

object Timestamp {
	def current = Timestamp(System.currentTimeMillis())

	val zero = Timestamp(0)
}