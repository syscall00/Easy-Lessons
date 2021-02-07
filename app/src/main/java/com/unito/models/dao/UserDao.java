package com.unito.models.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.unito.models.entity.User;

import java.util.List;

@Dao
public interface UserDao {


    /**
     * Get all users from user table
     * @return a livedata list of users
     */
    @Query("SELECT * FROM users")
    List<User> getAll();

    /**
     * Get an user by username
     * @param username
     * @return
     */
    @Query("SELECT * FROM users WHERE username = :username")
    LiveData<User> getUserByUsername(String username);


    /**
     * Insert an array of users in table
     * @param users
     */
    @Insert
    void insertAll(User[] users);

}
