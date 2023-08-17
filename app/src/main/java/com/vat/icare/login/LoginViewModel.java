package com.vat.icare.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.vat.icare.utils.romDB.VitalSign;
import com.vat.icare.utils.romDB.VitalSignRepository;

import java.util.List;

/**
 * Created by Dell on 20,July,2023
 */
public class LoginViewModel extends AndroidViewModel {

    private VitalSignRepository studentRepository;
    private final LiveData<List<VitalSign>> listLiveData;

    public LoginViewModel(Application application) {
        super(application);
        studentRepository = new VitalSignRepository(application);
        listLiveData = studentRepository.getAllStudents();
    }

    public LiveData<List<VitalSign>> getAllVitalFromVm() {
        return listLiveData;
    }

    public void insertVital(VitalSign student) {
        studentRepository.insertStudent(student);
    }

}
