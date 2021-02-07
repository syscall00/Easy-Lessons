package com.unito.models.defaultdata;

import com.unito.models.entity.Reservation;
import com.unito.models.entity.Tutor;

public class ReservationsData {

    public static Reservation[] populateReservations(Tutor[] tutors) {
        int i = 0;
        Reservation[] reservations = new Reservation[20*tutors.length];
        for (Tutor tutor : tutors) {
            for (int j = 0; j < 20; j++) {
                reservations[i++] = new Reservation(null, tutor.getID(), tutor.getName(), tutor.getSurname(),
                        null, j, "free");
            }

        }
        return reservations;
    }
}
