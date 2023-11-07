package com.vat.icare.medicineAlerts;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityMedicineAlertBinding;
import com.vat.icare.medicineAlerts.adapter.MedicineAdapter;

import java.util.ArrayList;

public class MedicineAlert extends AppCompatActivity {

    ArrayList<Model> dataholder = new ArrayList<Model>();
    //Array list to add reminders and display in recyclerview
    MedicineAdapter adapter;
    ActivityMedicineAlertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medicine_alert);
        binding.txtViewNoData.setVisibility(View.VISIBLE);

        binding.mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //Floating action button to change activity
        binding.mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
                startActivity(intent);
            }
        });

        Cursor cursor = new dbManager(getApplicationContext()).readallreminders();
        //Cursor To Load data From the database
        while (cursor.moveToNext()) {
            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataholder.add(model);
        }
        if (!dataholder.isEmpty()) {
            binding.txtViewNoData.setVisibility(View.GONE);
        }

        adapter = new MedicineAdapter(dataholder);
        binding.mRecyclerview.setAdapter(adapter);
    }
}