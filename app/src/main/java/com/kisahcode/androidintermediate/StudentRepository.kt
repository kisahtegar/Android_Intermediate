package com.kisahcode.androidintermediate

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.kisahcode.androidintermediate.database.*
import com.kisahcode.androidintermediate.helper.SortType
import com.kisahcode.androidintermediate.helper.SortUtils

/**
 * Repository class responsible for managing data operations between the ViewModel and the underlying data source.
 *
 * @property studentDao Data Access Object (DAO) for accessing the student-related data.
 */
class StudentRepository(private val studentDao: StudentDao) {

    /**
     * Retrieves all students from the database using Paging 2, optionally sorted based on the specified sorting type.
     *
     * This function returns a `LiveData` object containing a `PagedList` of students, where the data is loaded
     * in a paginated manner using Paging 2. The list of students can be optionally sorted based on the specified
     * sorting type before being paginated.
     *
     * @param sortType The type of sorting to be applied to the student list, if any.
     * @return LiveData object containing a `PagedList` of students, possibly sorted based on the sortType,
     *         and loaded in a paginated manner using Paging 2.
     */
    @Suppress("DEPRECATION")
    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>> {
        // Generate a sorted SQL query based on the specified sortType.
        val query = SortUtils.getSortedQuery(sortType)

        // Retrieve a `DataSource.Factory` that provides access to a paginated list of students from the DAO.
        val student = studentDao.getAllStudent(query)

        // Configure the Paging 2 library with parameters for data loading and pagination.
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true) // Enable placeholders to handle null items in the list
            .setInitialLoadSizeHint(30) // Hint for the number of items to load initially
            .setPageSize(10) // Number of items loaded per page
            .build()

        // Create a `LiveData` object containing a `PagedList` of students using the configured
        // `PagedList.Config` and the `DataSource.Factory` for loading data from the DAO.
        return LivePagedListBuilder(student, config).build()
    }

    /**
     * Retrieves all students with their associated universities from the database.
     *
     * @return LiveData object containing a list of StudentAndUniversity objects.
     */
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()

    /**
     * Retrieves all universities with their associated students from the database.
     *
     * @return LiveData object containing a list of UniversityAndStudent objects.
     */
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentDao.getAllUniversityAndStudent()

    /**
     * Retrieves all students with their associated courses from the database.
     *
     * @return LiveData object containing a list of StudentWithCourse objects.
     */
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentDao.getAllStudentWithCourse()

}