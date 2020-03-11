package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * this activity declares result of PlayActivity
 * @author Max Kaarla
 * @author Jarno Tiainen (layout)
 * @version 1.0
 */
public class ResultScreen extends AppCompatActivity {
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        Intent intent = getIntent();
        String message = intent.getStringExtra(PlayActivity.EXTRA_MESSAGE);
        resultText = findViewById(R.id.resultText);
        assert message != null;
        printResult(message);
    }

    /**
     * sets result textView's text based on message
     * @param message text for result textView
     */
    @SuppressLint("SetTextI18n")
    public void printResult(String message) {
        if (message.equals("Cured")) {
            resultText.setText(getString(R.string.result_unknown));
        } else {
            resultText.setText(getString(R.string.result) + message);
        }
    }
}
