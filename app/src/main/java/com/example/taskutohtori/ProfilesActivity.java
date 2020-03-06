package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
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

                if (!(profile.active) && delete && database.getProfileDao().getAllProfiles().size() > 1) {
                    database.getProfileDao().deleteProfile(database.getProfileDao().getProfileWithId(profile.id));
                    updateProfileList();

                    Context context = getApplicationContext();
                    CharSequence text = "Profiili poistettu:  " + profile.name;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();

                } else if(!delete) {
                    database.getProfileDao().updateActiveAllFalse(false);
                    database.getProfileDao().updateActive(true,profile.id);

                    Context context = getApplicationContext();
                    CharSequence text = "Valittu profiili:  " + profile.name;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();

                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Käytössä olevaa profiilia ei voi poistaa.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
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