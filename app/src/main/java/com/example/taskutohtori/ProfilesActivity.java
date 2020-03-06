package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

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

                profile = (Profile) adapterView.getItemAtPosition((int) l);
                clickedID = profile.id;
                SharedPreferences prefs = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
                currentID = prefs.getInt("CurrentProfile", 1);
                Log.d("tägi", "profileID " + clickedID + " currentID " + currentID);

                if (!(clickedID == currentID) && delete) {
                    database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId(profile.id));
                    updateProfileList();

                } else if (!delete) {
                    putCurrentProfile();
                    finish();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Käytössä olevaa profiilia ei voi poistaa.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
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
        prefEditor.putInt("CurrentProfile", profile.id);
        prefEditor.commit();
    }

    public void newProfile(View view) {
        Intent intent = new Intent(ProfilesActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }
}