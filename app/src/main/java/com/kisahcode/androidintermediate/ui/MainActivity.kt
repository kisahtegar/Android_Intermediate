package com.kisahcode.androidintermediate.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kisahcode.androidintermediate.adapter.LoadingStateAdapter
import com.kisahcode.androidintermediate.adapter.QuoteListAdapter
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * MainActivity class responsible for displaying a list of quotes in a RecyclerView.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with LinearLayoutManager
        binding.rvQuote.layoutManager = LinearLayoutManager(this)

        // Fetch and display quote data
        getData()
    }

    /**
     * Fetches quote data from the ViewModel and updates the RecyclerView adapter.
     */
    private fun getData() {
        // Initialize adapter for displaying quotes
        val adapter = QuoteListAdapter()

        // Set up RecyclerView with adapter and loading state footer
        binding.rvQuote.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry() // Retry loading if there was an error
            }
        )

        // Observe quote data from the ViewModel and submit it to the adapter
        mainViewModel.quote.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}