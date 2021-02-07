package com.unito.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unito.models.entity.Reservation;
import com.unito.repository.ReservationRepository;

import java.util.List;

public class ReservationViewModel extends AndroidViewModel {

    private ReservationRepository reservationRepo;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setRepo(ReservationRepository reservationRepo) {
        this.reservationRepo = reservationRepo;
    }

    /**
     * get a list of reservation using a username
     * @param username
     * @param reservationStatus
     * @return      a list of reservations of the user with the reservation status as called
     */
    public LiveData<List<Reservation>> getReservationByUser(String username, String reservationStatus) {
        return reservationRepo.getReservationByUser(username, reservationStatus);
    }

    /**
     * get a list of reservation using a tutor id
     * @param tutor_id
     * @return          a list of reservations belonging to the specified tutor
     */
    /*  */
    public LiveData<List<Reservation>> getReservationsByTutor(int tutor_id) {
        return reservationRepo.getReservationByTutor(tutor_id);
    }

    /**
     * Update a reservation
     * @param r   reservation which will be updated
     */
    public void updateReservation(Reservation r) {
        reservationRepo.update(r);
    }

    /**
     * Delete a reservation
     * @param r   reservation which will be deleted
     */
    public void deleteReservation(Reservation r) {
        reservationRepo.deleteReservation(r);
    }
}
