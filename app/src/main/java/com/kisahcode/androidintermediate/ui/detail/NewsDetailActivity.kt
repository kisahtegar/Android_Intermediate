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

        with(IntentCompat.getParcelableExtra(intent, NEWS_DATA, NewsEntity::class.java)){
            if (this != null){
                newsDetail = this
                supportActionBar?.title = newsDetail.title
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(newsDetail.url.toString())

                viewModel.setNewsData(newsDetail)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        this.menu = menu
        viewModel.bookmarkStatus.observe(this) { status ->
            setBookmarkState(status)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_bookmark) {
            if (this::newsDetail.isInitialized){
                viewModel.changeBookmark(newsDetail)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBookmarkState(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_bookmark)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmarked_white)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_bookmark_white)
        }
    }

    companion object {
        const val NEWS_DATA = "data"
    }
}