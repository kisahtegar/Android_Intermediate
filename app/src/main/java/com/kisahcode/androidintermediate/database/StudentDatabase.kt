package com.kisahcode.androidintermediate.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import kotlinx.coroutines.CoroutineScope

/**
 * StudentDatabase serves as the main access point for the underlying Room database.
 * It provides methods to retrieve DAOs and access the data stored in the database.
 *
 * @property studentDao DAO interface for accessing student-related data.
 */
@Database(
    entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = StudentDatabase.MyAutoMigration::class),
    ],
    exportSchema = true
)
abstract class StudentDatabase : RoomDatabase() {

    /**
     * Custom auto migration specification class for renaming the 'graduate' column to 'isGraduate'.
     */
    @RenameColumn(tableName = "Student", fromColumnName = "graduate", toColumnName = "isGraduate")
    class MyAutoMigration : AutoMigrationSpec

    /**
     * Retrieves the DAO interface for accessing student-related data.
     *
     * @return StudentDao object for accessing database operations related to students.
     */
    abstract fun studentDao(): StudentDao

    companion object {
        // Volatile ensures that INSTANCE variable is always up-to-date and visible to all threads.
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        /**
         * Retrieves the singleton instance of the StudentDatabase.
         *
         * If the instance does not exist, it creates a new one.
         *
         * @param context Application context.
         * @return Singleton instance of the StudentDatabase.
         */
        @JvmStatic
        fun getDatabase(context: Context, applicationScope: CoroutineScope): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration(false)

                        // Uncomment the line below if you want to use a pre-populated database from assets
                        .createFromAsset("student_database.db")

                        // Uncomment the block below if you want to use a pre-populated database with addCallback
//                        .addCallback(object :Callback(){
//                            override fun onCreate(db: SupportSQLiteDatabase) {
//                                super.onCreate(db)
//                                INSTANCE?.let { database ->
//                                    applicationScope.launch {
//                                        val studentDao = database.studentDao()
//                                        studentDao.insertStudent(InitialDataSource.getStudents())
//                                        studentDao.insertUniversity(InitialDataSource.getUniversities())
//                                        studentDao.insertCourse(InitialDataSource.getCourses())
//                                        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())                                }
//                                }
//                            }
//                        })
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}