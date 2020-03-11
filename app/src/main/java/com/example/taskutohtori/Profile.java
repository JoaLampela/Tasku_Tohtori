package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * An Entity, meaning a table in the database. The table contains rows of objects of the given
 * Entity's associated class.
 * @author Joa Lampela
 * @version 1.0
 * @see ProfileDao
 */
@Entity(tableName = "Profiles")
public class Profile {

    /**
     * All columns in the table are declared as variables in the object. This table's id is the
     * Primary Key and must therefore have a unique value for each object. The Primary Key is
     * automatically generated within these constraints for each object in the table.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int age;
    public boolean male;
    public boolean active;

    /**
     * The primary constructor for the Profile-objects in the table.
     * @author Joa Lampela
     * @param name What the Profile is called (from 1 to 20 characters long).
     * @param age What the Profile's age is (from 0 to 99).
     * @param male Whether the Profile is male or not.
     */
    public Profile(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.male = male;
        this.active = false;
    }

    /**
     * An alternate (secondary) constructor for the Profile-objects in the table.
     * @author Joa Lampela
     * @param name What the Profile is called (from 1 to 20 characters long).
     * @param age What the Profile's age is (from 0 to 99).
     * @param male Whether the Profile is male or not.
     * @param active Whether the Profile is the active one or not.
     */
    @Ignore
    public Profile(String name, int age, boolean male, boolean active) {
        this.name = name;
        this.age = age;
        this.male = male;
        this.active = active;
    }

    /**
     * Returns whether the profile is male or female
     * @author Jarno Tiainen
     * @return profiles sex as string
     */
    public String getSexString() {
        if (this.male) {
            return "Mies";
        } else {
            return "Nainen";
        }
    }
}
