package com.example.mining_algorithms.service

import com.example.mining_algorithms.pojo.Block
import com.example.mining_algorithms.tools.calculateBlockHash
import com.example.mining_algorithms.tools.generateHashPrefix
import java.util.*
import kotlin.random.Random

class MiningThread(private val complexity: Int) : Thread() {

    override fun run() {
        mineBlockParallel()
    }

    private fun mineBlockParallel() {
        val lastBlock = MiningService.getLastBlock()
        val prefixString = generateHashPrefix(complexity)
        val newBlock = Block(
            "This is new block",
            lastBlock.hash,
            Date().time
        )

        while (newBlock.hash.substring(0, complexity) != prefixString) {
            if (isInterrupted) {
                return
            }
            newBlock.nonce = Random.nextInt(0, 1000000000)
            newBlock.hash = calculateBlockHash(newBlock)
            newBlock.timestamp = Date().time
//            println("${currentThread().name} -> ${newBlock.hash}")
        }

        println("New block (${currentThread().name}) -> $newBlock\nMining time: ${newBlock.timestamp - lastBlock.timestamp}")
        MiningService.addBlock(newBlock)
    }
}