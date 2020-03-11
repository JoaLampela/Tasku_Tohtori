package com.example.taskutohtori;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * An abstract class that extends Android's own Room database structure (version 2.2.4). It contains
 * abstract methods for calling Data Access Objects relating to the database's tables' contents.
 * @author Joa Lampela
 * @version 1.0
*/
@Database(entities = {Profile.class, Disease.class, Symptom.class, MainSymptom.class, RareSymptom.class, Joiner.class}, version = 1)
public abstract class DatabaseT extends RoomDatabase {

    /**
     * Used for calling the Profile-entity's Data Access Object.
     * @author Joa Lampela
     * @see ProfileDao
     * @see Profile
     */
    public abstract ProfileDao getProfileDao();

    /**
     * Used for calling the Disease-entity's Data Access Object.
     * @author Joa Lampela
     * @see Disease
     * @see DiseaseDao
     */
    public abstract DiseaseDao getDiseaseDao();

    /**
     * Used for calling the MainSymptom-entity's Data Access Object.
     * @author Joa Lampela
     * @see MainSymptomDao
     * @see MainSymptom
     */
    public abstract MainSymptomDao getMainSymptomDao();

    /**
     * Used for calling the Joiner-entity's Data Access Object.
     * @author Joa Lampela
     * @see JoinerDao
     * @see Joiner
     */
    public abstract JoinerDao getJoinerDao();
}
