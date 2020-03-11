package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 *
 */
@Entity(tableName = "RareSymptoms")
public class RareSymptom {

    /**
     * All columns in the table are declared as variables in the object. This table's id is the
     * Primary Key and must therefore have a unique value for each object. The Primary Key is
     * automatically generated within these constraints for each object in the table.
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
