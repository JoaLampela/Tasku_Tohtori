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

    public int getSizeOfProfileList() {
        return  database.getProfileDao().getAllProfiles().size();
    }
    public String getProfileNameWithActiveStatus() {
        return database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).name;
    }
    public void updateAllProfilesToFalse() {
        database.getProfileDao().updateActiveAllFalse(false);
    }
    public void addNewProfile(String name, int age, boolean male, boolean active) {
        database.getProfileDao().insertProfile(new Profile(name, age, male, true));
    }
    public void deleteProfile(Profile profile) {
        database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId(profile.id));
    }
    public void updateProfileToActive(int id) {
        database.getProfileDao().updateActive(true,id);
    }
    public ArrayList<Profile> getAllProfiles() {
        return (ArrayList<Profile>) database.getProfileDao().getAllProfiles();
    }



}
