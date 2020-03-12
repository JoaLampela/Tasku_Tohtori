package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * An Entity, meaning a table in the database. The table contains rows of objects of the given
 * Entity's associated class. All columns in the table are declared as variables in the object.
 * @author Joa Lampela
 * @version 1.0
 * @see DiseaseDao
 */
@Entity(tableName = "Diseases",
        indices = {@Index(value = {"name"}, unique = true)})
public class Disease {

    /**
     * This table's id is the Primary Key and must therefore have a unique value for each object.
     * The Primary Key is automatically generated within these constraints for each object in the table.
     */
    @PrimaryKey(autoGenerate = true)
    int id;

    /**
     * The column "name" is set as a unique valued index, so there can be no two Disease-objects
     * with the same value for their name.
     */
    String name;

    /**
     * Disease's ageBias from 0 to 5.
     * @see com.example.taskutohtori.PlayActivity#ageBonus
     */
    int ageBias;

    /**
     * Disease's sexBias from -1 to 1.
     * @see com.example.taskutohtori.PlayActivity#sexBonus
     */
    float sexBias;

    /**
     * A constructor for the Disease-objects in the table.
     * @author Joa Lampela
     * @param name What the Disease is called.
     * @param ageBias What age group the Disease belongs to (from 0 to 5).
     * @param sexBias Which sex the Disease is more likely to affect (from -1 to 1).
     * @see com.example.taskutohtori.PlayActivity#ageBonus
     * @see com.example.taskutohtori.PlayActivity#sexBonus
     */
    public Disease(String name, int ageBias, float sexBias) {
        this.name = name;
        this.ageBias = ageBias;
        this.sexBias = sexBias;
    }
}
