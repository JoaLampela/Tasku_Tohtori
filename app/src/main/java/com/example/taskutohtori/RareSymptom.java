package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * An Entity, meaning a table in the database. The table contains rows of objects of the given
 * Entity's associated class. All columns in the table are declared as variables in the object.
 * @author Joa Lampela
 * @version 1.0
 */
@Entity(tableName = "RareSymptoms")
public class RareSymptom {

    /**
     * This table's id is the Primary Key and must therefore have a unique value for each object.
     * The Primary Key is automatically generated within these constraints for each object in the table.
     */
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;

    /**
     * A constructor for the RareSymptom-objects in the table.
     * @author Joa Lampela
     * @param name What the RareSymptom is called.
     */
    public RareSymptom(String name) {
        this.name = name;
    }
}
