package com.example.taskutohtori;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.ArrayList;

/**
 * DatabaseManager takes care of getting information from database
 * @author Max Kaarla
 * @version 1.0
 */
class DataBaseManager {

    private DatabaseT database;

    /**
     * created dataBaseManager object
     * @param context activity that dataBaseManager will be used on
     */
    DataBaseManager (Context context) {
        database = Room.databaseBuilder(context, DatabaseT.class, "Database").createFromAsset("database/Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    /**
     * returns size of symptom's diseases list
     * @param symptom symptoms name
     * @return int size of list
     */
    int getSizeOfDiseases(String symptom) {
        return this.database.getJoinerDao().getDiseasesWithSymptomName(symptom).size();
    }

    /**
     * Returns size of Disease's all symptoms list
     * @param disease diseases name
     * @return int size of list
     */
    int getSizeOfSymptoms(String disease) {
        return this.database.getJoinerDao().getSymptomNamesWithDiseaseName(disease).size();
    }

    /**
     * Gets diseases symptom names list
     * @param disease disease name
     * @return ArrayList of symptom names
     */
    ArrayList<String> getSymptoms(String disease) {
        return (ArrayList<String>) this.database.getJoinerDao().getSymptomNamesWithDiseaseName(disease);
    }

    /**
     * Gets diseases main symptom names list
     * @param disease disease's name
     * @return ArrayList of main symptom names
     */
    ArrayList<String> getMainSymptoms(String disease) {
        return (ArrayList<String>) this.database.getJoinerDao().getMainSymptomNamesWithDiseaseName(disease);
    }

    /**
     * Gets diseases rare symptom names list
     * @param disease disease's name
     * @return ArrayList of main symptom names
     */
    ArrayList<String> getRareSymptoms(String disease) {
        return (ArrayList<String>) database.getJoinerDao().getRareSymptomNamesWithDiseaseName(disease);
    }

    /**
     * Gets all diseases that have this symptom
     * @param symptom symptom's name
     * @return ArrayList of disease names
     */
    ArrayList<String> getDiseases(String symptom) {
        return (ArrayList<String>) database.getJoinerDao().getDiseasesWithSymptomName(symptom);
    }

    /**
     * Gets list of all disease names
     * @return ArrayList of disease names
     */
    ArrayList<String> getAllDiseases() {
        return (ArrayList<String>) database.getDiseaseDao().getAllDiseaseNames();
    }


    /**
     * Gets diseases age bias
     * @param disease disease's name
     * @return diseases ageBias
     */
    int getAgeBias(String disease) {
        Log.d("TEST","AgeBias: "+database.getDiseaseDao().getDiseaseAgeBiasWithName(disease)+disease);
        return database.getDiseaseDao().getDiseaseAgeBiasWithName(disease);
    }

    /**
     * Gets current user's age
     * @return age of current user
     */
    int getAge() {
        return database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).age;
    }

    /**
     * gets boolean of current users isMale value
     * @return boolean isMale
     */
    boolean getIsMale() {
        return database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).male;
    }

    /**
     * Gets disease's sexBias
     * @param disease disease's name
     * @return float diseases's sexBias
     */
    float getSexBias(String disease) {
        return database.getDiseaseDao().getDiseaseSexBiasWithName(disease);
    }

    /**
     * Gets size of profiles list
     * @return integer value of size of profiles list
     */
    int getSizeOfProfileList() {
        return  database.getProfileDao().getAllProfiles().size();
    }

    /**
     * Sets all profiles active status to false
     */
    void updateAllProfilesToFalse() {
        database.getProfileDao().updateActiveAllFalse(false);
    }

    /**
     * Creates new profile to database and sets its active status to true
     * @param name name of new profile
     * @param age age of new profile
     * @param male boolean isMale for new profile
     */
    void addNewProfile(String name, int age, boolean male) {
        database.getProfileDao().insertProfile(new Profile(name, age, male, true));
    }

    /**
     * Deletes profile from database
     * @param profile Deletable profile
     */
    void deleteProfile(Profile profile) {
        database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId(profile.id));
    }

    /**
     * Sets profiles ActiveStatus to true
     * @param id Id of the profile
     */
    void updateProfileToActive(int id) {
        database.getProfileDao().updateActive(true,id);
    }

    /**
     * Gets list of all profiles
     * @return ArrayList of Profile entities
     */
    ArrayList<Profile> getAllProfiles() {
        return (ArrayList<Profile>) database.getProfileDao().getAllProfiles();
    }

    /**
     * Gets all mainSymptoms
     * @return ArrayList of all mainSymptom names from database
     */
    ArrayList<String> getAllMainSymptoms() {
        return (ArrayList<String>) database.getMainSymptomDao().getAllMainSymptoms();
    }

}
