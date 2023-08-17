package com.vat.icare.utils.romDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BloodPressureDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VitalSign student);

    @Update
    void update(VitalSign student);

    @Query("SELECT * from vitalSigns_table ORDER By id Asc")
    LiveData<List<VitalSign>> getStudent();

    @Query("DELETE from vitalSigns_table")
    void deleteAll();
}
