package com.example.mining_algorithms.service

import com.example.mining_algorithms.pojo.Block
import com.example.mining_algorithms.pojo.Transaction
import com.example.mining_algorithms.tools.calculateBlockHash
import com.example.mining_algorithms.tools.generateHashPrefix
import java.util.*
import kotlin.random.Random

private const val nonceRange = 1000000000
class MiningThread(private val complexity: Int) : Thread() {
    private fun mineBlockParallel() {
        val lastBlock = MiningService.getLastBlock()
        val prefixString = generateHashPrefix(complexity)
        val newBlock = Block(
            lastBlock.hash,
            Date().time
        )
        while (newBlock.hash.substring(0, complexity) != prefixString) {
            if (isInterrupted) {
                return
            }
            newBlock.nonce = Random.nextInt(0, nonceRange)
            newBlock.hash = calculateBlockHash(newBlock)
            newBlock.transactions.add(Transaction(System.currentTimeMillis()))
            newBlock.timestamp = Date().time
            newBlock.timeSpent = newBlock.timestamp - lastBlock.timestamp

            MiningService.iterateHashCount()
        }
        MiningService.addBlock(newBlock)
    }

    override fun run() {
        mineBlockParallel()
    }
}