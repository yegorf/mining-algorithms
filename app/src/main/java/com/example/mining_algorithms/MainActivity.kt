package com.example.mining_algorithms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mining_algorithms.databinding.ActivityMainBinding
import com.example.mining_algorithms.event.MiningEndEvent
import com.example.mining_algorithms.event.NewBlockEvent
import com.example.mining_algorithms.event.RxBus
import com.example.mining_algorithms.export.exportToExcel
import com.example.mining_algorithms.service.MiningService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                binding.tvBlockchain.append(it.block.hash + "\n\n")
            })

        disposables.add(RxBus.subscribe(MiningEndEvent::class.java)
            .observeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.finishButtonsContainer.visibility = View.VISIBLE
            })
    }

    private fun startMining() {
        val complexity: Int = binding.etMiningComplexity.text.toString().toInt()
        val blocksCount: Int = binding.etBlocksCount.text.toString().toInt()
        val threadsCount: Int = binding.etThreadsCount.text.toString().toInt()

        binding.tvBlockchain.clearFocus()
        binding.finishButtonsContainer.clearFocus()
        binding.btnStartMining.clearFocus()

        MiningService.startParallelMining(blocksCount, threadsCount, complexity)
        binding.btnStartMining.visibility = View.GONE
    }

    private fun restart() {
        binding.tvBlockchain.text = ""
        binding.finishButtonsContainer.visibility = View.GONE
        binding.btnStartMining.visibility = View.VISIBLE

        binding.etMiningComplexity.text?.clear()
        binding.etBlocksCount.text?.clear()
        binding.etThreadsCount.text?.clear()
    }

    private fun exportResults() {
        exportToExcel(MiningService.blockchain, this)
    }
}