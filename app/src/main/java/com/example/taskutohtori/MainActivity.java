package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button playButton;
    Button profilesButton;
    Button exitButton;
    TextView currentProfileTV;
    boolean returning;
    ImageView profileImage;
    DataBaseManager DBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.playButton);
        profilesButton = findViewById(R.id.profilesButton);
        exitButton = findViewById(R.id.exitButton);
        currentProfileTV = findViewById(R.id.current_profile_text);
        profileImage = findViewById(R.id.profileImage);
        DBM = new DataBaseManager(this);
        returning = false;

        //Starts profile creation activity if the app has not been opened before
        Intent createProfileActivity = new Intent(MainActivity.this, CreateProfileActivity.class);
        if (DBM.getSizeOfProfileList() == 0) {
            startActivity(createProfileActivity);
            returning = true;
        }

        if (!returning) {
            updateCurrentProfileText();
        }
    }
    public void onPlayButtonClick(View v) {
        if (DBM.getSizeOfProfileList() >=1) {
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
    }

    public void onProfilesButtonClick(View v) {
        startActivity(new Intent(this,ProfilesActivity.class));
        returning = true;
    }
    public void onExitButtonClick(View V) {
        finish();
        System.exit(0);
    }

    public void updateCurrentProfileText() {
        if (DBM.getSizeOfProfileList() >= 1) {
            currentProfileTV.setText(DBM.getProfileNameWithActiveStatus());
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
}
