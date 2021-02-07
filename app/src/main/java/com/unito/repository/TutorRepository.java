package com.unito.repository;

import androidx.lifecycle.LiveData;

import com.unito.models.entity.Association;
import com.unito.models.dao.TutorDao;
import com.unito.models.entity.Tutor;

import java.util.List;
import java.util.concurrent.Executor;

public class TutorRepository {

    private final TutorDao tutorDao;
    private final Executor exe;

    public TutorRepository(TutorDao tutorDao, Executor exe) {
        this.tutorDao = tutorDao;
        this.exe = exe;
    }


    /**
     * Get an association by tutor
     *
     * @param tutor_id
     * @return association requested
     */
    public LiveData<Association> getAssociationByTutor(int tutor_id) {
        return tutorDao.getAssociationByTutor(tutor_id);
    }

    /**
     * Get the list of associations tutor-course
     *
     * @return list of associations of all courses
     */
    public LiveData<List<Association>> getAssociations() {
        return tutorDao.getAssociationsList();
    }


}
