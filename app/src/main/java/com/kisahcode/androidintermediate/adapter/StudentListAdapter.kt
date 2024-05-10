package com.kisahcode.androidintermediate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.database.Student
import com.kisahcode.androidintermediate.databinding.ItemStudentBinding

/**
 * Adapter for displaying a list of students in a RecyclerView using Paging 2.
 *
 * This adapter extends `PagedListAdapter` to efficiently display a paginated list of students in a RecyclerView.
 * It utilizes the Paging library's `DiffUtil.ItemCallback` for efficient item comparison and update calculations.
 *
 * @property WordsComparator DiffUtil.ItemCallback implementation to compare items in the list.
 */
class StudentListAdapter :
    PagedListAdapter<Student, StudentListAdapter.WordViewHolder>(WordsComparator()) {

    /**
     * Creates a new WordViewHolder instance and inflates the item layout.
     *
     * @param parent The parent view group.
     * @param viewType The view type of the new View.
     * @return WordViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    /**
     * Binds data to the item view at the specified position.
     *
     * @param holder The WordViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val student = getItem(position) as Student
        holder.bind(student)
    }

    /**
     * ViewHolder for the StudentListAdapter.
     *
     * Represents an item view in the RecyclerView.
     *
     * @property binding View binding for the item layout.
     */
    class WordViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the item view.
         *
         * @param data Student object containing student information.
         */
        fun bind(data: Student) {
            // Set the name of the student to the corresponding TextView
            binding.tvItemName.text = data.name
        }
    }

    /**
     * DiffUtil.ItemCallback implementation for comparing Student objects.
     * Used by the ListAdapter to determine if items have changed.
     */
    class WordsComparator : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.name == newItem.name
        }
    }
}