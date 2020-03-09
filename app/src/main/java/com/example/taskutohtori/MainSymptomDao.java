package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MainSymptomDao {

    @Insert
    void insertMainSymptom(MainSymptom... mainSymptoms);

    @Query("SELECT MainSymptoms.id FROM MainSymptoms WHERE MainSymptoms.name = :name")
    int getMainSymptomIdWithName(String name);
}
