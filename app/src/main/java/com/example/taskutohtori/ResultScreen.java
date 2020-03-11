package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity {
    TextView resultText;

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
}
