package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * splashscreen for app
 * @author Max Kaarla
 * @version 1.0
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
                finish();
            }
        },3000);
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
