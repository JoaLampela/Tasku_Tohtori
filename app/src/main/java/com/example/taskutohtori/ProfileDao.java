package com.example.taskutohtori;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * A Data Access Object for the Profile-Entity. Uses SQLite-queries to get rows and lists of values
 * from the Profiles table as well as being able to insert, delete and update values in said table.
 * @author Joa Lampela
 * @version 1.0
 * @see Profile
 */
@Dao
public interface ProfileDao {

    /**
     * Defines the SQLite-query as a Java-method "insertProfiles".
     * @author Joa Lampela
     * @param profiles Which Profile is to be inserted into the Profiles-table.
     * @see Profile
     */
    @Insert
    void insertProfile(Profile... profiles);

    /**
     * Defines the SQLite-query as a Java-method "getAllProfiles".
     * @author Joa Lampela
     * @return Returns all the rows in the Profiles-table as Profile-objects in a List.
     * @see Profile
     */
    @Query("SELECT * FROM Profiles")
    List<Profile> getAllProfiles();

    /**
     * Defines the SQLite-query as a Java-method "deleteProfile". Deletes the matching Profile from the
     * Profiles table.
     * @author Joa Lampela
     * @param profiles Which Profile you want to be removed from the Profiles table.
     * @see Profile
     */
    @Delete
    void deleteProfile(Profile... profiles);

    /**
     * Defines the SQLite-query as a Java-method "getProfileWithId".
     * @author Joa Lampela
     * @param id What the wanted Profile-object's unique id is in the Profiles table.
     * @return Returns the row in the Profiles-table with the matching id as a Profile-object.
     * @see Profile
     */
    @Query("SELECT * FROM Profiles WHERE Profiles.id = :id")
    Profile getProfileWithId(int id);

    /**
     * Defines the SQLite-query as a Java-method "updateActive". Sets the Profile with the matching
     * id to be the active or inactive Profile.
     * @author Joa Lampela
     * @param active What the wanted state of the Profile in the Profiles table is.
     * @param id What the wanted Profile-object's unique id is.
     * @see Profile
     */
    @Query("UPDATE Profiles SET active = :active WHERE Profiles.id = :id")
    void updateActive(boolean active, int id);

    /**
     * Defines the SQLite-query as a Java-method "updateActiveAllFalse". Sets all Profile-objects
     * in the Profiles table to be inactive Profiles.
     * @author Joa Lampela
     * @see Profile
     */
    @Query("UPDATE Profiles SET active = 'false'")
    void updateActiveAllFalse();

    /**
     * Defines the SQLite-query as a Java-method "getAllProfilesWithActiveStatus".
     * @author Joa Lampela
     * @param active
     * @return Returns the rows in the Profiles-table with the matching active status as Profile-objects in a List.
     * @see Profile
     */
    @Query("SELECT * FROM Profiles WHERE active = :active")
    List<Profile> getAllProfilesWithActiveStatus(boolean active);
}
