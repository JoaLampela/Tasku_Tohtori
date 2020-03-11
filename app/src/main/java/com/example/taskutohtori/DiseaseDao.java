package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Query;
import java.util.List;

/**
 * A Data Access Object for the Disease-Entity. Uses SQLite-queries to get individual values as well
 * as lists of values from the Diseases table.
 * @author Joa Lampela
 * @version 1.0
 * @see Disease
 */
@Dao
public interface DiseaseDao {

    /**
     * Defines the SQLite-query as a Java-method "getAllDiseaseNames".
     * @author Joa Lampela
     * @return Returns the names of all diseases in the Diseases-table as a List.
     * @see Disease
     */
    @Query("SELECT Diseases.name FROM Diseases")
    List<String> getAllDiseaseNames();

    /**
     * Defines the SQLite-query as a Java-method "getDiseaseAgeBiasWithName".
     * @author Joa Lampela
     * @param name What the Disease is called.
     * @return Returns the ageBias of a Disease with the matching name in the Diseases-table.
     * @see Disease
     * @see com.example.taskutohtori.PlayActivity#ageBonus
     */
    @Query("SELECT Diseases.ageBias FROM Diseases WHERE Diseases.name = :name")
    int getDiseaseAgeBiasWithName(String name);

    /**
     * Defines the SQLite-query as a Java-method "getDiseaseSexBiasWithName".
     * @author Joa Lampela
     * @param name What the Disease is called.
     * @return Returns the sexBias of a Disease with the matching name in the Diseases-table.
     * @see Disease
     * @see com.example.taskutohtori.PlayActivity#sexBonus
     */
    @Query("SELECT Diseases.sexBias FROM Diseases WHERE Diseases.name = :name")
    float getDiseaseSexBiasWithName(String name);
}
