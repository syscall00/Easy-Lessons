package com.unito.models.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.unito.models.entity.Course;

import java.util.List;

@Dao
public interface CourseDao {

    /**
     * Get all courses from course table
     * @return a livedata list of courses
     */
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    /**
     * Insert an array of courses in table
     * @param courses
     */
    @Insert
    void insertAll(Course[] courses);

}
