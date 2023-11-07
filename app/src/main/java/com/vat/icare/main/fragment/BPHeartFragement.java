package com.vat.icare.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vat.icare.R;
import com.vat.icare.databinding.FragmentBPHeartFragementBinding;
import com.vat.icare.databinding.FragmentDoctorBinding;
import com.vat.icare.domin.VitalSignViewModel;
import com.vat.icare.medicineAlerts.AddMedicineActivity;
import com.vat.icare.medicineAlerts.MedicineAlert;
import com.vat.icare.utils.romDB.VitalSign;
import com.vat.icare.virtualSigns.VitalSignsProcess;
import com.vat.icare.virtualSigns.adapter.HistryAdapter;

import java.util.ArrayList;

public class BPHeartFragement extends Fragment {
    VitalSignViewModel viewModel;

    FragmentBPHeartFragementBinding binding;
    public BPHeartFragement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBPHeartFragementBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(VitalSignViewModel.class);
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel.getAllStudentsFromVm().observe(getViewLifecycleOwner(), vitalSigns -> {
            if (vitalSigns != null && !vitalSigns.isEmpty()) {
                HistryAdapter adapter = new HistryAdapter((ArrayList<VitalSign>) vitalSigns);
                binding.recyclerHistory.setAdapter(adapter);
            }
        });

        binding.newMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VitalSignsProcess.class);
                startActivity(i);
            }
        });
        binding.newReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MedicineAlert.class);
                startActivity(i);
            }
        });
        return binding.getRoot();
    }
}