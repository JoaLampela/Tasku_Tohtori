package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

public class ProfilesActivity extends AppCompatActivity {

    ListView profileList;
    Button newProfile;
    Switch deleteSwitch;
    DatabaseT database;
    int clickedID;
    int currentID;
    boolean delete;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        database = Room.databaseBuilder(this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        newProfile = findViewById(R.id.newProfileButton);
        deleteSwitch = findViewById(R.id.deleteSwitch);
        profileList = findViewById(R.id.profileList);
        updateProfileList();

        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                delete = isChecked;
            }
        });

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                profile = (Profile) adapterView.getAdapter().getItem((int) l);

                if (!(profile.active) && delete) {
                    database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId(profile.id));
                    updateProfileList();

                } else {
                    database.getProfileDao().updateActiveAllFalse(false);
                    database.getProfileDao().updateActive(true,profile.id);
                    finish();
                }
            }
        });
    }

    public void updateProfileList() {
        profileList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, database.getProfileDao().getAllProfiles()));

    }

    public void newProfile(View view) {
        Intent intent = new Intent(ProfilesActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }
}