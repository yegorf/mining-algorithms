package com.example.mining_algorithms.pojo

import com.example.mining_algorithms.tools.calculateBlockHash

data class Block(
    var data: String,
    var previousHash: String,
    var timestamp: Long
) {
    var nonce: Int = 0
    var hash: String = calculateBlockHash(this)

    override fun toString(): String {
        return "Block(timestamp=$timestamp, previousHash='$previousHash', hash='$hash')"
    }
}
