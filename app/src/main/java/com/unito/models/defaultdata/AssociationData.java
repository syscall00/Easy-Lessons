package com.unito.models.defaultdata;

import com.unito.models.entity.Association;
import com.unito.models.entity.Course;
import com.unito.models.entity.Tutor;

import java.util.ArrayList;
import java.util.List;

public class AssociationData {
    public static Association[] populateAssociations() {

        List<Course> c1 = new ArrayList<>(), c2 = new ArrayList<>(),
                c3 = new ArrayList<>(), c4 = new ArrayList<>(), c5 = new ArrayList<>(),
                c6 = new ArrayList<>(), c7 = new ArrayList<>(),
                c8 = new ArrayList<>();
        c1.add(new Course(1,"Programmazione 1"));
        c2.add(new Course(2,"Programmazione 2"));
        c5.add(new Course(4,"Basi di dati"));
        c4.add(new Course(5,"Interazione Uomo-Macchina"));
        c6.add(new Course(6,"Algoritmi"));
        c7.add(new Course(7,"Matematica Discreta"));
        c8.add(new Course(8,"Tecnologie Web"));
        c3.add(new Course(9,"Probabilità e Statistica"));
        c2.add(new Course(10,"Linguaggi Formali e traduttori"));

        return new Association[]{
                new Association(new Tutor(1, "Luca", "Roversi"), c1),
                new Association(new Tutor(2, "Luca", "Padovani"),  c2),
                new Association(new Tutor(3, "Roberta", "Sirovich"), c3),
                new Association(new Tutor(4, "Marino", "Segnan"), c4),
                new Association(new Tutor(5, "Ruggero G.", "Pensa"), c5),
                new Association(new Tutor(6, "Ugo", "De Liguoro"), c6),
                new Association(new Tutor(7, "Andrea", "Mori"), c7),
                new Association(new Tutor(8, "Marco", "Botta"), c8)
        };
    }

}
