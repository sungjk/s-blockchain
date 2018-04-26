package com.sungjk.sblockchain.common

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

object IdGenerator {
    private val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
    private val nodeId0 = new AtomicInteger()
    private def timestamp(): Long = Timestamp.current.timestamp
    private def nodeId(): Int = {
        def mod(x: Int): Int = if (x >= 0) x % 2048 else Math.abs((x - Integer.MIN_VALUE) % 2048)
        mod(nodeId0.getAndIncrement())
    }
    private def randomString(length: Int): String = {
        ((0 until length) map { _ => chars(ThreadLocalRandom.current().nextInt(chars.length)) }).mkString
    }
    private def globallyUniqueRandomString(length: Int): String = {
        f"${randomString(length)}_${timestamp()}%x_${nodeId()}%03x"
    }
}
