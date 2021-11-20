package com.example.mining_algorithms.event

import com.example.mining_algorithms.pojo.Block

data class NewBlockEvent(
    val block: Block
)
