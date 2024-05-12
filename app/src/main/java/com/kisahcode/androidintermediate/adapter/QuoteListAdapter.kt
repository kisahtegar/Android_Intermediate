package com.kisahcode.androidintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.databinding.ItemQuoteBinding
import com.kisahcode.androidintermediate.network.QuoteResponseItem

/**
 * Adapter for displaying quote items in a RecyclerView using Paging.
 *
 * @constructor Creates a new instance of QuoteListAdapter.
 */
class QuoteListAdapter : PagingDataAdapter<QuoteResponseItem, QuoteListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    /**
     * Creates a new MyViewHolder instance.
     *
     * @param parent The parent view group.
     * @param viewType The view type of the new View.
     * @return MyViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * Binds data to the MyViewHolder based on the item at the specified position.
     *
     * @param holder The MyViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    /**
     * ViewHolder for the QuoteListAdapter.
     *
     * Represents an item view in the RecyclerView.
     *
     * @param binding View binding for the item layout.
     */
    class MyViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the item view.
         *
         * @param data QuoteResponseItem object containing quote information.
         */
        fun bind(data: QuoteResponseItem) {
            binding.tvItemQuote.text = data.en
            binding.tvItemAuthor.text = data.author
        }
    }

    companion object {
        // DiffUtil.ItemCallback implementation for comparing QuoteResponseItem objects
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QuoteResponseItem>() {
            override fun areItemsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}