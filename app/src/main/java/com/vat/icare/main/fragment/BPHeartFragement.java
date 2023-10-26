package com.vat.icare.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vat.icare.R;
import com.vat.icare.databinding.FragmentBPHeartFragementBinding;
import com.vat.icare.databinding.FragmentDoctorBinding;
import com.vat.icare.virtualSigns.VitalSignsProcess;

public class BPHeartFragement extends Fragment {

    FragmentBPHeartFragementBinding binding;
    public BPHeartFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBPHeartFragementBinding.inflate(inflater, container, false);
        binding.StartVS.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), VitalSignsProcess.class);
            startActivity(i);
        });
        return binding.getRoot();
    }
}