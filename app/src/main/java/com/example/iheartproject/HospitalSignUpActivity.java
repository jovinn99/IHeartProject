package com.example.iheartproject;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HospitalSignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText hospitalNameTbx;

    private EditText hospitalUserNameTbx;
    private EditText emailTbx;
    private EditText passwordTbx;
    private EditText confirmPasswordTbx;
    private String hospitalUserName;
    private String email;
    private String password;
    private String confirmPassword;
    private String hospitalName;
    String TAG = "HospitalSignUpActivity";

    // Can copy straight
    private void Login(String email, String password, String confirmPassword, String hospitalName, String hospitalUserName, DatabaseReference myRef){
        try
        {
            // Validate the user information
            // Check if value is empty
            if (email == null || password == null || confirmPassword == null || hospitalName == null || hospitalUserName == null) {
                Toast.makeText(HospitalSignUpActivity.this, "Please key in user info completely.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if user key in identical password
            if (!password.equals(confirmPassword)){
                Toast.makeText(HospitalSignUpActivity.this, "Confirm password is not identical to password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Get authentication info
            mAuth = FirebaseAuth.getInstance();

            // Create user
            // NOTE: Firebase are running in asynchronous way, so the firebase auth might run the task behind
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        // Determine if user creation success or not
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                // Firebase only allow display name and picture, so we add a display name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(hospitalName)
                                        .build();

                                // TODO: Since we have user object, i think name is not necessary for now
                                // Issue regarding this, user profile wont update
//                                // Update your user info
//                                user.updateProfile(profileUpdates)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task1) {
//                                                if (task1.isSuccessful()) {
//                                                    Log.d(TAG, "User profile updated.");
//                                                }
//                                                else{
//                                                    Log.d(TAG, "User profile fail to update.");
//                                                    Toast.makeText(HospitalSignUpActivity.this, "User profile fail to update.",
//                                                            Toast.LENGTH_SHORT).show();
//                                                    return;
//                                                }
//                                            }
//                                        });

                                // Create an user object (since user not hospital i set to false, and hospital name to null)
                                User userObj = new User(user.getUid(), email, hospitalName , hospitalUserName, null, true);

                                // TODO: Why do this?
                                // Firebase auth only can set email, photo, username
                                // If u want more, u best to do a link from ur auth to firebase realtime
                                // We don't put password in for security reasons
                                // if better, u can link only the id (to prevent hacker know ur email)
                                DatabaseReference newRef = myRef.child("users").child("test").push();
                                newRef.setValue(userObj);

                                Log.w(TAG, "User created : " + user.getEmail());
                                Toast.makeText(HospitalSignUpActivity.this, "User successfully created.",
                                        Toast.LENGTH_SHORT).show();

                                // Go back to login after u created user
                                startActivity(new Intent(HospitalSignUpActivity.this,
                                        LoginActivity.class));
                                finish();
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                String errorText = task.getException().getMessage();
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView(),
                                        "Sign Up Failed : " + errorText, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("exception",e.getMessage());
                }
            });
        }
        catch (Exception ex)
        {
            Log.w(TAG, "createUserWithEmail:failure", ex);
            Toast.makeText(HospitalSignUpActivity.this, "Exception error occurred",
                    Toast.LENGTH_SHORT).show();
            return;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_sign_up);

        // Hide Action Bar
        getSupportActionBar().hide();

        // Assign the text box components
        hospitalNameTbx = (EditText) findViewById(R.id.hospitalName);
        hospitalUserNameTbx = (EditText) findViewById(R.id.hospitalUserName);
        emailTbx = (EditText) findViewById(R.id.email);
        passwordTbx = (EditText) findViewById(R.id.password);
        confirmPasswordTbx = (EditText) findViewById(R.id.confirmPassword);

        // DATABASE
        // Connecting it to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();


        // TODO : Query the info with login
        // NOTE : This is the basic implementation of login, will implement it if i got time.
        FirebaseAuth tmAuth = FirebaseAuth.getInstance();

        // Login with ur username and password
        // Currently i test with sample email and password, u can alter it with textbox value in login page
        tmAuth.signInWithEmailAndPassword("tester10@gmail.com", "123456")
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
                                    }
                                    else {
                                        // If successful, we parse the value
                                        Log.d("firebase", String.valueOf(task2.getResult().getValue()));

                                        List<User> userList = new ArrayList<>();
                                        // TODO: Rework on diagnostic and other activity, can implement as such
                                        // Basically, we get the child of the data one by one, parse and insert to our object list
                                        for (DataSnapshot ds : task2.getResult().getChildren()) {
                                            // If u want a single value, u can get it like this via variable name
                                            String email = ds.child("Email").getValue(String.class);

                                            // Parse the whole row of firebase data into our object, and add into list
                                            User user = ds.getValue(User.class);
                                            userList.add(user);

                                            // Do what you want with the list
                                            Log.d("firebase", user.FullName);
                                            Log.d("firebase", String.valueOf(user.isHospital));
                                            Log.d("firebase", user.Email);
                                        }
                                    }
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(HospitalSignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //  Assign the buttons texts
                        hospitalName = hospitalNameTbx.getText().toString();
                        hospitalUserName = hospitalUserNameTbx.getText().toString();
                        email = emailTbx.getText().toString();
                        password = passwordTbx.getText().toString();
                        confirmPassword = confirmPasswordTbx.getText().toString();

                        // Do Login Details Verification Here
                        Login(email,password, confirmPassword, hospitalName, hospitalUserName, myRef);


                    }
                }
        );


        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Test", "Failed to read value.", error.toException());
                // TODO: Place a snackbar here said have issue on firebase, log it too
                Snackbar snackbar = Snackbar.make(getWindow().getDecorView(),
                        "There was a problem on database.", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }
}