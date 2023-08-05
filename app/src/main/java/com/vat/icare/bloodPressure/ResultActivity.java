package com.vat.icare.bloodPressure;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vat.icare.R;
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView name = findViewById(R.id.name);
        TextView score = findViewById(R.id.scoreText);
        TextView normal = findViewById(R.id.normal);

        Intent intent = getIntent();
        if (intent != null) {
            name.setText(intent.getStringExtra("name"));
            if (intent.getStringExtra("name").equals("Blood Pressure")) {
                score.setText(intent.getStringExtra("score"));
            } else {
                score.setText(intent.getIntExtra("score", 0) != -1 ? Integer.toString(intent.getIntExtra("score", 0)) : "Insufficient data");
            }
            normal.setText("Normal range\n" + intent.getStringExtra("normal"));

        }
    }
}