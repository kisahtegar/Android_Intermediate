package com.kisahcode.androidintermediate.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

/**
 * Data Access Object (DAO) for accessing student-related data in the database.
 * Provides methods to perform CRUD operations and retrieve data related to students.
 */
@Dao
interface StudentDao {

    /**
     * Inserts one or more students into the database.
     *
     * If a student with the same studentId already exists, the operation is ignored.
     *
     * @param student List of students to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: List<Student>)

    /**
     * Inserts one or more universities into the database.
     *
     * If a university with the same universityId already exists, the operation is ignored.
     *
     * @param university List of universities to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUniversity(university: List<University>)

    /**
     * Inserts one or more courses into the database.
     *
     * If a course with the same courseId already exists, the operation is ignored.
     *
     * @param course List of courses to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: List<Course>)

    /**
     * Inserts one or more course-student relationships into the database.
     *
     * If a relationship with the same studentId and courseId already exists, the operation is ignored.
     *
     * @param courseStudentCrossRef List of course-student relationships to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseStudentCrossRef(courseStudentCrossRef: List<CourseStudentCrossRef>)

    /**
     * Retrieves all students from the database using Paging 2.
     *
     * This function returns a `DataSource.Factory` that provides access to a paginated list of students.
     * The returned `DataSource` is responsible for loading data in chunks or pages, enabling efficient
     * loading of large datasets. Pagination allows for smoother scrolling and reduces memory consumption
     * by loading only a subset of data into memory at a time.
     *
     * @param query A SQLite query specifying custom selection criteria, sorting, or other database operations.
     *              This query is used by Paging to fetch the data from the database.
     * @return A `DataSource.Factory` that generates instances of `DataSource` responsible for loading paginated data.
     *         The `DataSource` loads data from the database according to the provided query and handles pagination.
     */
    @RawQuery(observedEntities = [Student::class])
    fun getAllStudent(query: SupportSQLiteQuery): DataSource.Factory<Int, Student>

    /**
     * Retrieves all students with their associated universities from the database.
     *
     * @return LiveData containing a list of StudentAndUniversity objects,
     *         each representing a student and their associated university.
     */
    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>>

    /**
     * Retrieves all universities with their associated students from the database.
     *
     * @return LiveData containing a list of UniversityAndStudent objects,
     *         each representing a university and its associated students.
     */
    @Transaction
    @Query("SELECT * from university")
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>>

    /**
     * Retrieves all students with the courses they are enrolled in from the database.
     *
     * @return LiveData containing a list of StudentWithCourse objects,
     *         each representing a student and the courses they are enrolled in.
     */
    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>>
}