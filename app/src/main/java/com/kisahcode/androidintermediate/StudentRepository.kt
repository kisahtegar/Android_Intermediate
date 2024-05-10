package com.kisahcode.androidintermediate

import androidx.lifecycle.LiveData
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
     * Retrieves all students from the database, optionally sorted based on the specified sorting type.
     *
     * @param sortType The type of sorting to be applied to the student list.
     * @return LiveData object containing a list of students, possibly sorted based on the sortType.
     */
    fun getAllStudent(sortType: SortType): LiveData<List<Student>> {
        // Generate a sorted SQL query based on the specified sortType.
        val query = SortUtils.getSortedQuery(sortType)

        // Retrieve a LiveData object containing a list of students from the DAO, possibly sorted.
        return studentDao.getAllStudent(query)
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