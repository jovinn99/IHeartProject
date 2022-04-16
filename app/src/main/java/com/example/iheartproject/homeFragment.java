package com.example.iheartproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Define variables here
    private TextView textView;
    private String backValue;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
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


        backValue = getActivity().getIntent().getStringExtra("BackValue");
        if(backValue == null)
        {
            Log.d("Main","No value found");
        }
        else
        {
            Log.d("Main",backValue);
            // Perform any action here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_home, container, false);

        /*// Perform any action here
        textView = (TextView) inf.findViewById(R.id.textView3);
        if(backValue == null)
        {
            textView.setText("New text");
        }
        else{
            textView.setText(backValue);
        }*/


        Button diagnosticButton = (Button) inf.findViewById(R.id.DiagnosticButton);
        diagnosticButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Instantiate destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        startActivity(new Intent( getActivity(), DiagnosticActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button lifeSupportButton = (Button) inf.findViewById(R.id.LifeSupportButton);
        lifeSupportButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("HomeFrag","Life Support Button Pressed!");

                        // Route the current page to another
                        // ONLY change the "LifeSupportActivity" to "YourDesiredActivity"
                        startActivity(new Intent( getActivity(), LifeSupportActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button treatmentButton = (Button) inf.findViewById(R.id.TreatmentButton);
        treatmentButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        startActivity(new Intent( getActivity(), TreatmentActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button monitorButton = (Button) inf.findViewById(R.id.MonitorButton);
        monitorButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        startActivity(new Intent( getActivity(), MonitorActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button medLabButton = (Button) inf.findViewById(R.id.MedLabButton);
        medLabButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        startActivity(new Intent( getActivity(), MedLabActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        Button therapeuticButton = (Button) inf.findViewById(R.id.TherapeuticButton);
        therapeuticButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // instantiate ur destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        startActivity(new Intent( getActivity(), TherapeuticActivity.class));
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