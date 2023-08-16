package com.vat.icare.virtualSigns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import com.vat.icare.R;
import com.vat.icare.databinding.ActivityHistoryBinding;
import com.vat.icare.databinding.ActivityVitalSignsResultsBinding;
import com.vat.icare.login.LoginViewModel;

public class HistoryActivity extends AppCompatActivity {

    Context context;
    ActivityHistoryBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        context=this;
    }
}