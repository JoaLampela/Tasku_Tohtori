package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

/**
 * An activity where the user can choose their profile.
 * @author Jarno Tiainen
 * @see ProfileListAdapter
 * @version 1.0
 */

public class ProfilesActivity extends AppCompatActivity {

    ListView profileList;
    Button newProfile;
    Switch deleteSwitch;
    boolean delete;
    Profile profile;
    Toast toast;
    DataBaseManager DBM;
    Button backButton;

    /**
     * Sets a OnCheckedChangedListener to deleteSwitch and an OnItemClickListener to profileList.
     * @param savedInstanceState a Bundle object containing the activity's previously saved state
     */
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

    /**
     * Used to make a toast on screen when the user interacts with the activity. It first cancels
     * any toasts currently on the screen and then sets a new text to appear in the new toast.
     * The text in the new toast depends on where the user clicked in the activity.
     * @param string text to be showed in the toast
     */
    public void updateToast(String string) {
        toast.cancel();
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 300);
        toast.setText(string);
        toast.show();
    }

    /**
     * Sets the adapter for profileList again when the profile database has changed.
     */
    public void updateProfileList() {
        profileList.setAdapter(new ProfileListAdapter(this,R.layout.profile_adapter_view_layout, DBM.getAllProfiles()));
    }

    /**
     * Called when the new profile creation button is clicked. First disables the button so the
     * user can't click it again and then starts the CreateProfileActivity and closes this activity.
     * @param view view in ProfilesActivity layout
     */
    public void newProfile(View view) {
        newProfile.setClickable(false);
        Intent intent = new Intent(ProfilesActivity.this, CreateProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when the back button is clicked. First disables the button so the user can't click
     * it again and then closes this activity.
     * @param view view in ProfilesActivity layout
     */
    public void backToMain(View view) {
        backButton.setClickable(false);
        finish();
    }
}