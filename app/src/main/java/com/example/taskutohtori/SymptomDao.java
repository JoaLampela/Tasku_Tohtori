package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface SymptomDao {

    @Insert
    void insertSymptom(Symptom... symptoms);

    @Query("SELECT Symptoms.id FROM Symptoms WHERE Symptoms.name = :name")
    int getSymptomIdWithName(String name);
}
