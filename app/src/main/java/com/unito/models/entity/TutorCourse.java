package com.unito.models.entity;


import androidx.room.Embedded;

public class TutorCourse {
    @Embedded
    private Tutor tutor;
    @Embedded
    private Course course;

    public TutorCourse(Tutor tutor, Course course) {
        this.tutor = tutor;
        this.course = course;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "TutorCourse{" +
                "tutor=" + tutor +
                ", course=" + course +
                '}';
    }
}
