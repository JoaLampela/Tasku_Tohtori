package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.StrictMath.abs;

public class PlayActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private DataBaseManager DBM;

    Button yesButton;
    Button noButton;
    TextView question;
    ImageManager thisImageManager;

    private ArrayList<String> listOfAllDiseases;
    private ArrayList<String> positiveSymptoms;
    private ArrayList<String> askedSymptoms;
    private ArrayList<String> finalSymptoms;
    private HashMap<String, Float> powerMap;

    private String currentSymptom;
    private String result;
    boolean allMainQuestionsAsked;
    boolean declareDisease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        DBM = new DataBaseManager(this);

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        question = findViewById(R.id.question);
        thisImageManager = new ImageManager();

        listOfAllDiseases = DBM.getAllDiseases();
        positiveSymptoms = new ArrayList<>();
        askedSymptoms = new ArrayList<>();
        finalSymptoms = new ArrayList<>();
        powerMap = new HashMap<>();

        allMainQuestionsAsked = false;
        declareDisease = false;

        createPower();
        newQuestion();
        updateUI();
    }

    public void onYesButtonClick(View v) {
        increaseDiseasePower(currentSymptom);
        newQuestion();
        updateUI();
    }

    public void onNoButtonClick(View v) {
        if (!allMainQuestionsAsked) {
            removeDiseases(currentSymptom);
        }
        newQuestion();
        updateUI();
    }

    private void newQuestion() {

        if (!allMainQuestionsAsked) {
            currentSymptom = newMainQuestion();
        } else {
            currentSymptom = newRareQuestion();
        }
        if (currentSymptom.equals("Stop")) {
            calculateResult();
            declareDisease = true;
        }
        if (currentSymptom.equals("Cured")) {
            declareDisease = true;
            result = "Cured";
        }
    }

    private String newMainQuestion() {

        float maxPower = 0;
        String nextDisease = null;

        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            float thisPower = powerMap.get(listOfAllDiseases.get(i));
            if (thisPower >= maxPower && thisPower != 1) {
                nextDisease = listOfAllDiseases.get(i);
                maxPower = thisPower;
            }
        }
        if (nextDisease != null) {
            for (int i = 0; i < DBM.getSizeOfMainSymptoms(nextDisease); i++) {
                if (!positiveSymptoms.contains(DBM.getMainSymptoms(nextDisease).get(i))) {
                    return DBM.getMainSymptoms(nextDisease).get(i);
                }
            }
        }
        if (listOfAllDiseases.isEmpty()) {
            return "Cured";
        }
        createFinalMainSymptomList();
        allMainQuestionsAsked = true;
        return (newRareQuestion());
    }

    private String newRareQuestion() {
        if (!finalSymptoms.isEmpty()) {
            currentSymptom = finalSymptoms.get(0);
            finalSymptoms.remove(finalSymptoms.get(0));
            return currentSymptom;
        }
        return "Stop";
    }

    //to be called after each button press(kyllä, ei, en osaa sanoa)
    private void updateUI() {
        ImageView doctorImage = findViewById(R.id.doctorImage);
        doctorImage.setImageResource(thisImageManager.updateImage());

        if (!declareDisease) {
            question.setText("Kuuluuko oireisiisi " + currentSymptom + "?");
        } else {
            Intent intent = new Intent(PlayActivity.this, ResultScreen.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
            finish();
        }
    }

    //to be called after user answers no to the question
    private void removeDiseases(String currentSymptom) {
        Log.d("TEST", "removeDisease");

        askedSymptoms.add(currentSymptom);
        for (int i = 0; i < DBM.getSizeOfDiseases(currentSymptom); i++) {
            String thisDisease = DBM.getDiseases(currentSymptom).get(i);
            if (DBM.getMainSymptoms(thisDisease).contains(currentSymptom)) {
                listOfAllDiseases.remove(thisDisease);
            }

        }
    }

    //to Be called after user answers yes to the question
    private void increaseDiseasePower(String currentSymptom) {
        positiveSymptoms.add(currentSymptom);
        askedSymptoms.add(currentSymptom);
        for (int i = 0; i < DBM.getSizeOfDiseases(currentSymptom); i++) {
            String thisDisease = DBM.getDiseases(currentSymptom).get(i);
            updatePower(thisDisease);
        }
    }

    //gives power values for each disease
    private void createPower() {
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            powerMap.put(listOfAllDiseases.get(i), (float) 0.0);
        }
    }

    //updates disease's power
    private void updatePower(String thisDisease) {
        int containedSymptoms = 0;
        for (int i = 0; i < DBM.getSizeOfMainSymptoms(thisDisease); i++) {
            if (askedSymptoms.contains(DBM.getMainSymptoms(thisDisease).get(i))) {
                containedSymptoms++;
            }
        }
        powerMap.put(thisDisease, (float) containedSymptoms / DBM.getSizeOfMainSymptoms(thisDisease));
    }

    //need to add age and sex bonuses back
    private void updateFinalPower(String askedDisease) {

        float containedSymptoms = 0;
        ArrayList<String> allSymptoms = new ArrayList<>();
        for (int i = 0; i < DBM.getSizeOfSymptoms(askedDisease); i++) {
            allSymptoms.add(DBM.getSymptoms(askedDisease).get(i));
        }
        for (int i = 0; i < askedSymptoms.size(); i++) {
            if (allSymptoms.contains(askedSymptoms.get(i))) {
                containedSymptoms++;
            } else {
                containedSymptoms--;
            }
        }
        powerMap.put(askedDisease, (containedSymptoms + abs(containedSymptoms)* ageBonus(askedDisease) + abs(containedSymptoms) * sexBonus(askedDisease)) / allSymptoms.size());
    }

    //calculates ageBonus for updateFinalPower
    private float ageBonus(String disease) {
        float ageBias = DBM.getAgeBias(disease);
        int age = DBM.getAge();
        int ageGroup = 0;

        if (ageBias == 0) {
            return 1;
        }
        if (age > 0) {
            ageGroup = 1;
        }
        if (age > 12) {
            ageGroup = 2;
        }
        if (age > 21) {
            ageGroup = 3;
        }
        if (age > 40) {
            ageGroup = 4;
        }
        if (age > 60) {
            ageGroup = 5;
        }
        return abs(ageGroup - ageBias) + 1;
    }

    //calculates sexBonus for updateFinalPower
    private float sexBonus(String disease) {
        boolean isMale = DBM.getIsMale();
        float sexBias = DBM.getSexBias(disease);
        if (isMale) {
            return abs(sexBias + 1);
        }
        return abs(sexBias - 1);
    }

    private void createFinalMainSymptomList() {
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            for (int j = 0; j < DBM.getSizeOfSymptoms(listOfAllDiseases.get(i)); j++) {
                if (!askedSymptoms.contains(DBM.getSymptoms(listOfAllDiseases.get(i)).get(j))) {
                    if (!finalSymptoms.contains(DBM.getSymptoms(listOfAllDiseases.get(i)).get(j))) {
                        finalSymptoms.add(DBM.getSymptoms(listOfAllDiseases.get(i)).get(j));
                    }
                }
            }
        }
    }

    private void calculateResult() {
        float bestResult = -100;
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            updateFinalPower(listOfAllDiseases.get(i));
            if (powerMap.get(listOfAllDiseases.get(i)) >= bestResult) {
                result = listOfAllDiseases.get(i);
                Log.d("TEST", "Result: " + result);
                bestResult = powerMap.get(listOfAllDiseases.get(i));
            }
        }
    }
}
