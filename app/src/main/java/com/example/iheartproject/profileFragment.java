package com.example.iheartproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_profile, container, false);

        Button userProfile = (Button) inf.findViewById(R.id.userProfile);
        userProfile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Instantiate destination fragment
                        Log.d("HomeFrag", "Button Pressed!");

                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference();

                        // Connecting it to the firebase authentication database
                        FirebaseAuth tmAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = tmAuth.getCurrentUser();
                        myRef.child("users").child("test").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                if (!task2.isSuccessful()) {
                                    // If not successful, we stop the process
                                    Log.e("firebase", "Error getting data", task2.getException());
                                    return;
                                } else {
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
                                        // try to match if user info is same as firebase realtime
                                        if (user.getEmail().equals(userObj.Email)) {
                                            Log.d("firebase", "User email match");
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
                                            Intent i = new Intent(getActivity(), HospitalUserProfileActivity.class);
                                            Gson gson = new Gson();
                                            String userJson = gson.toJson(currentUser);
                                            i.putExtra("user", userJson);
                                            startActivity(i);
                                            ((Activity) getActivity()).overridePendingTransition(0, 0);
                                        } else {
                                            Intent i = new Intent(getActivity(), DonorUserProfileActivity.class);
                                            // We parsing user object into json, so that we can pass it to other activities
                                            Gson gson = new Gson();
                                            String userJson = gson.toJson(currentUser);

                                            // Assign ur information to new intent, with a key
                                            i.putExtra("user", userJson);

                                            startActivity(i);

                                            getActivity().getFragmentManager().popBackStack();
                                            ((Activity) getActivity()).overridePendingTransition(0, 0);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
        );

        Button termsOfService = (Button) inf.findViewById(R.id.termsOfService);
        termsOfService.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("Profile Frag", "Terms of Service Button Pressed!");

                        // Route the current page to another
                        // ONLY change the "LifeSupportActivity" to "YourDesiredActivity"
                        startActivity(new Intent(getActivity(), TermAndConditionActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button contactUs = (Button) inf.findViewById(R.id.contactUs);
        contactUs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:0167432134"));
                        startActivity(intent);
                    }
                }
        );

        Button logOut = (Button) inf.findViewById(R.id.logOut);
        logOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("Profile Frag", "Log Out Button Pressed!");

                        // Route the current page to another
                        // ONLY change the "LifeSupportActivity" to "YourDesiredActivity"
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        //startActivity(new Intent(getActivity(), LoginActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        // Make sure to return view
        return inf;
    }

}

    /*@Override
    public void onResume() {
        super.onResume();
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
        getActivity().getFragmentManager().popBackStack();
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}*/