package com.unito.models.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.unito.models.ObjectConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "associations")
@TypeConverters({ObjectConverter.class})
public class Association {

    @NonNull
    @PrimaryKey
    @Embedded
    private Tutor tutor;

    private List<Course> courses;

    public Association(@NotNull Tutor tutor, List<Course> courses) {
        this.tutor = tutor;
        this.courses = courses;
    }

    @Ignore
    public Association() {
    }

    @NotNull
    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(@NotNull Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getFormattedCourses() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            ret.append(c.getCourseName()).append(i == courses.size() - 1 ? "" : ", ");
        }
        return ret.toString();
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @NotNull
    @Override
    public String toString() {
        return "Associations{" +
                "tutor=" + tutor +
                ", courses=" + courses.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Association that = (Association) o;
        return tutor.equals(that.tutor) &&
                Objects.equals(courses, that.courses);
    }

}
