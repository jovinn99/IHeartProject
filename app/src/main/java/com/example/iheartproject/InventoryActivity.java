package com.example.iheartproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner$InspectionCompanion;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class InventoryActivity extends AppCompatActivity {

    private String equipmentNameIpt;
    private String brandIpt;
    private Spinner chooseTypeSpn;
    private Spinner chooseBrandSpn;
    private Button searchDatabaseButton;
    private RecyclerView recyclerView;
    private EquipmentListAdapter equipmentListAdapter;
    private FirebaseDatabase fbDb;
    private DatabaseReference dbRef;
    private Equipment equipmentInfo;
    private ArrayList<Equipment> equipmentArray = new ArrayList<>();
    private ArrayList<Equipment> filterEquipmentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setTitle("Equipment Management");

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        chooseTypeSpn = findViewById(R.id.chooseType);
        chooseBrandSpn = findViewById(R.id.chooseBrand);
        searchDatabaseButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.equipmentList);

        fillEquipmentArray();

        // Search result (EquipmentListAdapter) in recycler view adapter operation
        equipmentListAdapter = new EquipmentListAdapter(this);
        recyclerView.setAdapter(equipmentListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // EquipmentType Spinner adapter
        chooseTypeSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                equipmentNameIpt = chooseBrandSpn.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(InventoryActivity.this, "Please select the equipment.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Brand Spinner adapter
        chooseBrandSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                brandIpt = chooseBrandSpn.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(InventoryActivity.this, "Please select the brand.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Search button listener
        searchDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEquipmentList();
            }
        });
    }

    // Filter equipmentArray and then add matched result into ArrayList filteredEquipmentArray
    public void searchEquipmentList() {
        // Create new object of ArrayList everytime searching to avoid loading duplicated and old filtered ArrayList
        filterEquipmentArray = new ArrayList<>();

        for(int i = 0; i < equipmentArray.size(); i++) {
            if(equipmentArray.get(i).getEquipmentName().equals(equipmentNameIpt) && equipmentArray.get(i).getBrand().equals(brandIpt)) {
                filterEquipmentArray.add(equipmentArray.get(i));
            }
        }
        equipmentListAdapter.setEquipment(filterEquipmentArray);
    }

    // Load all the data into the ArrayList equipmentArray
    public void fillEquipmentArray() {
        fbDb = FirebaseDatabase.getInstance("https://telemedicine2022-2137d-default-rtdb.asia-southeast1.firebasedatabase.app/");
        dbRef = fbDb.getReference("Medical Equipment");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    equipmentInfo = dataSnapshot.getValue(Equipment.class);
                    equipmentArray.add(equipmentInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(InventoryActivity.this, "Fail to fetch data from database.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}