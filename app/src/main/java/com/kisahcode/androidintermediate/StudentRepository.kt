package com.kisahcode.androidintermediate

import androidx.lifecycle.LiveData
import com.kisahcode.androidintermediate.database.*
import com.kisahcode.androidintermediate.helper.InitialDataSource

/**
 * Repository class responsible for managing data operations between the ViewModel and the underlying data source.
 *
 * @property studentDao Data Access Object (DAO) for accessing the student-related data.
 */
class StudentRepository(private val studentDao: StudentDao) {

    /**
     * Retrieves all students from the database.
     *
     * @return LiveData object containing a list of students.
     */
    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()

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

    /**
     * Inserts initial data into the database.
     *
     * This includes students, universities, courses, and their associations.
     */
    suspend fun insertAllData() {
        studentDao.insertStudent(InitialDataSource.getStudents())
        studentDao.insertUniversity(InitialDataSource.getUniversities())
        studentDao.insertCourse(InitialDataSource.getCourses())
        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())
    }
}