package com.kisahcode.androidintermediate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.databinding.ItemStudentBinding
import com.kisahcode.androidintermediate.database.UniversityAndStudent

/**
 * Adapter for displaying a list of universities with their associated students in a RecyclerView.
 *
 * @property WordsComparator DiffUtil.ItemCallback implementation to compare items in the list.
 */
class UniversityAndStudentAdapter :
    ListAdapter<UniversityAndStudent, UniversityAndStudentAdapter.WordViewHolder>(WordsComparator()) {

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
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for the UniversityAndStudentAdapter.
     *
     * Represents an item view in the RecyclerView.
     *
     * @property binding View binding for the item layout.
     */
    class WordViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the item view.
         *
         * @param data UniversityAndStudent object containing university and student information.
         */
        fun bind(data: UniversityAndStudent) {
            // Extract student names and display them in a comma-separated format
            val arrayName = arrayListOf<String>()
            data.student.forEach {
                arrayName.add(it.name)
            }
            binding.tvItemName.text = arrayName.joinToString(separator = ", ")

            // Set the name of the university to the corresponding TextView
            binding.tvItemUniversity.text = data.university.name
            binding.tvItemUniversity.visibility = View.VISIBLE
        }
    }

    /**
     * DiffUtil.ItemCallback implementation for comparing UniversityAndStudent objects.
     * Used by the ListAdapter to determine if items have changed.
     */
    class WordsComparator : DiffUtil.ItemCallback<UniversityAndStudent>() {
        override fun areItemsTheSame(
            oldItem: UniversityAndStudent,
            newItem: UniversityAndStudent
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: UniversityAndStudent,
            newItem: UniversityAndStudent
        ): Boolean {
            return oldItem.university.name == newItem.university.name
        }
    }
}