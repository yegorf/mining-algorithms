package com.example.mining_algorithms.service

import com.example.mining_algorithms.event.MiningEndEvent
import com.example.mining_algorithms.event.NewBlockEvent
import com.example.mining_algorithms.event.RxBus
import com.example.mining_algorithms.pojo.Block
import com.example.mining_algorithms.tools.mineBlock
import java.util.*

object MiningService {
    val blockchain: ArrayList<Block> = arrayListOf()
    private val miningThreads = arrayListOf<MiningThread>()

    private var threadsCount = 0
    private var complexity = 0
    private var blocksCount = 0

    var totalHashCount = 0

    @Synchronized
    fun iterateHashCount() {
        totalHashCount++
    }

    fun getLastBlock() = blockchain.last()

    fun getTotalSpentTime(): Long {
        var time = 0L
        blockchain.forEach {
            time += it.timeSpent
        }
        return time
    }

    @Synchronized
    fun addBlock(block: Block) {
        stopThreads()
        blockchain.add(block)
        RxBus.publish(NewBlockEvent(block))

        if (blockchain.size < blocksCount + 1) {
            miningThreads.clear()
            for (i in 1..threadsCount) {
                miningThreads.add(MiningThread(complexity))
            }
            startThreads()
        } else {
            RxBus.publish(MiningEndEvent())
        }
    }

    fun startParallelMining(blocksCount: Int, threadsCount: Int, complexity: Int) {
        clear()

        MiningService.complexity = complexity
        MiningService.threadsCount = threadsCount
        MiningService.blocksCount = blocksCount

        generateGenesisBlock()
        for (i in 1..threadsCount) {
            miningThreads.add(MiningThread(complexity))
        }
        startThreads()
    }

    private fun clear() {
        miningThreads.clear()
        blockchain.clear()
    }

    private fun startThreads() {
        miningThreads.forEach {
            it.start()
        }
    }

    private fun stopThreads() {
        miningThreads.forEach {
            it.interrupt()
        }
    }

    fun startMining(blocksCount: Int, complexity: Int): List<Block> {
        generateGenesisBlock()
        for (i in 1..blocksCount) {
            mineBlock(complexity)
        }
        return blockchain
    }

    private fun mineBlock(complexity: Int) {
        val lastBlock = blockchain.last()
        val newBlock = mineBlock(lastBlock.hash, complexity)

        blockchain.add(newBlock)
    }

    private fun generateGenesisBlock() {
        val genesisBlock = Block(
            "",
            Date().time
        )

        blockchain.add(genesisBlock)
    }
}