package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;

/**
 * An activity where the user can create a new profile.
 * @author Jarno Tiainen
 * @version 1.0
 */

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
    Toast toast;

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
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    /**
     * Validation method for the name input. Sets errors in the EditText and returns a boolean
     * for the onClickCreateUser method that tells if the name can be used.
     * @return boolean true or false depending on if the name is acceptable
     */
    public boolean checkName() {
        String nameGet = nameInput.getEditText().getText().toString().trim();
        if (nameGet.isEmpty()) {
            nameInput.setError("Nimi ei voi olla tyhjä");
            return false;

        } else if(nameGet.length() > 20) {
            nameInput.setError("Nimi on liian pitkä");
            return false;

        } else {
            nameInput.setError(null);
            name = nameGet;
            return true;
        }
    }

    /**
     * Validation method for the age input. Sets errors in the EditText and returns a boolean
     * for the onClickCreateUser method that tells if the age can be used.
     * @return boolean true or false depending on if the age is acceptable
     */
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

    /**
     * Validation method for the sex input. Shows a toast prompting the user to choose a sex and
     * returns a boolean for the onClickCreateUser method that tells if an option has been
     * checked or not.
     * @return boolean true or false depending on if an option has been checked or not
     */
    public boolean checkSex() {
        if (!maleButton.isChecked() & !femaleButton.isChecked()) {
            toast.cancel();
            CharSequence text = "Valitse sukupuoli";
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 400);
            toast.show();
            return false;

        } else {
            male = maleButton.isChecked();
            return true;
        }
    }

    /**
     * Creates a new profile and sets it as the active profile when the user clicks the button, but
     * only if all the validation methods return a true value. Also disables the button so the user
     * can't spam click it and create multiples of the same profile.
     * Else updates the errors shown on screen by calling the validation methods and also
     * re-enables the button to be clickable.
     * @param view view in CreateProfileActivity layout
     */
    public void onClickCreateUser(View view) {
        confirmButton.setClickable(false);

        if (!checkName() | !checkAge() | !checkSex()) {
            confirmButton.setClickable(true);

        } else {
            DBM.updateAllProfilesToFalse();
            DBM.addNewProfile(name, age, male);
            finish();
        }

    }
}