package com.unito.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.unito.models.dao.AssociationDao;
import com.unito.models.dao.CourseDao;
import com.unito.models.dao.ReservationDao;
import com.unito.models.dao.TutorDao;
import com.unito.models.dao.UserDao;
import com.unito.models.entity.Association;
import com.unito.models.entity.Course;
import com.unito.models.entity.Tutor;
import com.unito.models.entity.User;
import com.unito.models.entity.Reservation;
@TypeConverters(ObjectConverter.class)
@Database(entities = {User.class, Tutor.class, Course.class, Reservation.class, Association.class}, version = 1, exportSchema = false)

public abstract class MyDatabase extends RoomDatabase {


    public abstract UserDao userDao();
    public abstract TutorDao tutorDao();
    public abstract CourseDao courseDao();
    public abstract ReservationDao reservationDao();
    public abstract AssociationDao associationsDao();
}
