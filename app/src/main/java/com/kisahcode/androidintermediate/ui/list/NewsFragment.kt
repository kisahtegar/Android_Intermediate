package com.kisahcode.androidintermediate.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.data.Result
import com.kisahcode.androidintermediate.databinding.FragmentNewsBinding
import com.kisahcode.androidintermediate.ui.ViewModelFactory
import com.kisahcode.androidintermediate.ui.detail.NewsDetailActivity

/**
 * Fragment responsible for displaying a list of news items.
 *
 * This fragment displays a list of news items either from the headline news or bookmarked news,
 * depending on the specified tab. It observes changes in the ViewModel to update the UI accordingly.
 */
class NewsFragment : Fragment() {
    private var tabName: String? = null

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    /**
     * Called immediately after onCreateView() and is responsible for initializing the UI components
     * and setting up the observers for ViewModel data changes.
     *
     * @param view The view returned by onCreateView().
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabName = arguments?.getString(ARG_TAB)

        // Get the ViewModelFactory instance to create ViewModel objects
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())

        // Obtain the NewsViewModel instance using viewModels delegate and ViewModelFactory
        val viewModel: NewsViewModel by viewModels {
            factory
        }

        // Initialize the adapter for the RecyclerView
        val newsAdapter = NewsAdapter { news ->
            // Handle item click event by starting NewsDetailActivity and passing selected news data
            val intent = Intent(activity, NewsDetailActivity::class.java)
            intent.putExtra(NewsDetailActivity.NEWS_DATA, news)
            startActivity(intent)
        }

        // Depending on the specified tab, observe changes in the ViewModel data
        if (tabName == TAB_NEWS) {
            // Observe changes in the headline news data
            viewModel.getHeadlineNews().observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            // Show loading progress bar
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            // Hide progress bar and update RecyclerView with news data
                            binding?.progressBar?.visibility = View.GONE
                            val newsData = result.data
                            newsAdapter.submitList(newsData)
                        }
                        is Result.Error -> {
                            // Hide progress bar and show error message if data loading fails
                            binding?.progressBar?.visibility = View.GONE
                            binding?.viewError?.root?.visibility = View.VISIBLE
                            binding?.viewError?.tvError?.text = getString(R.string.something_wrong)
                        }
                    }
                }
            }
        } else if (tabName == TAB_BOOKMARK) {
            // Observe changes in the bookmarked news data
            viewModel.getBookmarkedNews().observe(viewLifecycleOwner) { bookmarkedNews ->
                // Update RecyclerView with bookmarked news data and handle visibility of error message
                newsAdapter.submitList(bookmarkedNews)
                binding?.progressBar?.visibility = View.GONE
                binding?.viewError?.tvError?.text = getString(R.string.no_data)
                binding?.viewError?.root?.visibility =
                    if (bookmarkedNews.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }

        // Set up RecyclerView with the news adapter
        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    /**
     * Cleans up resources when the fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_NEWS = "news"
        const val TAB_BOOKMARK = "bookmark"
    }
}