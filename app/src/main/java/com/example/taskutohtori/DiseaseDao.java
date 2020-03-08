package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiseaseDao {

    @Insert
    void insertDisease(Disease... diseases);

    @Query("SELECT Diseases.id FROM Diseases WHERE Diseases.name = :name")
    int getDiseaseIdWithName(String name);

    @Query("SELECT Diseases.name FROM Diseases")
    List<String> getAllDiseaseNames();

    @Query("SELECT Diseases.ageBias FROM Diseases WHERE Diseases.name = :name")
    int getDiseaseAgeBiasWithName(String name);

    @Query("SELECT Diseases.sexBias FROM Diseases WHERE Diseases.name = :name")
    float getDiseaseSexBiasWithName(String name);
}
