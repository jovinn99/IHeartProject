package com.example.iheartproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link statusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class statusFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public statusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment statusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static statusFragment newInstance(String param1, String param2) {
        statusFragment fragment = new statusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_status, container, false);

        Button inventory = (Button) inf.findViewById(R.id.inventory);
        inventory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), InventoryActivity.class));
                    }
                }
        );


        Button status = (Button) inf.findViewById(R.id.status);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Instantiate destination fragment
                Log.d("Status Frag", "Button Pressed!");

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference();

                // Connecting it to the firebase authentication database
                FirebaseAuth tmAuth = FirebaseAuth.getInstance();
                FirebaseUser user = tmAuth.getCurrentUser();
                myRef.child("users").child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()) {
                            // If not successful, we stop the process
                            Log.e("firebase", "Error getting data.", task.getException());
                            return;
                        } else {
                            // If successful, we parse the value
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));

                            List<User> userList = new ArrayList<>();
                            User currentUser = null;
                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                // Parse the whole row of firebase data into our object, and add into list
                                User userObj = ds.getValue(User.class);
                                userList.add(userObj);
                            }

                            // Loop again to match user
                            for (User userObj : userList) {
                                // Try to match if user info is same as firebase realtime
                                if (user.getEmail().equals(userObj.Email)) {
                                    Log.d("firebase", "User email match.");
                                    currentUser = userObj;
                                    break;
                                }
                            }

                            // Check if user exist
                            if (currentUser == null) {
                                Log.e("firebase", "Unable to find user info in firebase.");
                                return;
                            } else {
                                if (currentUser.isHospital) {
                                    Intent i = new Intent(getActivity(), HospitalRequestActivity.class);
                                    startActivity(i);
                                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                                } else {
                                    Intent i = new Intent(getActivity(), RespondActivity.class);
                                    startActivity(i);
                                    getActivity().getFragmentManager().popBackStack();
                                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                                }
                            }
                        }
                    }
                });
            }
        });


        return inf;
    }

    /*@Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().getFragmentManager().popBackStack();
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }*/
}