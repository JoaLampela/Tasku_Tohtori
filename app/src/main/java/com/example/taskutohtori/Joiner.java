package com.example.taskutohtori;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

/**
 * An Entity, meaning a table in the database. The table contains rows of objects of the given
 * Entity's associated class. This specific table acts as a many-to-many joiner of other tables
 * in the database. All columns in the table are declared as variables in the object.
 * @author Joa Lampela
 * @version 1.0
 * @see JoinerDao
 */
@Entity(tableName = "Joiner",
        foreignKeys = {@ForeignKey(entity = Disease.class, parentColumns = "id", childColumns = "diseaseId", onDelete = CASCADE),
                @ForeignKey(entity = Symptom.class, parentColumns = "id", childColumns = "symptomId", onDelete = CASCADE),
                @ForeignKey(entity = MainSymptom.class, parentColumns = "id", childColumns = "mainSymptomId", onDelete = CASCADE),
                @ForeignKey(entity = RareSymptom.class, parentColumns = "id", childColumns = "rareSymptomId", onDelete = CASCADE)},
        indices = {@Index(value = {"diseaseId", "symptomId", "mainSymptomId", "rareSymptomId"}, unique = true)})
public class Joiner {

    /**
     * This table's id is the Primary Key and must therefore have a unique value for each object.
     * The Primary Key is automatically generated within these constraints for each object in the table.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * The Foreign Key diseaseId in this table is used for connecting it with the Diseases table by
     * matching its value to Diseases' id's value. It is part of a unique index along with other
     * Foreign Keys, so no two rows of them is allowed to be the same.
     * @see Disease
     */
    @NonNull
    public Integer diseaseId;

    /**
     * The Foreign Key symptomId in this table is used for connecting it with the Symptoms table by
     * matching its value to Symptoms' id's value. It is part of a unique index along with other
     * Foreign Keys, so no two rows of them is allowed to be the same.
     * @see Symptom
     */
    @NonNull
    public Integer symptomId;

    /**
     * The Foreign Key mainSymptomId in this table is used for connecting it with the MainSymptoms table by
     * matching its value to MainSymptoms' id's value. It is part of a unique index along with other
     * Foreign Keys, so no two rows of them is allowed to be the same.
     * @see MainSymptom
     */
    @Nullable
    public Integer mainSymptomId;

    /**
     * The Foreign Key rareSymptomId in this table is used for connecting it with the rareSymptoms table by
     * matching its value to rareSymptoms' id's value. It is part of a unique index along with other
     * Foreign Keys, so no two rows of them is allowed to be the same.
     * @see RareSymptom
     */
    @Nullable
    public Integer rareSymptomId;

    /**
     * A constructor for the Joiner-objects in the table. Creates a connection between tables'
     * contents by linking their id-values as a Joiner-object that can be inserted into this table.
     * @author Joa Lampela
     * @param diseaseId What the Disease id is in the link.
     * @param symptomId What the Symptom id is in the link.
     * @param mainSymptomId What the MainSymptom id is in the link.
     * @param rareSymptomId What the RareSymptom id is in the link.
     * @see Disease
     * @see Symptom
     * @see MainSymptom
     * @see RareSymptom
     */
    public Joiner(Integer diseaseId, Integer symptomId, Integer mainSymptomId, Integer rareSymptomId) {
        this.diseaseId = diseaseId;
        this.symptomId = symptomId;
        this.mainSymptomId = mainSymptomId;
        this.rareSymptomId = rareSymptomId;
    }
}
