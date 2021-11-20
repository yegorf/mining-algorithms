package com.example.mining_algorithms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mining_algorithms.databinding.ActivityMainBinding
import com.example.mining_algorithms.event.MiningEndEvent
import com.example.mining_algorithms.event.NewBlockEvent
import com.example.mining_algorithms.event.RxBus
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
            val complexity: Int = binding.etMiningComplexity.text.toString().toInt()
            val blocksCount: Int = binding.etBlocksCount.text.toString().toInt()
            val threadsCount: Int = binding.etThreadsCount.text.toString().toInt()

            MiningService.startParallelMining(blocksCount, threadsCount, complexity)
            it.visibility = View.GONE
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
}