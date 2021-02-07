package com.unito.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.unito.Provider;
import com.unito.models.dao.AssociationDao;
import com.unito.models.dao.ReservationDao;
import com.unito.models.entity.Association;
import com.unito.models.entity.Reservation;

import java.util.List;
import java.util.concurrent.Executor;

public class ReservationRepository {

    private ReservationDao reservationDao;
    private AssociationDao associationDao;
    private Executor exe;

    public ReservationRepository(ReservationDao reservationDao, Executor exe) {
        this.reservationDao = reservationDao;
        this.exe = exe;
    }

    /**
     * get list of reservations by user and reservation status
     *
     * @param username
     * @param reservationStatus
     * @return
     */
    public LiveData<List<Reservation>> getReservationByUser(String username, String reservationStatus) {
        return reservationDao.getReservationByUser(username, reservationStatus);
    }

    /**
     * get list of reservations by tutor id
     *
     * @param tutorid
     * @return
     */
    public LiveData<List<Reservation>> getReservationByTutor(int tutorid) {
        Provider p = Provider.getProvider(null);
        associationDao = p.ad;
        return reservationDao.getReservationByTutor(tutorid);
    }

    /**
     * update a reservation
     *
     * @param reservation
     */
    public void update(Reservation reservation) {
        exe.execute(() -> reservationDao.updateReservation(reservation));
    }


    /**
     * Delete a reservation.
     * Create a copy of the reservation and set his status as RESERVATION_STATUS_DELETE
     * then set the slot as free.
     *
     * @param r reservation to be deleted
     */
    public void deleteReservation(Reservation r) {
        Reservation flagDeleteReservation = new Reservation(r.getUsername()
                , r.getTutorID(), r.getTutorName(),
                r.getTutorSurname(), r.getCourse_name(), r.getSlot(),
                Reservation.RESERVATION_STATUS_DELETE);

        exe.execute(() -> reservationDao.insert(flagDeleteReservation));

        r.setCourse_name(null);
        r.setReservationStatus(Reservation.RESERVATION_STATUS_FREE);
        exe.execute(() -> reservationDao.updateReservation(r));

    }
}
