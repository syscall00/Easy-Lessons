package com.unito.models.defaultdata;

import com.unito.models.entity.Course;

public class CourseData {
    public static Course[] populateCourse() {

        return new Course[]{
                new Course("Programmazione 1"),
                new Course("Programmazione 2"),
                new Course("Programmazione 3"),
                new Course("Basi di dati"),
                new Course("Interazione Uomo-Macchina"),
                new Course("Algoritmi"),
                new Course("Matematica Discreta"),
                new Course("Tecnologie Web"),
                new Course("Probabilità e Statistica"),
                new Course("Linguaggi Formali e traduttori")
        };
    }
}

