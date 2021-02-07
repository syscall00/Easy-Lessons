package com.unito.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.unito.models.entity.Reservation;

import java.util.List;

@Dao
public interface ReservationDao {

    /**
     * Get all tutors from tutor table
     *
     * @return a livedata list of reservations
     */
    @Query("SELECT * FROM reservations")
    LiveData<List<Reservation>> getAll();


    /**
     * get list of reservations by user and reservation status
     *
     * @param username
     * @param reservation_status
     * @return
     */
    @Query("SELECT * FROM reservations WHERE username = :username AND reservation_status = :reservation_status" +
            " ORDER BY slot ASC")
    LiveData<List<Reservation>> getReservationByUser(String username, String reservation_status);


    /**
     * get list of reservations by tutor id
     *
     * @param tutorId
     * @return
     */
    @Query("SELECT * FROM reservations WHERE tutor_id = :tutorId")
    LiveData<List<Reservation>> getReservationByTutor(int tutorId);


    /**
     * update a reservation
     *
     * @param r
     */
    @Update
    void updateReservation(Reservation r);


    /**
     * insert a new reservation
     *
     * @param reservation
     */
    @Insert
    void insert(Reservation reservation);


    /**
     * Insert an array of reservations in table
     * @param reservations
     */
    @Insert
    void insertAll(Reservation... reservations);

}