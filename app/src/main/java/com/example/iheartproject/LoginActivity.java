package com.example.iheartproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTbx;
    private EditText passwordTbx;
    private String email;
    private String password;
    private String TAG = "Login Activity";

    public void Login(String email, String password)
    {
        try
        {
            // Validate email and password
            if (email.length() <= 0|| password.length() <= 0)
            {
                Toast.makeText(LoginActivity.this, "Please enter your email and password.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // DATABASE
            // Connecting it to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference();

            // Connecting it to the firebase authentication database
            FirebaseAuth tmAuth = FirebaseAuth.getInstance();

            // Login with ur username and password
            tmAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                // Acquire the current logged in user
                                FirebaseUser user = tmAuth.getCurrentUser();

                                // We query to our realtime database, and acquire our additional info
                                myRef.child("users").child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                        if (!task2.isSuccessful()) {
                                            // If not successful, we stop the process
                                            Log.e("firebase", "Error getting data", task2.getException());
                                            Toast.makeText(LoginActivity.this, "Error occurred when connecting to firebase.",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else {
                                            // If successful, we parse the value
                                            Log.d("firebase", String.valueOf(task2.getResult().getValue()));

                                            List<User> userList = new ArrayList<>();
                                            User currentUser = null;
                                            // Basically, we get the child of the data one by one, parse and insert to our object list
                                            for (DataSnapshot ds : task2.getResult().getChildren()) {
                                                // Parse the whole row of firebase data into our object, and add into list
                                                User userObj = ds.getValue(User.class);
                                                userList.add(userObj);
                                            }

                                            // Loop again to match user
                                            for (User userObj : userList) {
                                                // Try to match if user info is same as firebase realtime
                                                if (user.getEmail().equals(userObj.Email)){
                                                    Log.d("firebase", "User email match");
                                                    currentUser = userObj;
                                                    break;
                                                }
                                            }

                                            // Check if user exist
                                            if (currentUser == null){
                                                Log.e("firebase", "Unable to find user info in firebase");
                                                Toast.makeText(LoginActivity.this, "Unable to fetch user info",
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            Toast.makeText(LoginActivity.this, "User authenticated. Welcome " + currentUser.FullName + ".",
                                                    Toast.LENGTH_SHORT).show();

                                            // TODO : Remember this, copy this and change the intent from and to activities
                                            // This section is for u to
                                            // Redirect user to main activity class
                                            Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);

                                            // We parsing user object into json, so that we can pass it to other activities
                                            Gson gson = new Gson();
                                            String userJson = gson.toJson(currentUser);

                                            // Assign ur information to new intent, with a key
                                            newIntent.putExtra("user", userJson);

                                            startActivity(newIntent);

                                            finish();
                                        }
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Incorrect email or password. Try again.",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.e(TAG, "signInWithEmail:failure" + ex.getMessage());
            Toast.makeText(LoginActivity.this, "Unexpected error occurred during login : " +ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Hide Action Bar
        getSupportActionBar().hide();

        Button forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        forgotPasswordButton.setPaintFlags(forgotPasswordButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                        startActivity(new Intent(LoginActivity.this,
                                ForgetPasswordActivity.class));
//                        finish();
                    }
                }
        );

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*// Close down the virtual keyboard
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);*/

                        // Do Login Details Verification Here
                        emailTbx = (EditText) findViewById(R.id.email);
                        passwordTbx = (EditText) findViewById(R.id.password);

                        email = emailTbx.getText().toString();
                        password = passwordTbx.getText().toString();

                        Login(email,password);

                    }
                }
        );

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        registerButton.setPaintFlags(registerButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                        startActivity(new Intent(LoginActivity.this,
                                SelectionTabActivity.class));
                        finish();
                    }
                }
        );
    }
}