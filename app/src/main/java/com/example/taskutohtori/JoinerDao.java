package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

@Dao
public interface JoinerDao  {

    @Query("SELECT Diseases.name FROM Diseases INNER JOIN Joiner ON Diseases.id = Joiner.diseaseId INNER JOIN Symptoms ON Joiner.symptomId = Symptoms.id WHERE Symptoms.name = :symptomName")
    List<String> getDiseasesWithSymptomName(String symptomName);

    @Query("SELECT Symptoms.name FROM Symptoms INNER JOIN Joiner ON Symptoms.id = Joiner.symptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getSymptomNamesWithDiseaseName(String diseaseName);

    @Query("SELECT MainSymptoms.name FROM MainSymptoms INNER JOIN Joiner ON MainSymptoms.id = Joiner.mainSymptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getMainSymptomNamesWithDiseaseName(String diseaseName);

    @Query("SELECT RareSymptoms.name FROM RareSymptoms INNER JOIN Joiner ON RareSymptoms.id = Joiner.rareSymptomId INNER JOIN Diseases ON Joiner.diseaseId = Diseases.id WHERE Diseases.name LIKE :diseaseName")
    List<String> getRareSymptomNamesWithDiseaseName(String diseaseName);
}
