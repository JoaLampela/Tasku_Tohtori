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
        playButton.setClickable(false);
        if (database.getProfileDao().getAllProfiles().size() >=1) {
            startActivity(new Intent(this,PlayActivity.class));
            returning = true;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Luo ensin profiili.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();
        }
        setDelay(100,playButton);
    }

    public void onProfilesButtonClick(View v) {
        profilesButton.setClickable(false);
        startActivity(new Intent(this,ProfilesActivity.class));
        returning = true;
        setDelay(100,profilesButton);
    }
    public void onExitButtonClick(View v) {
        exitButton.setClickable(false);
        finish();
        System.exit(0);
        setDelay(100,exitButton);
    }

    public void onInfoButtonClick(View v) {
        infoButton.setClickable(false);
        startActivity(new Intent(this,InfoActivity.class));
        setDelay(100,infoButton);
    }

    public void updateCurrentProfileText() {
        if (database.getProfileDao().getAllProfiles().size() >= 1) {
            currentProfileTV.setText(database.getProfileDao().getAllProfilesWithActiveStatus(true).get(0).name);
            profileImage.setImageResource(R.drawable.profile_image);
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
    private void setDelay(int delay, final View thisButton) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                thisButton.setClickable(true);
            }
        },delay);
    }
}
