package com.unito.models.defaultdata;

import com.unito.models.entity.Tutor;

public class TutorData {
    public static Tutor[] populateTutor() {
        return new Tutor[]  {
                new Tutor(1,"Luca","Roversi"),
                new Tutor(2,"Luca", "Padovani" ),
                new Tutor(3,"Roberta","Sirovich" ),
                new Tutor(4,"Marino", "Segnan"),
                new Tutor(5,"Ruggero G.", "Pensa"),
                new Tutor(6,"Ugo", "De Liguoro"),
                new Tutor(7,"Andrea", "Mori"),
                new Tutor(8,"Marco", "Botta")
        };
    }
}
