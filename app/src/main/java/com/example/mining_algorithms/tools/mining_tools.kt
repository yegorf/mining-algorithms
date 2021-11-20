package com.example.mining_algorithms.tools

import com.example.mining_algorithms.pojo.Block
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.random.Random

fun calculateBlockHash(block: Block): String {
    val dataToHash: String = block.previousHash +
            block.timestamp.toString() +
            block.nonce.toString() +
            block.data
    val bytes = SHA256Encoder.hash(dataToHash.toByteArray(StandardCharsets.UTF_8))
    val buffer = StringBuffer()
    bytes.forEach {
        buffer.append(String.format("%02x", it))
    }
    return buffer.toString()
}

fun mineBlock(previousHash: String, complexity: Int): Block {
    val newBlock = Block(
        getTransactions(),
        previousHash,
        Date().time
    )
    val prefixString = generateHashPrefix(complexity)
    while (newBlock.hash.substring(0, complexity) != prefixString) {
        newBlock.nonce = Random.nextInt(0, 1000000000)
        newBlock.hash = calculateBlockHash(newBlock)
        newBlock.timestamp = Date().time
    }
    return newBlock
}

fun isBlockchainValid(blockchain: List<Block>, complexity: Int): Boolean {
    val prefixString = generateHashPrefix(complexity)
    var isValid = true

    for (i in 0..blockchain.size) {
        val currentBlock = blockchain[i]
        val previousHash = if (i == 0) {
            ""
        } else {
            blockchain[i - 1].hash
        }

        isValid = currentBlock.hash == calculateBlockHash(currentBlock)
                && currentBlock.previousHash == previousHash
                && currentBlock.hash.substring(0, complexity) == prefixString

        if (!isValid) {
            break
        }
    }

    return isValid
}

fun generateHashPrefix(complexity: Int) = String(CharArray(complexity))
    .replace('\u0000', '0')

fun getTransactions() = "This is new block"