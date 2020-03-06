package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface JoinerDao {

    @Insert
    void insertJoinerValue(Joiner... joiners);

    @Query("SELECT * FROM Diseases INNER JOIN Joiner ON Diseases.id = Joiner.diseaseId WHERE Joiner.symptomId = :symptomId")
    List<Disease> getDiseasesWithSymptomId(int symptomId);

    @Query("SELECT * FROM Symptoms INNER JOIN Joiner ON Symptoms.id = Joiner.symptomId WHERE Joiner.diseaseId = :diseaseId")
    List<Symptom> getSymptomsWithDiseaseId(int diseaseId);

    @Query("SELECT * FROM MainSymptoms INNER JOIN Joiner ON MainSymptoms.id = Joiner.mainSymptomId WHERE Joiner.diseaseId = :diseaseId")
    List<MainSymptom> getMainSymptomsWithDiseaseId(int diseaseId);

    @Query("SELECT * FROM RareSymptoms INNER JOIN Joiner ON RareSymptoms.id = Joiner.rareSymptomId WHERE Joiner.diseaseId = :diseaseId")
    List<RareSymptom> getRareSymptomsWithDiseaseId(int diseaseId);

    @Query("SELECT MainSymptoms.name FROM MainSymptoms INNER JOIN Joiner ON MainSymptoms.id = Joiner.mainSymptomId WHERE Joiner.diseaseId = :diseaseId")
    List<String> getMainSymptomNamesWithDiseaseId(int diseaseId);

    @Query("SELECT Diseases.name FROM Diseases INNER JOIN Joiner ON Diseases.id = Joiner.diseaseId INNER JOIN Symptoms ON Joiner.symptomId = Symptoms.id WHERE Symptoms.name = :symptomName")
    List<String> getDiseasesWithSymptomName(String symptomName);
}
