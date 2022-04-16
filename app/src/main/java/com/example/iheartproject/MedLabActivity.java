package com.example.iheartproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MedLabActivity extends AppCompatActivity {

    String[] type={"Microscopes", "Hematology Analyzers", "Blood Gas Analysers", "Autoclaves",
            "Urinalysis Analyzers", "DNA Analyzers"};
    String[] brand={"Abbott", "AmScope", "Beckman", "Benchmark", "Bio-Rad", "Illumina", "Leica",
            "Nikon", "Mindray", "Nihon Kohden", "Olympus", "Omax", "Roche", "Siemens",
            "Thermo Fisher", "Tuttnauer", "Zeiss", "Zirbus"};

    EditText expDateTbx;
    DatePickerDialog picker;
    EditText typeModelTbx;
    EditText serialNoTbx;
    EditText qtyTbx;

    String modelName;
    String serialNo;
    Integer quantity;
    String manufacturerBrand;
    String medLabType;
    String currentExpiryDate;

    // COPY THIS TO YOUR ACTIVITY PAGE
    public enum Equipment{
        Diagnostic,
        LifeSupport,
        MedLab,
        Monitor,
        Therapeutic,
        Treatment
    }

    public class EquipmentItem{
        public String Uid;
        public MedLabActivity.Equipment Equipment;
        public String EquipmentType;
        public String Brand;
        public String Model;
        public String SerialNo;
        public Integer Qty;
        public String ExpiryDate;

        public EquipmentItem(){

        }
        public EquipmentItem(String uid, MedLabActivity.Equipment eqp, String equipmentType, String brand, String model, String serialNo, Integer qty, String expiryDate )
        {
            Uid = uid;
            Equipment = eqp;
            EquipmentType = equipmentType;
            Brand = brand;
            Model = model;
            SerialNo = serialNo;
            Qty = qty;
            ExpiryDate = expiryDate;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_lab);
        setTitle("Medical Laboratory Equipment");

        // DATABASE
        // Connecting it to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        // Assign all the button with id
        typeModelTbx = (EditText) findViewById(R.id.typeModel);
        qtyTbx = (EditText) findViewById(R.id.typeQuantity);
        serialNoTbx = (EditText) findViewById(R.id.typeSerialNum);
        expDateTbx = (EditText) findViewById(R.id.typeDate);

        expDateTbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(MedLabActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expDateTbx.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Spinner dropdown1=(Spinner) findViewById(R.id.chooseType);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, type);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown1.setAdapter(adapter1);

        Spinner dropdown2=(Spinner) findViewById(R.id.chooseBrand);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, brand);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter2);

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(charSequence.charAt(i)))
                    {
                        return "";
                    }
                }
                return null;
            }
        };

        Button submitButton = (Button) findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Instantiate destination fragment
                        Log.d("HomeFrag","Button Pressed!");

                        // ASSIGN THE UI VALUE TO STRINGS
                        String uuid = UUID.randomUUID().toString().replace("-", "");
                        medLabType = dropdown1.getSelectedItem().toString();
                        manufacturerBrand = dropdown2.getSelectedItem().toString();
                        modelName = typeModelTbx.getText().toString();
                        serialNo = serialNoTbx.getText().toString();
                        quantity = Integer.valueOf(qtyTbx.getText().toString());
                        currentExpiryDate = expDateTbx.getText().toString();

                        // CREATE OBJECT WITH IT
                        MedLabActivity.EquipmentItem equipment = new MedLabActivity.EquipmentItem(uuid, MedLabActivity.Equipment.MedLab,medLabType,manufacturerBrand,modelName,serialNo,quantity,currentExpiryDate);

                        // TO update the database
                        // PUSH , get a new ref, then set/ save the value
                        DatabaseReference newRef = myRef.child("MedicalEquipment").push();
                        newRef.setValue(equipment);

                        startActivity(new Intent(MedLabActivity.this,
                                MainActivity.class));
 /*                     /*Fragment mFragment = new DiagnosticFragment();

                        // Copy this to switch page, but mfragment to desired fragment obj
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_fragment, mFragment ).commit();*/
                    }
                }
        );

        // CAN COPY IF YOU NEED TO QUERY THE INFO
        // Read from the database
        myRef.child("MedicalEquipment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // FETCHING EQUIPMENT ITEM LIST FROM FIREBASE
                List<MedLabActivity.EquipmentItem> eqpList = new ArrayList<MedLabActivity.EquipmentItem>();

                Map<String, MedLabActivity.EquipmentItem> td = (HashMap<String, MedLabActivity.EquipmentItem>) dataSnapshot.getValue();
                List<Object> tdList = new ArrayList<Object>(td.values());
                for(Object objectTd :tdList ){
                    MedLabActivity.EquipmentItem eqpItem2 = new MedLabActivity.EquipmentItem();
                    Map<String, String> item = (Map<String, String>) objectTd;
                    for (Map.Entry<String,String> entry : item.entrySet()) {
                        String key = entry.getKey();
                        String value = String.valueOf(entry.getValue());

                        if(key.equals("com.example.iheartproject.Equipment"))
                            eqpItem2.Equipment = MedLabActivity.Equipment.valueOf(value);
                        if(key.equals("Qty"))
                            eqpItem2.Qty = Integer.valueOf(value);
                        if(key.equals("ExpiryDate"))
                            eqpItem2.ExpiryDate = value;
                        if(key.equals("EquipmentType"))
                            eqpItem2.EquipmentType = value;
                        if(key.equals("SerialNo"))
                            eqpItem2.SerialNo = value;
                        if(key.equals("Brand"))
                            eqpItem2.Brand = value;
                        if(key.equals("Model"))
                            eqpItem2.Model = value;
                        if(key.equals("Uid"))
                            eqpItem2.Uid = value;

                        System.out.println(key);
                        System.out.println(value);
                        // do stuff
                    }
                    eqpList.add(eqpItem2);
                }

                System.out.println("LIST ITEM");
                for(MedLabActivity.EquipmentItem item: eqpList){
                    System.out.println(item.Brand);
                    System.out.println(item.Equipment);
                }

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