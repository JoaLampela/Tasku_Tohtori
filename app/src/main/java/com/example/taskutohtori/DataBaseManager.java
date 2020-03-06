package com.example.taskutohtori;

import android.content.Context;

import androidx.room.Room;

import java.util.ArrayList;

public class DataBaseManager {

    private DatabaseT database;

    DataBaseManager (Context context) {
        this.database = Room.databaseBuilder(context, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
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



}
