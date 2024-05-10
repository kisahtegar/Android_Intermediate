package com.kisahcode.androidintermediate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kisahcode.androidintermediate.adapter.StudentAndUniversityAdapter
import com.kisahcode.androidintermediate.adapter.StudentListAdapter
import com.kisahcode.androidintermediate.adapter.StudentWithCourseAdapter
import com.kisahcode.androidintermediate.adapter.UniversityAndStudentAdapter
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding

/**
 * MainActivity class responsible for displaying student-related data in a RecyclerView.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory((application as MyApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with LinearLayoutManager
        binding.rvStudent.layoutManager = LinearLayoutManager(this)

        // Load initial data
        getStudent()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_single_table -> {
                // Load student data
                getStudent()
                return true
            }
            R.id.action_many_to_one -> {
                // Load student and university data
                getStudentAndUniversity()
                true
            }
            R.id.action_one_to_many -> {
                // Load university and student data
                getUniversityAndStudent()
                true
            }

            R.id.action_many_to_many -> {
                // Load student with course data
                getStudentWithCourse()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Retrieves and displays a list of students in the RecyclerView.
     *
     * This function fetches the list of students from the ViewModel and observes changes using LiveData.
     * When the data changes, it updates the RecyclerView adapter with the new list of students.
     */
    private fun getStudent() {
        // Initialize a new instance of StudentListAdapter
        val adapter = StudentListAdapter()

        // Set the adapter to the RecyclerView
        binding.rvStudent.adapter = adapter

        // Observe the list of students from the ViewModel
        mainViewModel.getAllStudent().observe(this) {
            // Submit the new list of students to the adapter
            Log.d(TAG, "getStudent: $it")
            it.forEach(::println)
            adapter.submitList(it)
        }
    }

    /**
     * Retrieves and displays a list of students with their associated universities in the RecyclerView.
     *
     * This function fetches the list of student-university pairs from the ViewModel and observes
     * changes using LiveData. When the data changes, it updates the RecyclerView adapter with the
     * new list of student-university pairs.
     */
    private fun getStudentAndUniversity() {
        // Initialize a new instance of StudentAndUniversityAdapter
        val adapter = StudentAndUniversityAdapter()

        // Set the adapter to the RecyclerView
        binding.rvStudent.adapter = adapter

        // Observe the list of student-university pairs from the ViewModel
        mainViewModel.getAllStudentAndUniversity().observe(this) {
            // Submit the new list of student-university pairs to the adapter
            Log.d(TAG, "getStudentAndUniversity: $it")
            adapter.submitList(it)
        }
    }

    /**
     * Retrieves and displays a list of universities with their associated students in the RecyclerView.
     *
     * This function fetches the list of university-student pairs from the ViewModel and observes
     * changes using LiveData. When the data changes, it updates the RecyclerView adapter with the
     * new list of university-student pairs.
     */
    private fun getUniversityAndStudent() {
        // Initialize a new instance of UniversityAndStudentAdapter
        val adapter = UniversityAndStudentAdapter()

        // Set the adapter to the RecyclerView
        binding.rvStudent.adapter = adapter

        // Observe the list of university-student pairs from the ViewModel
        mainViewModel.getAllUniversityAndStudent().observe(this) {
            // Submit the new list of university-student pairs to the adapter
            Log.d(TAG, "getUniversityAndStudent: $it")
            adapter.submitList(it)
        }
    }

    /**
     * Retrieves and displays a list of students with their associated courses in the RecyclerView.
     *
     * This function fetches the list of student-course pairs from the ViewModel and observes changes
     * using LiveData. When the data changes, it updates the RecyclerView adapter with the new list
     * of student-course pairs.
     */
    private fun getStudentWithCourse() {
        // Initialize a new instance of getStudentWithCourse
        val adapter = StudentWithCourseAdapter()

        // Set the adapter to the RecyclerView
        binding.rvStudent.adapter = adapter

        // Observe the list of student-course pairs from the ViewModel
        mainViewModel.getAllStudentWithCourse().observe(this) {
            // Submit the new list of student-course pairs to the adapter
            Log.d(TAG, "getStudentWithCourse: $it")
            adapter.submitList(it)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}