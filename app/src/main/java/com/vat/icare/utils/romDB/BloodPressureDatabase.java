package com.vat.icare.utils.romDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {VitalSign.class}, version = 1, exportSchema = false)
public abstract class BloodPressureDatabase extends RoomDatabase {

    public abstract BloodPressureDoa studentDao();

    private static volatile BloodPressureDatabase bloodPressureDatabase;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static BloodPressureDatabase getDatabase(final Context context) {
        if (bloodPressureDatabase == null) {
            synchronized (BloodPressureDatabase.class) {
                if (bloodPressureDatabase == null) {
                    bloodPressureDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    BloodPressureDatabase.class, "vitalSigns_database")
                            .build();
                }
            }
        }
        return bloodPressureDatabase;
    }
}
