package com.example.mining_algorithms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mining_algorithms.databinding.ActivityMainBinding
import com.example.mining_algorithms.event.MiningEndEvent
import com.example.mining_algorithms.event.NewBlockEvent
import com.example.mining_algorithms.event.RxBus
import com.example.mining_algorithms.presentation.BlocksAdapter
import com.example.mining_algorithms.service.MiningService
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val disposables = CompositeDisposable()
    private val adapter = BlocksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecycler()
        setOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        subscribeToEvents()
    }

    override fun onPause() {
        super.onPause()
        disposables.dispose()
        disposables.clear()
    }

    private fun initRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerBlocks.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerBlocks.context,
            layoutManager.orientation
        )
        binding.recyclerBlocks.addItemDecoration(dividerItemDecoration)
        binding.recyclerBlocks.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.btnStartMining.setOnClickListener {
            startMining()
        }
        binding.btnRestart.setOnClickListener {
            restart()
        }
        binding.btnExportResults.setOnClickListener {
            exportResults()
        }
    }

    private fun subscribeToEvents() {
        disposables.add(RxBus.subscribe(NewBlockEvent::class.java)
            .observeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.addBlock(it.block)
            })

        disposables.add(RxBus.subscribe(MiningEndEvent::class.java)
            .observeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.finishButtonsContainer.visibility = View.VISIBLE
                binding.tvResultData.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                val timeSec: Double = (MiningService.getTotalSpentTime().toDouble() / 1000)
                val count = MiningService.totalHashCount
                val rate = count.toDouble() / MiningService.getTotalSpentTime().toDouble()

                binding.tvResultData.text =
                    "Total time spent: $timeSec s\nTotal hashes count: $count\nHash rate: $rate h/s"

                binding.viewGraph.visibility = View.VISIBLE

                val blockchain = MiningService.blockchain
                val pointsCount = blockchain.size - 1
                val series = LineGraphSeries<DataPoint>()

                for (i in 0..pointsCount) {
                    series.appendData(DataPoint(i.toDouble(), blockchain[i].timeSpent.toDouble()), true, pointsCount)
                }

                val thickness = binding.etThreadsCount.text.toString().toInt()
                series.thickness = thickness
                binding.viewGraph.addSeries(series)
            })
    }

    private fun startMining() {
        val complexity: Int = binding.etMiningComplexity.text.toString().toInt()
        val blocksCount: Int = binding.etBlocksCount.text.toString().toInt()
        val threadsCount: Int = binding.etThreadsCount.text.toString().toInt()

        binding.etBlocksCount.clearFocus()
        binding.etMiningComplexity.clearFocus()
        binding.etMiningComplexity.clearFocus()

        MiningService.startParallelMining(blocksCount, threadsCount, complexity)
        binding.btnStartMining.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun restart() {
        binding.finishButtonsContainer.visibility = View.GONE
        binding.btnStartMining.visibility = View.VISIBLE

        binding.etMiningComplexity.text?.clear()
        binding.etBlocksCount.text?.clear()
        binding.etThreadsCount.text?.clear()

        binding.tvResultData.visibility = View.GONE
        adapter.clear()
        MiningService.blockchain.clear()
        binding.viewGraph.removeAllSeries()
        binding.viewGraph.visibility = View.GONE
    }

    private fun exportResults() {
        //exportToExcel(MiningService.blockchain, this)
    }
}