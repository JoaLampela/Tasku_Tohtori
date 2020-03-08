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

    @Delete
    void deleteProfile(Profile... profiles);

    @Query("SELECT * FROM Profiles WHERE Profiles.id = :id")
    Profile getProfileWithId(int id);

    @Query("UPDATE Profiles SET active = :active WHERE Profiles.id = :id")
    void updateActive(boolean active, int id);

    @Query("UPDATE Profiles SET active = :isTrue")
    void updateActiveAllFalse(boolean isTrue);

    @Query("SELECT * FROM Profiles WHERE active = :active")
    List<Profile> getAllProfilesWithActiveStatus(boolean active);
}
