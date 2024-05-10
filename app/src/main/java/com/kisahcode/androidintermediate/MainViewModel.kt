package com.kisahcode.androidintermediate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap
import com.kisahcode.androidintermediate.database.Student
import com.kisahcode.androidintermediate.database.StudentAndUniversity
import com.kisahcode.androidintermediate.database.StudentWithCourse
import com.kisahcode.androidintermediate.database.UniversityAndStudent
import com.kisahcode.androidintermediate.helper.SortType

/**
 * ViewModel class responsible for handling the presentation logic and managing data for the MainActivity.
 *
 * @property studentRepository Repository class for accessing student-related data.
 */
class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    private val _sort = MutableLiveData<SortType>()

    init {
        // Initialize the sorting type to ASCENDING by default.
        _sort.value = SortType.ASCENDING
    }

    /**
     * Changes the sorting type for the list of students.
     *
     * @param sortType The new sorting type to be applied.
     */
    fun changeSortType(sortType: SortType) {
        _sort.value = sortType
    }

    /**
     * Retrieves all students from the database, possibly sorted based on the current sorting type.
     *
     * @return LiveData object containing a list of students, possibly sorted.
     */
    fun getAllStudent(): LiveData<List<Student>> = _sort.switchMap {
        studentRepository.getAllStudent(it)
    }

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