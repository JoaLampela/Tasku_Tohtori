package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity {
    TextView resultText;
    Button againButton;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Intent intent = getIntent();
        String message = intent.getStringExtra(PlayActivity.EXTRA_MESSAGE);
        resultText = findViewById(R.id.resultText);
        printresult(message);
    }

    public void printresult(String message) {
        if(message.equals("Cured")) {
            resultText.setText(getString(R.string.result_unknown));
        }
        else {
            resultText.setText(getString(R.string.result) + message);
        }
    }

    public void onAgainButtonClick(View view) {
        Intent again = new Intent(this, PlayActivity.class);
        startActivity(again);
        finish();
    }

    public void onBackButtonClick(View view) {
        finish();
    }

    private void setDelay(int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        },delay);
    }
}
