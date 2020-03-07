package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class CreateProfileActivity extends AppCompatActivity {
    String name;
    int age;
    boolean male;
    TextInputLayout nameInput;
    TextInputEditText nameInputEdit;
    TextInputLayout ageInput;
    TextInputEditText ageInputEdit;
    RadioButton maleButton;
    RadioButton femaleButton;
    Button confirmButton;
    DatabaseT database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        nameInput = findViewById(R.id.nameInputLayout);
        nameInputEdit = findViewById(R.id.nameInputEditText);
        ageInput = findViewById(R.id.ageInputLayout);
        ageInputEdit = findViewById(R.id.ageInputEditText);
        maleButton = findViewById(R.id.maleButton);
        femaleButton = findViewById(R.id.femaleButton);
        confirmButton = findViewById(R.id.confirmButton);
        database = Room.databaseBuilder(this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        nameInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInputEdit.getText().toString().equals("")) {
                    nameInput.setError("Nimikenttä ei saa olla tyhjä.");
                } else {
                    nameInput.setError(null);
                }
            }
        });

        ageInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ageInputEdit.getText().toString().equals("")) {
                    ageInput.setError("Ikäkenttä ei saa olla tyhjä.");
                } else {
                    ageInput.setError(null);
                }
            }
        });
    }

    public void createUser(View view) {

        if (nameInputEdit.getText().toString().equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Nimikenttä ei saa olla tyhjä.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();

        } else if(ageInputEdit.getText().toString().equals("")) {
            Context context = getApplicationContext();
            CharSequence text = "Ikäkenttä ei saa olla tyhjä.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();

        } else if(!maleButton.isChecked() && !femaleButton.isChecked()) {
            Context context = getApplicationContext();
            CharSequence text = "Valitse sukupuoli";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();

        } else {
            name = nameInputEdit.getText().toString();
            age = Integer.parseInt(ageInputEdit.getText().toString());
            male = maleButton.isChecked();
            database.getProfileDao().updateActiveAllFalse(false);
            database.getProfileDao().insertProfile(new Profile(name, age, male, true));
            finish();
        }
    }
}