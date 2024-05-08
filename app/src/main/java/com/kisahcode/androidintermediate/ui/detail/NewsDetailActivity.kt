package com.kisahcode.androidintermediate.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.databinding.ActivityNewsDetailBinding
import com.kisahcode.androidintermediate.ui.ViewModelFactory

/**
 * Activity class for displaying news detail.
 *
 * This activity displays the detail view of a news article, including its title and content.
 * It allows users to bookmark or unbookmark the article.
 */
class NewsDetailActivity : AppCompatActivity() {

    private lateinit var newsDetail: NewsEntity
    private lateinit var binding: ActivityNewsDetailBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: NewsDetailViewModel by viewModels {
        factory
    }
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the news detail from the intent extras
        with(IntentCompat.getParcelableExtra(intent, NEWS_DATA, NewsEntity::class.java)){
            if (this != null){
                newsDetail = this
                supportActionBar?.title = newsDetail.title
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(newsDetail.url.toString())

                // Set the news data in the ViewModel
                viewModel.setNewsData(newsDetail)
            }
        }
    }

    /**
     * Initializes the options menu.
     *
     * This method inflates the menu resource file and sets up the menu items. It also observes the
     * bookmark status LiveData to update the bookmark icon based on the current state.
     *
     * @param menu The options menu.
     * @return Returns true to display the menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu resource file
        menuInflater.inflate(R.menu.detail_menu, menu)

        // Save the menu instance
        this.menu = menu

        // Observe the bookmark status LiveData to update the bookmark icon
        viewModel.bookmarkStatus.observe(this) { status ->
            setBookmarkState(status)
        }
        return true
    }

    /**
     * Handles menu item selection.
     *
     * This method is called when a menu item is selected. It checks if the selected item
     * is the bookmark action item. If it is, it triggers the bookmark status change for the
     * current news article.
     *
     * @param item The selected menu item.
     * @return Returns true if the menu item selection is handled, false otherwise.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark) {
            // Check if the selected item is the bookmark action item
            if (this::newsDetail.isInitialized) {
                // Trigger bookmark status change for the current news article
                viewModel.changeBookmark(newsDetail)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Sets the bookmark icon based on the bookmark status.
     *
     * This method updates the bookmark icon in the options menu based on the bookmark status
     * of the news article. If the article is bookmarked, it sets the bookmark icon to a filled
     * bookmark icon; otherwise, it sets it to an empty bookmark icon.
     *
     * @param state The current bookmark status of the news article.
     */
    private fun setBookmarkState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state) {
            // If the article is bookmarked, set the bookmark icon to a filled bookmark icon
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmarked_white)
        } else {
            // If the article is not bookmarked, set the bookmark icon to an empty bookmark icon
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_white)
        }
    }

    companion object {
        const val NEWS_DATA = "data"
    }
}