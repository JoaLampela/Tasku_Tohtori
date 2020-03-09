package com.example.taskutohtori;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;

public class DataBaseManager {

    private DatabaseT database;

    DataBaseManager (Context context) {
        database = Room.databaseBuilder(context, DatabaseT.class, "Database").createFromAsset("database/Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        Log.d("TEST", "Database first item name: " + this.getSymptoms("Flunssa"));
    };

    public int getSizeOfMainSymptoms(String nextDisease) {
        return this.database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(nextDisease)).size();
    }

    public int getSizeOfDiseases(String symptom) {
        return this.database.getJoinerDao().getDiseasesWithSymptomName(symptom).size();
    }
    public int getSizeOfSymptoms(String disease) {
        return this.database.getJoinerDao().getSymptomNamesWithDiseaseName(disease).size();
    }

    public ArrayList<String> getSymptoms(String disease) {
        return (ArrayList<String>) this.database.getJoinerDao().getSymptomNamesWithDiseaseName(disease);
    }

    public ArrayList<String> getMainSymptoms(String disease) {
        return (ArrayList<String>) this.database.getJoinerDao().getMainSymptomNamesWithDiseaseName(disease);
    }

    public ArrayList<String> getRareSymptoms(String disease) {
        return (ArrayList<String>) database.getJoinerDao().getRareSymptomNamesWithDiseaseName(disease);
    }

    public ArrayList<String> getDiseases(String symptom) {
        return (ArrayList<String>) database.getJoinerDao().getDiseasesWithSymptomName(symptom);
    }

    public ArrayList<String> getAllDiseases() {
        return (ArrayList<String>) database.getDiseaseDao().getAllDiseaseNames();
    }

    public int getAgeBias(String disease) {
        return database.getDiseaseDao().getDiseaseAgeBiasWithName(disease);
    }
    public int getAge() {
        return database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).age;
    }
    public boolean getIsMale() {
        return database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).male;
    }
    public float getSexBias(String disease) {
        return database.getDiseaseDao().getDiseaseSexBiasWithName(disease);
    }



}
