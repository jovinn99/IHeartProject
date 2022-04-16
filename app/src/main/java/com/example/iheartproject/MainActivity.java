package com.example.iheartproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO: Learn about how to implement firebase and how to use an Object  (very important)
    // This is the Object u would need to create, similar to a database table
    // Place your value inside, eg name, item, model those
    class MedicalEquipment{
        int id;
        String equipmentType;
    }

    public MedicalEquipment SetMedicalEquipment(String equipmentType){
        MedicalEquipment med = new MedicalEquipment();
        med.id = 1;
        med.equipmentType = equipmentType;
        return med;
    }

    class Data{
        String id;
        String operator;
        SampleObject obj;
    }
    // U can do like a nested object, so u can categorize into one object based on ur type
    class SampleObject{
        String qty;
        String expDate;
        String equipmentName;
        String manufacturer;
        String model;
        String serialNo;
    }

    // Sample Object, to test if we can put value inside the object
    public Data InstantiateObj(){
        Data data = new Data();
        SampleObject sampleObj = new SampleObject();
        sampleObj.qty="Quantity";
        sampleObj.expDate="Exp Date";
        sampleObj.equipmentName="com.example.iheartproject.Equipment Name";
        sampleObj.manufacturer="Manufacturer";
        sampleObj.model="Model";
        sampleObj.serialNo="Serial Number";
        data.id = "1";
        data.operator = "Same level";
        data.obj = sampleObj;

        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide Action Bar
        getSupportActionBar().hide();


        // Invoke this in main, as it will be used globally
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNav, navController);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Item");

        // TODO : Please learn about Object Reference by value or pointer
        // REMEMBER IT WILL BE CLONED!!!
        // Currently the object right now would use the same object
        // But i think since u testing so is fine can ignore, test for me
        Data data = InstantiateObj();
        List<Data> dataList = new ArrayList<>();
        for(int i = 0 ; i < 10; i++){
            String value = String.valueOf(i+2);
            Data newData = data;
            newData.id = value;

            // Add the object into the list
            dataList.add(newData);
        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // TODO: Learn about Json format
                // Fetch ur data, from the dataSnapshot (we set string, so is a json form)
                String value = dataSnapshot.getValue(String.class);

                // We log it so u know what happens
                Log.d("Test", "Value is: " + value);

                // We get the type of the Object, which is a list of object (can copy)
                // This is quite important, as u fetch the info d but u need to parse it from json to a more
                // bite size object for ur app to use (which is list of object)
                Type listType = new TypeToken<ArrayList<Data>>(){}.getType();

                // TODO: GSON implementation (good for u)
                // Just copy this, it basically parse ur Json string into the object type u want
                List<Data> firebaseDataList = new Gson().fromJson(value, listType);

                // We log it to see what happen
                // TODO : Populate ur UI with these info
//                for( Data fireBaseData : firebaseDataList ) {
//                    Log.d("Test", "Id is: " + fireBaseData.id);
//                    Log.d("Test", "Operator is: " + fireBaseData.operator);
//                    Log.d("Test", "com.example.iheartproject.Equipment Name is: " + fireBaseData.obj);
//                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Test", "Failed to read value.", error.toException());
                // TODO: Place a snackbar here said have issue on firebase, log it too
                Snackbar snackbar2 = Snackbar.make(getWindow().getDecorView(),
                        "There was a problem on database.", Snackbar.LENGTH_SHORT);
                snackbar2.show();
            }
        });

        //  Step 5 : Remember ur object list right? u can alter ur object value d, then convert back
        // to json string format, so u can store into ur firebase DB
        String dataJson = new Gson().toJson(dataList);

        // Step 6 : This is for u to save ur json string to database
        // Best to put like a try catch to make sure ur database no problem
        // TODO : Learn how to try catch, and test if onCancelled would handle the setValue error
        myRef.setValue(dataJson);

    }
}