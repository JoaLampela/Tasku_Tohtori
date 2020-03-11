package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MainActivity for the Taskutohtori app.
 * @author Jarno Tiainen
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button profilesButton;
    Button exitButton;
    ImageButton infoButton;
    TextView currentProfileTV;
    DatabaseT database;
    boolean noProfiles;
    ImageView profileImage;
    Toast toast;

    /**
     * Checks whether the profile database is empty when the app is started. If empty, it starts
     * CreateProfileActivity. Else, it displays the profile with the attribute active set to true
     * on screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.playButton);
        profilesButton = findViewById(R.id.profilesButton);
        exitButton = findViewById(R.id.exitButton);
        infoButton = findViewById(R.id.infoButton);
        currentProfileTV = findViewById(R.id.current_profile_text);
        profileImage = findViewById(R.id.profileImage);
        database = Room.databaseBuilder(MainActivity.this, DatabaseT.class, "Database").createFromAsset("database/Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        noProfiles = false;
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        Intent createProfileActivity = new Intent(MainActivity.this, CreateProfileActivity.class);
        if (database.getProfileDao().getAllProfiles().size() == 0) {
            startActivity(createProfileActivity);
            noProfiles = true;
        }

        if (!noProfiles) {
            updateCurrentProfileText();
        }
    }

    /**
     * Called when the play button is clicked. First disables all buttons in MainActivity so the
     * user can't open multiple activities by spam clicking the buttons. Then, if the profile
     * database has at least one profile, it starts PlayActivity. Else, it shows a toast
     * prompting the user to first make a profile.
     * @param v
     */
    public void onPlayButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        if (database.getProfileDao().getAllProfiles().size() >=1) {
            startActivity(new Intent(this,PlayActivity.class));
            setDelay(100);
        } else {
            toast.cancel();
            CharSequence text = "Luo ensin profiili.";
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 750);
            toast.show();
            setDelay(100);
        }
    }

    /**
     * Called when the play button is clicked. First disables all buttons in MainActivity so the
     * user can't open multiple activities by spam clicking the buttons. Then it starts
     * ProfilesActivity.
     * @param v
     */
    public void onProfilesButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        startActivity(new Intent(this,ProfilesActivity.class));
        setDelay(100);
    }

    /**
     * Called when the play button is clicked. First disables all buttons in MainActivity so the
     * user can't open multiple activities by spam clicking the buttons. Then it closes
     * MainActivity and exits the app.
     * @param v
     */
    public void onExitButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        finish();
        System.exit(0);
    }

    /**
     * Called when the play button is clicked. First disables all buttons in MainActivity so the
     * user can't open multiple activities by spam clicking the buttons. Then it starts
     * InfoActivity.
     * @param v
     */
    public void onInfoButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        setDelay(100);
        startActivity(new Intent(this,InfoActivity.class));
    }

    /**
     * When called, changes the displayed profile name to that profiles name, which currently has
     * the active attribute set to true. Also changes the little icon next to the displayed profile
     * name depending on the profiles sex.
     */
    public void updateCurrentProfileText() {
        if (database.getProfileDao().getAllProfiles().size() >= 1) {
            currentProfileTV.setText(database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).name);
            if(database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).male) {
                profileImage.setImageResource(R.drawable.profile_male);
            } else {
                profileImage.setImageResource(R.drawable.profile_female);
            }
        }
    }

    /**
     * Calls the updateCurrentProfileText method when MainActivity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateCurrentProfileText();
    }

    /**
     * Re-enables the buttons in MainActivity to be clickable after a specified delay.
     * @param delay
     */
    private void setDelay(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                profilesButton.setClickable(true);
                playButton.setClickable(true);
                exitButton.setClickable(true);
                infoButton.setClickable(true);
            }
        },delay);
    }
}
