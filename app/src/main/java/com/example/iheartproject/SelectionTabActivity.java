package com.example.iheartproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SelectionTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_tab);

        // Hide Action Bar
        getSupportActionBar().hide();

        ImageButton healthCareButton = (ImageButton) findViewById(R.id.chooseHospital);
        healthCareButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Do Login Details Verification Here

                        startActivity(new Intent(SelectionTabActivity.this,
                                HospitalSignUpActivity.class));
                        finish();
                    }
                }
        );

        ImageButton donorButton = (ImageButton) findViewById(R.id.chooseDonor);
        donorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Do Login Details Verification Here

                        startActivity(new Intent(SelectionTabActivity.this,
                                UserSignUpActivity.class));
                        finish();
                    }
                }
        );
    }
}