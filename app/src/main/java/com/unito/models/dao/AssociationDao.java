package com.unito.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.unito.models.entity.Association;
import com.unito.models.entity.TutorCourse;

import java.util.List;

@Dao
public interface AssociationDao {


    /**
     * Get all associations from association table
     *
     * @return a livedata list of associations
     */
    @Query("SELECT * FROM associations")
    LiveData<List<Association>> getAll();


    /**
     * Insert an array of associations in table
     *
     * @param associations
     */
    @Insert
    void insertAll(Association[] associations);


}
