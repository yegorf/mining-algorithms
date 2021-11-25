package com.example.mining_algorithms.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mining_algorithms.databinding.ItemBlockBinding
import com.example.mining_algorithms.pojo.Block

class BlocksAdapter : RecyclerView.Adapter<BlocksAdapter.BlockHolder>() {
    private val data = arrayListOf<Block>()

    class BlockHolder(private val binding: ItemBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(block: Block) {
            binding.tvHash.text = block.hash
            binding.tvTime.text = "${block.timeSpent} ms"
            binding.tvTransactions.text = "${block.transactions.size} transactions"
        }
    }

    fun addBlock(block: Block) {
        data.add(block)
        notifyItemInserted(data.size)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockHolder {
        val binding = ItemBlockBinding.inflate(LayoutInflater.from(parent.context))
        return BlockHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}