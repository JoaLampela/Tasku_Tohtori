package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
    boolean delete;
    Profile profile;
    Toast toast;
    DataBaseManager DBM;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        DBM = new DataBaseManager(this);
        newProfile = findViewById(R.id.newProfileButton);
        backButton = findViewById(R.id.backButtonResultScreen);
        deleteSwitch = findViewById(R.id.deleteSwitch);
        profileList = findViewById(R.id.profileList);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        updateProfileList();

        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                delete = isChecked;
                if(delete) {
                    deleteSwitch.setText(R.string.profile_delete_on);
                    updateToast("Klikkaa profiilia poistaaksesi sen.");
                } else {
                    deleteSwitch.setText(R.string.profile_delete);
                }
            }
        });

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                profile = (Profile) adapterView.getAdapter().getItem((int) l);

                if (!(profile.active) && delete && DBM.getSizeOfProfileList() > 1) {
                    DBM.deleteProfile(profile);
                    updateProfileList();

                    updateToast("Profiili poistettu:  " + profile.name);

                } else if(!delete) {
                    DBM.updateAllProfilesToFalse();
                    DBM.updateProfileToActive(profile.id);
                    updateProfileList();

                } else {
                    updateToast("Käytössä olevaa profiilia ei voi poistaa.");
                }

            }
        });
    }

    public void updateToast(String string) {
        toast.cancel();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setText(string);
        toast.show();
    }

    public void updateProfileList() {
        profileList.setAdapter(new ProfileListAdapter(this,R.layout.profile_adapter_view_layout, DBM.getAllProfiles()));
    }

    public void newProfile(View view) {
        newProfile.setClickable(false);
        Intent intent = new Intent(ProfilesActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void backToMain(View view) {
        backButton.setClickable(false);
        finish();
    }
}