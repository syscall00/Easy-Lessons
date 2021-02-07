package com.unito.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.unito.models.entity.Association;
import com.unito.models.entity.Tutor;
import com.unito.repository.TutorRepository;

import java.util.List;

public class TutorViewModel extends AndroidViewModel {

    private TutorRepository tutorRepo;
    private Tutor tutor;


    public TutorViewModel(Application application) {
        super(application);
    }
    public void setRepo(TutorRepository tutorRepo) {
        this.tutorRepo = tutorRepo;
    }


    public Tutor getTutor() {
        return tutor;
    }
    public void setTutor(Tutor tutor) {this.tutor = tutor;}



    /**
     * get a specific association using a tutor identifier
     * @param tutor_id
     * @return      association object with the specified id
     */
    public LiveData<Association> getAssociationByTutor(int tutor_id) {
        return tutorRepo.getAssociationByTutor(tutor_id);
    }


    /**
     * get all tutor-course associations
     * @return  a list of all associations saved
     */
    public LiveData<List<Association>> getAssociations() {
        return tutorRepo.getAssociations();
    }

}
