package com.unito.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unito.models.entity.User;
import com.unito.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepo;
    private String loggedUser;

    public UserViewModel(Application application) {
        super(application);
    }
    public void setRepo(UserRepository userRepo) {
        this.userRepo = userRepo;
    }


    /**
     * get logged user saved as static
     * @return      logged user
     */
    public String getLoggedUser() { return loggedUser; }


    /**
     * set logged user
     * @param user  username which will be saved
     */
    public void setLoggedUser(String user) {loggedUser = user; }


    /**
     * get a specific user object using an username
     * @param username
     * @return      the user object
     */
    public LiveData<User> login (String username) {
        return userRepo.getUser(username);
    }


}
