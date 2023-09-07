package com.vat.icare.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.vat.icare.R;
import com.vat.icare.databinding.FragmentDoctorBinding;
import com.vat.icare.main.adapter.DoctorAdapter;

public class DoctorFragment extends Fragment {

    FragmentDoctorBinding binding;
    DoctorAdapter doctorAdapter;

    public DoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentDoctorBinding.inflate(inflater,container,false);
        doctorAdapter = new DoctorAdapter();

        binding.recyclerViewDoctor.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.recyclerViewDoctor.setAdapter(doctorAdapter);
        return binding.getRoot();
    }
}