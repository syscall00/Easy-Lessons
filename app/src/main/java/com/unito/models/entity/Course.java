package com.unito.models.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "course_name")
    private String courseName;

    @Ignore
    public Course(String courseName) {
        this.courseName = courseName;
    }
    public Course(int id, String courseName) {
        this.courseName = courseName;
    }

    @Ignore
    public Course() {
    }
    public int getId() { return id;   }
    public String getCourseName() {
        return courseName;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Course{" +
                "ID=" + id +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
