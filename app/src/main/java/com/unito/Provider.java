package com.unito;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.unito.models.dao.AssociationDao;
import com.unito.models.dao.ReservationDao;
import com.unito.models.dao.TutorDao;
import com.unito.models.dao.UserDao;
import com.unito.models.defaultdata.AssociationData;
import com.unito.models.defaultdata.CourseData;
import com.unito.models.defaultdata.ReservationsData;
import com.unito.models.defaultdata.TutorData;
import com.unito.models.defaultdata.UsersData;
import com.unito.models.MyDatabase;
import com.unito.repository.ReservationRepository;
import com.unito.repository.TutorRepository;
import com.unito.repository.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Provider {

    public MyDatabase db;
    public UserDao ud;
    public ReservationDao rd;
    public TutorDao td;

    public AssociationDao ad;

    public Executor exe;
    public static Application a;
    public UserRepository ur;
    public ReservationRepository rr;
    public TutorRepository tr;
    private static Provider instance;

    MyDatabase provideDatabase(Application application) {

        RoomDatabase.Callback populateCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase sqldb) {
                super.onCreate(sqldb);
                exe.execute(() -> {
                    db.userDao().insertAll(UsersData.populateUser());
                    db.tutorDao().insertAll(TutorData.populateTutor());
                    db.courseDao().insertAll(CourseData.populateCourse());
                    db.associationsDao().insertAll(AssociationData.populateAssociations());

                    db.reservationDao().insertAll(ReservationsData.populateReservations(
                            TutorData.populateTutor()));
                });
            }
        };

        return Room.databaseBuilder(application,
                MyDatabase.class, "MyDatabase.db")
                .addCallback(populateCallback).build();
    }

    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    UserDao provideUserDao(MyDatabase database) {
        return database.userDao();
    }
    ReservationDao provideReservationDao(MyDatabase database) {
        return database.reservationDao();
    }
    TutorDao provideTutorDao(MyDatabase database) {
        return database.tutorDao();
    }

    UserRepository provideUserRepository(UserDao userDao, Executor executor) {
        return new UserRepository(userDao, executor);
    }
    ReservationRepository provideReservationRepository(ReservationDao reservationDao, Executor executor) {
        return new ReservationRepository(reservationDao, executor);
    }
    TutorRepository provideTutorRepository(TutorDao tutorDao, Executor executor) {
        return new TutorRepository(tutorDao, executor);
    }

    AssociationDao provideAssociationDao(MyDatabase database) {
        return database.associationsDao();
    }

    public static Provider getProvider(Application a) {
        if (instance == null) {
            instance = new Provider();
            instance.a = a;
            instance.db = instance.provideDatabase(a);
            instance.exe =instance.provideExecutor();
            instance.ud = instance.provideUserDao(instance.db);
            instance.rd = instance.provideReservationDao(instance.db);
            instance.td = instance.provideTutorDao(instance.db);

            instance.ad = instance.provideAssociationDao(instance.db);

            instance.ur = instance.provideUserRepository(instance.ud, instance.exe);
            instance.rr = instance.provideReservationRepository(instance.rd, instance.exe);
            instance.tr = instance.provideTutorRepository(instance.td, instance.exe);
        }
        return instance;
    }





}
