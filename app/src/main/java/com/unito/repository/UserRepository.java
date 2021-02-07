package com.unito.repository;

import androidx.lifecycle.LiveData;

import com.unito.models.dao.UserDao;
import com.unito.models.entity.User;

import java.util.concurrent.Executor;

public class UserRepository {


    private final UserDao userDao;
    private final Executor exe;


    public UserRepository(UserDao userDao, Executor exe) {
        this.userDao = userDao;
        this.exe = exe;
    }

    /**
     * Get an user by username
     *
     * @param username
     * @return
     */
    public LiveData<User> getUser(String username) {
        return userDao.getUserByUsername(username);
    }


}
