package com.kisahcode.androidintermediate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kisahcode.androidintermediate.adapter.StudentAndUniversityAdapter
import com.kisahcode.androidintermediate.adapter.StudentListAdapter
import com.kisahcode.androidintermediate.adapter.StudentWithCourseAdapter
import com.kisahcode.androidintermediate.adapter.UniversityAndStudentAdapter
import com.kisahcode.androidintermediate.databinding.ActivityMainBinding
import com.kisahcode.androidintermediate.helper.SortType

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
                showSortingOptionMenu(true)
                return true
            }
            R.id.action_many_to_one -> {
                // Load student and university data
                getStudentAndUniversity()
                showSortingOptionMenu(false)
                true
            }
            R.id.action_one_to_many -> {
                // Load university and student data
                getUniversityAndStudent()
                showSortingOptionMenu(false)
                true
            }

            R.id.action_many_to_many -> {
                // Load student with course data
                getStudentWithCourse()
                showSortingOptionMenu(false)
                true
            }

            R.id.action_sort -> {
                showSortingPopupMenu()
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

    /**
     * Shows or hides the sorting option menu item in the activity's options menu based on the provided flag.
     *
     * If the flag is true, the sorting option menu item is shown; otherwise, it is hidden. This
     * function is used to dynamically control the visibility of the sorting option menu item based
     * on the current context or state of the activity.
     *
     * @param isShow Boolean flag indicating whether to show or hide the sorting option menu item.
     *               True to show the menu item, false to hide it.
     */
    private fun showSortingOptionMenu(isShow: Boolean) {
        // Find the sorting option menu item by its ID
        val view = findViewById<View>(R.id.action_sort) ?: return

        // Set the visibility of the sorting option menu item based on the provided flag
        view.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * Shows the sorting options popup menu when the sorting menu item is clicked.
     *
     * The sorting options include sorting the displayed data in ascending order, descending order,
     * or in a random order. This function inflates a popup menu with sorting options and listens
     * for user selections. When a sorting option is selected, it notifies the MainViewModel to
     * change the sorting type accordingly.
     */
    private fun showSortingPopupMenu() {
        // Find the anchor view for the popup menu
        val view = findViewById<View>(R.id.action_sort) ?: return

        // Create a new instance of PopupMenu with the current activity context and the anchor view
        PopupMenu(this, view).run {
            // Inflate the menu layout containing sorting options
            menuInflater.inflate(R.menu.sorting_menu, menu)

            // Set a listener to handle menu item clicks
            setOnMenuItemClickListener {
                // Determine which sorting option was clicked and notify the MainViewModel to change the sorting type
                mainViewModel.changeSortType(
                    when (it.itemId) {
                        R.id.action_ascending -> SortType.ASCENDING
                        R.id.action_descending -> SortType.DESCENDING
                        else -> SortType.RANDOM
                    }
                )
                true// Indicate that the click event has been handled
            }

            // Show the popup menu anchored to the anchor view
            show()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}