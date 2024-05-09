package com.kisahcode.androidintermediate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kisahcode.androidintermediate.database.StudentWithCourse
import com.kisahcode.androidintermediate.databinding.ItemStudentBinding

/**
 * Adapter for displaying a list of students with their associated courses in a RecyclerView.
 *
 * @property WordsComparator DiffUtil.ItemCallback implementation to compare items in the list.
 */
class StudentWithCourseAdapter :
    ListAdapter<StudentWithCourse, StudentWithCourseAdapter.WordViewHolder>(WordsComparator()) {

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
     * ViewHolder for the StudentWithCourseAdapter.
     *
     * Represents an item view in the RecyclerView.
     *
     * @property binding View binding for the item layout.
     */
    class WordViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds data to the item view.
         *
         * @param data StudentWithCourse object containing student and course information.
         */
        fun bind(data: StudentWithCourse) {
            // Set the name of the university to the corresponding TextView
            binding.tvItemUniversity.text = data.studentAndUniversity.university?.name
            binding.tvItemUniversity.visibility = View.VISIBLE

            // Set the name of the student to the corresponding TextView
            binding.tvItemName.text = data.studentAndUniversity.student.name

            // Extract course names and display them in a comma-separated format
            val arrayCourse = arrayListOf<String>()
            data.course.forEach {
                arrayCourse.add(it.name)
            }
            binding.tvItemCourse.text = arrayCourse.joinToString(separator = ", ")
            binding.tvItemCourse.visibility = View.VISIBLE
        }
    }

    /**
     * DiffUtil.ItemCallback implementation for comparing StudentWithCourse objects.
     * Used by the ListAdapter to determine if items have changed.
     */
    class WordsComparator : DiffUtil.ItemCallback<StudentWithCourse>() {
        override fun areItemsTheSame(oldItem: StudentWithCourse, newItem: StudentWithCourse): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StudentWithCourse, newItem: StudentWithCourse): Boolean {
            return oldItem.studentAndUniversity.student.name == newItem.studentAndUniversity.student.name
        }
    }
}