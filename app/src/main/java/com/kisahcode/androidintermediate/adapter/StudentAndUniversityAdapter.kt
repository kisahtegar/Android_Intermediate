package com.kisahcode.androidintermediate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.database.StudentAndUniversity
import com.kisahcode.androidintermediate.databinding.ItemStudentBinding

/**
 * Adapter for displaying a list of students with their associated universities in a RecyclerView.
 *
 * @property WordsComparator DiffUtil.ItemCallback implementation to compare items in the list.
 */
class StudentAndUniversityAdapter :
    ListAdapter<StudentAndUniversity, StudentAndUniversityAdapter.WordViewHolder>(WordsComparator()) {

    /**
     * Creates a new [WordViewHolder] instance and inflates the item layout.
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
     * ViewHolder for the StudentAndUniversityAdapter.
     *
     * Represents an item view in the RecyclerView.
     *
     * @property binding View binding for the item layout.
     */
    class WordViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the item view.
         *
         * @param data StudentAndUniversity object containing student and university information.
         */
        fun bind(data: StudentAndUniversity) {
            // Set the name of the student to the corresponding TextView
            binding.tvItemName.text = data.student.name

            // Set the name of the associated university to the corresponding TextView
            // If the student is not associated with any university, hide the university TextView
            binding.tvItemUniversity.text = data.university?.name
            binding.tvItemUniversity.visibility = View.VISIBLE
        }
    }

    /**
     * DiffUtil.ItemCallback implementation for comparing StudentAndUniversity objects.
     * Used by the ListAdapter to determine if items have changed.
     */
    class WordsComparator : DiffUtil.ItemCallback<StudentAndUniversity>() {
        override fun areItemsTheSame(oldItem: StudentAndUniversity, newItem: StudentAndUniversity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StudentAndUniversity, newItem: StudentAndUniversity): Boolean {
            return oldItem.student.name == newItem.student.name
        }
    }
}