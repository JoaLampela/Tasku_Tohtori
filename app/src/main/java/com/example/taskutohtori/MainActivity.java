package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button profilesButton;
    Button exitButton;
    ImageButton infoButton;
    TextView currentProfileTV;
    DatabaseT database;
    boolean returning;
    ImageView profileImage;

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
        returning = false;

        //Starts profile creation activity if the app has not been opened before
        Intent createProfileActivity = new Intent(MainActivity.this, CreateProfileActivity.class);
        if (database.getProfileDao().getAllProfiles().size() == 0) {
            startActivity(createProfileActivity);
            returning = true;
        }

        if (!returning) {
            updateCurrentProfileText();
        }
    }
    public void onPlayButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        setDelay(100);
        if (database.getProfileDao().getAllProfiles().size() >=1) {
            startActivity(new Intent(this,PlayActivity.class));
            returning = true;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Luo ensin profiili.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 750);
            toast.show();
        }
    }

    public void onProfilesButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);

        setDelay(100);
        startActivity(new Intent(this,ProfilesActivity.class));
        returning = true;
    }
    public void onExitButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        finish();
        System.exit(0);
        setDelay(100);
    }

    public void onInfoButtonClick(View v) {
        profilesButton.setClickable(false);
        playButton.setClickable(false);
        exitButton.setClickable(false);
        infoButton.setClickable(false);
        setDelay(100);
        startActivity(new Intent(this,InfoActivity.class));
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tägi", "Main onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tägi", "Main onResume called");
        updateCurrentProfileText();
        returning = false;
    }
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
