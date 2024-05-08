package com.kisahcode.androidintermediate.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kisahcode.androidintermediate.R
import com.kisahcode.androidintermediate.data.local.entity.NewsEntity
import com.kisahcode.androidintermediate.databinding.ItemNewsBinding
import com.kisahcode.androidintermediate.ui.list.NewsAdapter.MyViewHolder
import com.kisahcode.androidintermediate.utils.DateFormatter
import java.util.*

/**
 * Adapter for managing the list of news items displayed in the RecyclerView.
 *
 * This adapter is responsible for displaying news items in a RecyclerView. It utilizes a ListAdapter
 * with DiffUtil for efficient updates when the list of news items changes.
 *
 * @property onItemClick Callback triggered when a news item is clicked.
 */
class NewsAdapter(private val onItemClick: (NewsEntity) -> Unit) : ListAdapter<NewsEntity, MyViewHolder>(
    DIFF_CALLBACK
) {

    /**
     * Inflates the layout for the view holder.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return Returns a new instance of MyViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onItemClick)
    }

    /**
     * Binds the data to the view holder.
     *
     * @param holder The view holder to bind the data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }

    /**
     * ViewHolder for displaying each news item in the RecyclerView.
     *
     * @property binding The binding for the item layout.
     * @property onItemClick Callback triggered when the item is clicked.
     */
    class MyViewHolder(private val binding: ItemNewsBinding, val onItemClick: (NewsEntity) -> Unit) : RecyclerView.ViewHolder(
        binding.root
    ) {
        /**
         * Binds the news data to the view holder.
         *
         * @param news The news entity to bind.
         */
        fun bind(news: NewsEntity) {
            binding.tvItemTitle.text = news.title
            binding.tvItemPublishedDate.text = DateFormatter.formatDate(news.publishedAt, TimeZone.getDefault().id)
            Glide.with(itemView.context)
                .load(news.urlToImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
                )
                .into(binding.imgPoster)
            itemView.setOnClickListener {
                onItemClick(news)
            }
        }
    }

    companion object {
        // DiffUtil callback for comparing items in the list.
        val DIFF_CALLBACK: DiffUtil.ItemCallback<NewsEntity> =
            object : DiffUtil.ItemCallback<NewsEntity>() {
                override fun areItemsTheSame(oldUser: NewsEntity, newUser: NewsEntity): Boolean {
                    return oldUser.title == newUser.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: NewsEntity, newUser: NewsEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}