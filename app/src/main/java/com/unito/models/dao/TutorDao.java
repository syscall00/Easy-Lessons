package com.unito.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.unito.models.entity.Association;
import com.unito.models.entity.Tutor;

import java.util.List;

@Dao
public interface TutorDao {


    /**
     * Get the list of associations tutor-course
     * @return  list of associations of all courses
     */
    @Query("SELECT * FROM associations ORDER BY ID")
    LiveData<List<Association>> getAssociationsList();

    /**
     * Get an association by tutor
     * @param tutor_id
     * @return  association requested
     */
    @Query("SELECT * FROM associations WHERE ID = :tutor_id")
    LiveData<Association> getAssociationByTutor(int tutor_id);

    /**
     * Insert an array of tutors in table
     * @param tutors
     */
    @Insert
    void insertAll(Tutor[] tutors);



}
