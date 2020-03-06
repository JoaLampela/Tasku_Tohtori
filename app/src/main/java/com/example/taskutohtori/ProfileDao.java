package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert
    void insertProfile(Profile... profiles);

    @Query("SELECT * FROM Profiles")
    List<Profile> getAllProfiles();

    @Query("SELECT * FROM Profiles WHERE Profiles.name LIKE :name AND Profiles.age LIKE :age AND Profiles.male LIKE :male")
    List<Profile> getAllProfilesIDsWithAllAttributes(String name, int age, boolean male);

    @Query("SELECT Profiles.name FROM Profiles WHERE Profiles.id = :id")
    List<String> getProfileNamesWithId(int id);

    @Delete
    void deleteProfile(Profile... profiles);

    @Query("SELECT * FROM Profiles WHERE Profiles.id = :id")
    Profile getProfileWithId(int id);

    @Query("SELECT Profiles.id FROM Profiles")
    List<Integer> getAllIds();
}
