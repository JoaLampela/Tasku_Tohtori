package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

/**
 * A Data Access Object for the MainSymptom-Entity. Uses SQLite-queries to get lists of values from
 * the MainSymptoms table.
 * @author Joa Lampela
 * @version 1.0
 * @see MainSymptom
 */
@Dao
public interface MainSymptomDao {

    /**
     * Defines the SQLite-query as a Java-method "getAllMainSymptomNames".
     * @author Joa Lampela
     * @return Returns the unique names of all mainSymptoms in the MainSymptoms-table as a List.
     * @see MainSymptom
     */
    @Query("SELECT DISTINCT MainSymptoms.name FROM MainSymptoms")
    List<String> getAllMainSymptomNames();
}
