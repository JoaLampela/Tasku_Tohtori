package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button profilesButton;
    Button exitButton;
    TextView currentProfileTV;
    DatabaseT database;
    boolean returning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.playButton);
        profilesButton = findViewById(R.id.profilesButton);
        exitButton = findViewById(R.id.exitButton);
        currentProfileTV = findViewById(R.id.current_profile_text);
        database = Room.databaseBuilder(MainActivity.this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        returning = false;

        //Starts profile creation activity if the app has not been opened before
        Intent firstProfileActivity = new Intent(MainActivity.this, CreateProfileActivity.class);
        if (firstStartCheck()) {
            SharedPreferences prefs = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = prefs.edit();
            prefEditor.putBoolean("check", false);
            prefEditor.commit();
            startActivity(firstProfileActivity);
            returning = true;
        }

        if (!returning) {
            updateCurrentProfileText();
        }
    }
    public void onPlayButtonClick(View v) {
        startActivity(new Intent(this,PlayActivity.class));
        returning = true;
    }

    public void onProfilesButtonClick(View v) {
        startActivity(new Intent(this,ProfilesActivity.class));
        returning = true;
    }
    public void onExitButtonClick(View V) {
        finish();
        System.exit(0);
    }

    //Checking if the app has been launched before from a boolean value saved to SharedPreferences
    public boolean firstStartCheck() {
        SharedPreferences prefs = getSharedPreferences("Prefs", Activity.MODE_PRIVATE);
        return prefs.getBoolean("check", true);
    }

    public void updateCurrentProfileText() {
        currentProfileTV.setText(database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).name);
    }

    /*public void updateCurrentProfileText() {
        Log.d("tägi", "updateCurrentProfileText called");
        currentProfileTV.setText(database.getProfileDao().getProfileNamesWithId(currentProfile()).get(0));
        Log.d("tägi", "updateCurrentProfileText finished");
    }*/

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
        Log.d("tägi", "Main onResume finished");
    }
}
