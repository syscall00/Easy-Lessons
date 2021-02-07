package com.unito.models.defaultdata;

import com.unito.models.entity.User;

public class UsersData {
    public static User[] populateUser() {
        return new User[]  {
                new User("user1", "password1"),
                new User("user2", "password2"),
                new User("user3", "password3")
        };
    }
}
