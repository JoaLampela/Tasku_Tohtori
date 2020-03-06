package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Profiles")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int age;
    public boolean male;
    public boolean active;

    public Profile(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.male = male;
        this.active = false;
    }

    @Ignore
    public Profile(String name, int age, boolean male, boolean active) {
        this.name = name;
        this.age = age;
        this.male = male;
        this.active = active;
    }

    public String getSexString() {
        if (this.male) {
            return "Mies";
        } else {
            return "Nainen";
        }
    }

    @Override
    public String toString() {
        return this.name + ", " + this.age + ", " + getSexString();
    }
}
