package com.vat.icare.utils.romDB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class VitalSignRepository {

    BloodPressureDatabase studentRoomDatabase;
    BloodPressureDoa studentDao;
    private LiveData<List<VitalSign>> listStudents;

    public VitalSignRepository(Application application) {
        studentRoomDatabase = BloodPressureDatabase.getDatabase(application);
        studentDao = studentRoomDatabase.studentDao();
        listStudents = studentDao.getStudent();
    }

    public void insertStudent(VitalSign student) {
        BloodPressureDatabase.databaseWriteExecutor.execute(() -> studentDao.insert(student));
    }

    public LiveData<List<VitalSign>> getAllStudents() {
        return listStudents;
    }
}
