package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

public class ProfilesActivity extends AppCompatActivity {

    ListView profileList;
    Button newProfile;
    Switch deleteSwitch;
    DatabaseT database;
    int clickedID;
    int currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        database = Room.databaseBuilder(this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        newProfile = findViewById(R.id.newProfileButton);
        deleteSwitch = findViewById(R.id.deleteSwitch);
        profileList = findViewById(R.id.profileList);
        updateProfileList();
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickedID = (int) l + 1;
                SharedPreferences prefs = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
                currentProfile = prefs.getInt("CurrentProfile", 1);

                if (!(clickedID == currentProfile) && deleteSwitch.isChecked()) {
                    database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId((int) l + 1));
                    updateProfileList();

                } else if (!deleteSwitch.isChecked()); {
                    putCurrentProfile();
                    finish();
                }
            }
        });
    }

    public void updateProfileList() {
        profileList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, database.getProfileDao().getAllProfiles()));

    }

    public void putCurrentProfile() {
        SharedPreferences prefs = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt("CurrentProfile", clickedID);
        prefEditor.commit();
    }

    public void newProfile(View view) {

        Intent intent = new Intent(ProfilesActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }
}