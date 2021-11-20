package com.example.mining_algorithms

import com.example.mining_algorithms.paralel.ParallelMiningService
import org.junit.Test
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random

class Testi {

    @Test
    fun test() {
        val dataToHash = "kek lol"
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(dataToHash.toByteArray(StandardCharsets.UTF_8))
        val buffer = StringBuffer()

        bytes.forEach {
            buffer.append(String.format("%02x", it))
        }

        println(buffer)
    }

    @Test
    fun test2() {
        ParallelMiningService().startMining(4, 3)
    }

    @Test
    fun test4() {
        for (i in 0..2) {
            Thread {
                for (j in 0..2) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    println(Thread.currentThread().name + " " + j)
                }
            }.start()
        }
    }

    @Test
    fun test3() {
        MiningService(1, object : MiningService.MiningServiceListener {
            override fun onGenesisBlockCreated(block: Block) {
                println("genesis -> \n" + block.hash)
            }

            override fun onBlockFound(block: Block) {
                println(block.hash)
            }
        }).startMining(5)
    }
}