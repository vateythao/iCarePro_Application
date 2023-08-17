package com.vat.icare.domin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vat.icare.utils.romDB.VitalSign;
import com.vat.icare.utils.romDB.VitalSignRepository;

import java.util.List;

public class VitalSignViewModel extends AndroidViewModel {
    private VitalSignRepository vitalSignRepository;
    private final LiveData<List<VitalSign>> listLiveData;

    public VitalSignViewModel(Application application) {
        super(application);
        vitalSignRepository = new VitalSignRepository(application);
        listLiveData = vitalSignRepository.getAllStudents();
    }

    public LiveData<List<VitalSign>> getAllStudentsFromVm() {
        return listLiveData;
    }

    public void insertStudent(VitalSign student) {
        vitalSignRepository.insertStudent(student);
    }
}
