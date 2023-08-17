package com.vat.icare.virtualSigns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityHistoryBinding;
import com.vat.icare.databinding.ActivityVitalSignsResultsBinding;
import com.vat.icare.domin.VitalSignViewModel;
import com.vat.icare.login.LoginViewModel;
import com.vat.icare.utils.romDB.VitalSign;
import com.vat.icare.virtualSigns.adapter.HistryAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    Context context;
    ActivityHistoryBinding binding;
    VitalSignViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        context = this;

        viewModel =ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(VitalSignViewModel.class);
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getAllStudentsFromVm().observe(this, vitalSigns -> {
            if (vitalSigns != null && !vitalSigns.isEmpty()) {
                HistryAdapter adapter = new HistryAdapter((ArrayList<VitalSign>) vitalSigns);
                binding.recyclerHistory.setAdapter(adapter);
            }
        });
    }
}