package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;


public class CreateProfileActivity extends AppCompatActivity {
    String name;
    int age;
    boolean male;
    TextInputLayout nameInput;
    TextInputLayout ageInput;
    RadioButton maleButton;
    RadioButton femaleButton;
    Button confirmButton;
    DataBaseManager DBM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        nameInput = findViewById(R.id.name_input_layout);
        ageInput = findViewById(R.id.age_input_layout);
        maleButton = findViewById(R.id.maleButton);
        femaleButton = findViewById(R.id.femaleButton);
        confirmButton = findViewById(R.id.confirmButton);
        DBM = new DataBaseManager(this);

    }

    public boolean checkName() {
        String nameGet = nameInput.getEditText().getText().toString().trim();
        if (nameGet.isEmpty()) {
            nameInput.setError("Nimi ei voi olla tyhjä");
            return false;

        } else  if(nameGet.length() > 20) {
            nameInput.setError("Nimi on liian pitkä");
            return false;

        } else {
            nameInput.setError(null);
            name = nameGet;
            return true;
        }
    }

    public boolean checkAge() {
        String ageGet = ageInput.getEditText().getText().toString().trim();
        if (ageGet.isEmpty()) {
            ageInput.setError("Ikä ei voi olla tyhjä");
            return false;

        } else if(Integer.parseInt(ageGet) >= 100 | Integer.parseInt(ageGet) < 0) {
            ageInput.setError("Valitse ikä väliltä 0-99");
            return false;

        } else {
            ageInput.setError(null);
            age = Integer.parseInt(ageGet);
            return true;
        }
    }

    public boolean checkSex() {
        if (!maleButton.isChecked() & !femaleButton.isChecked()) {

            Context context = getApplicationContext();
            CharSequence text = "Valitse sukupuoli";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 400);
            toast.show();
            return false;

        } else {
            male = maleButton.isChecked();
            return true;
        }
    }

    public void onClickCreateUser(View view) {
        confirmButton.setClickable(false);

        if (!checkName() | !checkAge() | !checkSex()) {
            confirmButton.setClickable(true);
            return;

        } else {
            DBM.updateAllProfilesToFalse();
            DBM.addNewProfile(name, age, male, true);
            finish();
        }
    }
}