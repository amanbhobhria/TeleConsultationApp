package com.iisc.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iisc.R;

public class PhyziHealth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phyzihealth);
        LinearLayout bmi = (LinearLayout) findViewById(R.id.bmiLayout);
        LinearLayout step = (LinearLayout) findViewById(R.id.stepLayout);
        LinearLayout bp = (LinearLayout) findViewById(R.id.bpLayout);
        LinearLayout women = (LinearLayout) findViewById(R.id.whLayout);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhyziHealth.this, PhyziHealth_bmiActivity.class));
            }
        });

        step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhyziHealth.this, PhyziHealth_StepCounter.class));
            }
        });


        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhyziHealth.this, PhyziHealth_BloodPressure_View.class));
            }
        });

        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PhyziHealth.this,"Commented in PhyzHealth.java", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(PhyziHealth.this, WHealth.class));
            }
        });


    }
}
