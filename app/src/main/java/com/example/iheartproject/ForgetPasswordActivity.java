package com.example.iheartproject;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Button forgotPasswordButton = (Button) findViewById(R.id.send_reset_password_button);
        forgotPasswordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText passwordResetEmail = findViewById(R.id.password_reset_email);
                        String email = passwordResetEmail.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(ForgetPasswordActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                                        finish();

                                    } else {
                                        Toast.makeText(ForgetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }

                    }
                }
        );
    }
}