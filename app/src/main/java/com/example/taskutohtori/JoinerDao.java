package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

/**
 * A Data Access Object for the Joiner-Entity. Uses SQLite-queries to get individual values as well
 * as lists of values from any table using Joiner-links.
 * @author Joa Lampela
 * @version 1.0
 * @see Joiner
 */
@Dao
public interface JoinerDao  {

    /**
     * Defines the SQLite-query as a Java-method "getDiseasesWithSymptomName". Joins Symptoms and
     * Diseases tables and finds the matching Diseases' names.
     * @author Joa Lampela
     * @param symptomName Which Symptom's name you want to use to search for a Disease's name.
     * @return Returns the names of all Diseases with matching Symptom names as a List.
     * @see Disease
     * @see Symptom
     */
    @Query("SELECT Diseases.name FROM Diseases INNER JOIN Joiner ON Diseases.id = Joiner.diseaseId INNER JOIN Symptoms ON Joiner.symptomId = Symptoms.id WHERE Symptoms.name = :symptomName")
    List<String> getDiseasesWithSymptomName(String symptomName);

    /**
     * Defines the SQLite-query as a Java-method "getSymptomNamesWithDiseaseName". Joins Symptoms and
     * Diseases tables and finds the matching Diseases' names.
     * @author Joa Lampela
     * @param diseaseName Which Disease's name you want to use to search for a Symptom's name.
     * @return Returns the names of all Symptoms with matching Disease names as a List.
     * @see Symptom
     * @see Disease
     */
    @Query("SELECT Symptoms.name FROM Symptoms INNER JOIN Joiner ON Symptoms.id = Joiner.symptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getSymptomNamesWithDiseaseName(String diseaseName);

    /**
     * Defines the SQLite-query as a Java-method "getMainSymptomNamesWithDiseaseName". Joins
     * MainSymptoms and Diseases tables and finds the matching MainSymptoms' names.
     * @author Joa Lampela
     * @param diseaseName Which Disease's name you want to use to search for a MainSymptom's name.
     * @return Returns the names of all MainSymptoms with matching Disease names as a List.
     * @see MainSymptom
     * @see Disease
     */
    @Query("SELECT MainSymptoms.name FROM MainSymptoms INNER JOIN Joiner ON MainSymptoms.id = Joiner.mainSymptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getMainSymptomNamesWithDiseaseName(String diseaseName);

    /**
     * Defines the SQLite-query as a Java-method "getRareSymptomNamesWithDiseaseName". Joins
     * RareSymptoms and Diseases tables and finds the matching RareSymptoms' names.
     * @author Joa Lampela
     * @param diseaseName Which Disease's name you want to use to search for a MainSymptom's name.
     * @return Returns the names of all MainSymptoms with matching Disease names as a List.
     * @see RareSymptom
     * @see Disease
     */
    @Query("SELECT RareSymptoms.name FROM RareSymptoms INNER JOIN Joiner ON RareSymptoms.id = Joiner.rareSymptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getRareSymptomNamesWithDiseaseName(String diseaseName);
}
