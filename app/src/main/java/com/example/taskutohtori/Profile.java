package com.example.taskutohtori;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Profiles")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int age;
    public boolean male;

    public Profile(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.male = male;
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
