package com.kisahcode.androidintermediate.database

import androidx.room.*

/**
 * Entity representing a student in the database.
 *
 * Each student has a unique studentId, a name, and is associated with a university.
 * Additionally, it includes information about whether the student is a graduate or not.
 *
 * @property studentId Unique identifier for the student.
 * @property name Name of the student.
 * @property univId Identifier of the university the student is associated with.
 * @property isGraduate Flag indicating whether the student is a graduate or not. Defaults to false.
 */
@Entity
data class Student(
    @PrimaryKey
    val studentId: Int,
    val name: String,
    val univId: Int,
    @ColumnInfo(defaultValue = "false")
    val isGraduate: Boolean? = false
)

/**
 * Entity representing a university in the database.
 *
 * Each university has a unique universityId and a name.
 *
 * @property universityId Unique identifier for the university.
 * @property name Name of the university.
 */
@Entity
data class University(
    @PrimaryKey
    val universityId: Int,
    val name: String,
)

/**
 * Entity representing a course in the database.
 *
 * Each course has a unique courseId and a name.
 *
 * @property courseId Unique identifier for the course.
 * @property name Name of the course.
 */
@Entity
data class Course(
    @PrimaryKey
    val courseId: Int,
    val name: String,
)

/**
 * Represents a combination of a student and their associated university.
 *
 * This is a one-to-one relationship between Student and University entities.
 *
 * @property student The student entity.
 * @property university The university entity associated with the student.
 */
data class StudentAndUniversity(
    @Embedded
    val student: Student,

    @Relation(
        parentColumn = "univId",
        entityColumn = "universityId"
    )
    val university: University? = null
)

/**
 * Represents a combination of a university and its associated students.
 *
 * This is a one-to-many relationship between University and Student entities.
 *
 * @property university The university entity.
 * @property student List of student entities associated with the university.
 */
data class UniversityAndStudent(
    @Embedded
    val university: University,

    @Relation(
        parentColumn = "universityId",
        entityColumn = "univId"
    )
    val student: List<Student>
)

/**
 * Entity representing the many-to-many relationship between students and courses.
 *
 * Each record represents a student enrolled in a course.
 *
 * @property sId Identifier of the student.
 * @property cId Identifier of the course.
 */
@Entity(primaryKeys = ["sId", "cId"])
data class CourseStudentCrossRef(
    val sId: Int,
    @ColumnInfo(index = true)
    val cId: Int,
)

/**
 * Represents a combination of a student and the courses they are enrolled in.
 *
 * This is a many-to-many relationship between Student and Course entities.
 *
 * @property studentAndUniversity The student and university combination.
 * @property course List of courses in which the student is enrolled.
 */
data class StudentWithCourse(
    @Embedded
    val studentAndUniversity: StudentAndUniversity,

    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)