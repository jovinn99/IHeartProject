package com.example.iheartproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HospitalUserProfileActivity extends AppCompatActivity {

    private EditText updateHospitalNameTbx;
    private EditText updateHospitalUserNameTbx;
    private EditText updateEmailTbx;
    private String updateHospitalName;
    private String updateHospitalUserName;
    private String updateEmail;
    String TAG = "HospitalUserProfileActivity";

    public void EditUserProfile(FirebaseUser user, String previousEmail, String updateHospitalName, String updateHospitalUserName, String updateEmail)
    {
        try
        {
            // DATABASE
            // Connecting it to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference();

            user.updateEmail(updateEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated to " + updateEmail);
                            }
                            else {
                                Log.d(TAG, "User email address not updated.");
                                Toast.makeText(HospitalUserProfileActivity.this, "User email fail to update.",
                                        Toast.LENGTH_SHORT).show();
                                Intent newIntent = new Intent (HospitalUserProfileActivity.this, profileFragment.class);
                                startActivity(newIntent);
                                finish();
                            }
                        }
                    });

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(updateHospitalName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");

                                // Query to realtime database and acquire additional info
                                myRef.child("users").child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task1) {
                                        if (!task1.isSuccessful()) {
                                            // If not successful, stop the process
                                            Log.e("firebase", "Error getting data.", task1.getException());
                                            Toast.makeText(HospitalUserProfileActivity.this, "Error occurred when connecting to firebase.",
                                                    Toast.LENGTH_SHORT).show();
                                            Intent newIntent = new Intent(HospitalUserProfileActivity.this, profileFragment.class);
                                            startActivity(newIntent);
                                            finish();
                                        }
                                        else {
                                            // If successful, parse the value
                                            Log.d("firebase", String.valueOf(task1.getResult().getValue()));

                                            List<Hospital> userList = new ArrayList<>();
                                            Hospital currentUser = null;
                                            String key = null;
                                            // Basically, we get the child of the data one by one, parse and insert to our object list
                                            for (DataSnapshot ds: task1.getResult().getChildren()) {
                                                Log.d(TAG, ds.getKey());
                                                // Parse the whole row of firebase data into object, and add into list
                                                Hospital userObj = ds.getValue(Hospital.class);
                                                if (userObj.Email == null)
                                                    continue;
                                                if (userObj.Email.equals(previousEmail)) {
                                                    Log.d("firebase", "User email match.");
                                                    currentUser = userObj;
                                                    key = ds.getKey();
                                                    break;
                                                }
                                            }

                                            // Check if user exist
                                            if (currentUser == null) {
                                                Log.e("firebase", "Unable to find user info in firebase.");
                                                Toast.makeText(HospitalUserProfileActivity.this, "Unable to fetch user info.",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent newIntent = new Intent(HospitalUserProfileActivity.this, profileFragment.class);
                                                startActivity(newIntent);
                                                finish();
                                            }
                                            else {
                                                Log.d("firebase", "Current user email is " + user.getEmail());

                                                // Set your new value here and update to database
                                                currentUser.Email = updateEmail;
                                                currentUser.UserName = updateHospitalUserName;
                                                currentUser.FullName = updateHospitalName;
                                                Log.d("firebase", "Current user email changed to " + user.getEmail());

                                                myRef.child("users").child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if (!task.isSuccessful()) {
                                                            Log.e("firebase", "Error getting data.", task.getException());
                                                        }
                                                        else {
                                                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                                        }
                                                    }
                                                });
                                                Map<String, Object> values = currentUser.toMap();

                                                myRef.child("users").child("test").child(key).setValue(currentUser)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("firebase", "User has updated.");
                                                                Toast.makeText(HospitalUserProfileActivity.this, "User profile updated",
                                                                        Toast.LENGTH_SHORT).show();
                                                                Intent newIntent = new Intent(HospitalUserProfileActivity.this, profileFragment.class);
                                                                startActivity(newIntent);
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("firebase", "Unable to update user info in firebase.");
                                                                Intent newIntent = new Intent(HospitalUserProfileActivity.this, profileFragment.class);
                                                                startActivity(newIntent);
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "User profile fail to update.");
                                Toast.makeText(HospitalUserProfileActivity.this, "User profile fail to update.",
                                        Toast.LENGTH_SHORT).show();
                                Intent newIntent = new Intent(HospitalUserProfileActivity.this, profileFragment.class);
                                startActivity(newIntent);
                                finish();
                            }
                        }
                    });
        }
        catch (Exception ex)
        {
            Log.e(TAG, "updateWithEmail:failure" + ex.getMessage());
            Toast.makeText(HospitalUserProfileActivity.this, "Unexpected error occurred during edit user profile: " + ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_user_profile);

        setTitle("Edit User Profile");

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        updateHospitalNameTbx = (EditText) findViewById(R.id.updateHospitalName);
        updateHospitalUserNameTbx = (EditText) findViewById(R.id.updateHospitalUserName);
        updateEmailTbx = (EditText) findViewById(R.id.updateEmail);

        Gson gson = new Gson();
        String userJson = getIntent().getStringExtra("user");
        Log.d("Hospital User Profile",userJson);

        Hospital user = gson.fromJson(userJson, Hospital.class);
        updateHospitalNameTbx.setText(user.HospitalName);
        updateHospitalUserNameTbx.setText(user.UserName);
        updateEmailTbx.setText(user.Email);

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("FB auth", "User has signed in.");
        } else {
            Log.d("FB auth", "User not found.");
        }

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener((view -> {
            String previousEmail = user.Email;
            updateHospitalName = updateHospitalNameTbx.getText().toString();
            updateHospitalUserName = updateHospitalUserNameTbx.getText().toString();
            updateEmail = updateEmailTbx.getText().toString();

            EditUserProfile(fbUser, previousEmail, updateHospitalName, updateHospitalUserName, updateEmail);
        }));

        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        Log.d(TAG, "onCreate: " + fullName + " ");
    }
}