package com.example.iheartproject;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

// TODO: this is a simple class for user, u gotta add ur hospital info into it
// I have moved this user out as a single class, easier for any java activity to access it
// If u follow the steps in usersignupactivity on parsing the value to object, u gotta go to proguard-rules.pro, and add ur class there
// https://stackoverflow.com/questions/41650103/no-setter-field-for-found-android-firebase
public class Hospital {
    public String UserID;
    public String Email;
    public String FullName;
    public String UserName;
    public String HospitalName;
    public boolean isHospital = true;

    public Hospital() {

    }

    public Hospital(String UserID, String Email, String FullName, String UserName, String HospitalName, boolean isHospital) {
        this.UserID = UserID;
        this.Email = Email;
        this.FullName = FullName;
        this.UserName = UserName;
        this.HospitalName = HospitalName;
        this.isHospital = isHospital;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("User ID", UserID);
        result.put("Email", Email);
        result.put("FullName", FullName);
        result.put("UserName", UserName);
        result.put("HospitalName", HospitalName);
        result.put("isHospital", isHospital);

        return result;
    }
}
