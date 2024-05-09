package com.kisahcode.androidintermediate

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kisahcode.androidintermediate.database.Student
import com.kisahcode.androidintermediate.database.StudentAndUniversity
import com.kisahcode.androidintermediate.database.StudentWithCourse
import com.kisahcode.androidintermediate.database.UniversityAndStudent
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for handling the presentation logic and managing data for the MainActivity.
 *
 * @property studentRepository Repository class for accessing student-related data.
 */
class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    /**
     * Initializes the ViewModel by inserting initial data into the database.
     */
    init {
        insertAllData()
    }

    /**
     * Retrieves all students from the database.
     *
     * @return LiveData object containing a list of students.
     */
    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()

    /**
     * Retrieves all students with their associated universities from the database.
     *
     * @return LiveData object containing a list of StudentAndUniversity objects.
     */
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentRepository.getAllStudentAndUniversity()

    /**
     * Retrieves all universities with their associated students from the database.
     *
     * @return LiveData object containing a list of UniversityAndStudent objects.
     */
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentRepository.getAllUniversityAndStudent()

    /**
     * Retrieves all students with their associated courses from the database.
     *
     * @return LiveData object containing a list of StudentWithCourse objects.
     */
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentRepository.getAllStudentWithCourse()

    /**
     * Inserts initial data into the database asynchronously.
     */
    private fun insertAllData() = viewModelScope.launch {
        studentRepository.insertAllData()
    }
}

/**
 * ViewModelProvider.Factory implementation for creating MainViewModel instances with a provided StudentRepository.
 *
 * @property repository StudentRepository instance to be injected into the MainViewModel.
 */
class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}