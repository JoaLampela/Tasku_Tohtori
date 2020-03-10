package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private ArrayList<String> askedSymptoms;
    private ArrayList<String> finalSymptoms;
    private HashMap<String, Float> powerMap;
    private ArrayList<String> listOfAllMainSymptoms;

    private String currentSymptom;
    private String result;
    boolean allMainQuestionsAsked;
    boolean declareDisease;
    boolean firstTime;


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
        listOfAllMainSymptoms = DBM.getAllMainSymptoms();
        askedSymptoms = new ArrayList<>();
        finalSymptoms = new ArrayList<>();
        powerMap = new HashMap<>();

        allMainQuestionsAsked = false;
        declareDisease = false;
        firstTime = true;

        removeImpossibleDiseases();

        createPower();
        newQuestion();
        updateUI();
    }

    public void onYesButtonClick(View v) {
        yesButton.setClickable(false);
        noButton.setClickable(false);
        allMainQuestionsAsked = true;
        newQuestion();
        updateUI();
    }

    public void removeImpossibleDiseases() {
        for(int i= 0; i < listOfAllDiseases.size(); i++) {
            if(DBM.getIsMale()) {
                if (DBM.getSexBias(listOfAllDiseases.get(i)) <= -1.0) {
                    listOfAllDiseases.remove(listOfAllDiseases.get(i));
                }
            }
            else {
                if (DBM.getSexBias(listOfAllDiseases.get(i)) >= 1.0) {
                    listOfAllDiseases.remove(listOfAllDiseases.get(i));
                }
            }

        }
    }

    public void onNoButtonClick(View v) {
        yesButton.setClickable(false);
        noButton.setClickable(false);
        if (!allMainQuestionsAsked) {
            removeDiseases(currentSymptom);
            listOfAllMainSymptoms.remove(currentSymptom);
            Log.d("TEST","listOfAllMainSymptoms = "+ listOfAllMainSymptoms);
        }
        newQuestion();
        updateUI();
    }

    private void newQuestion() {
        if (!allMainQuestionsAsked) {
            currentSymptom = newMainQuestion();
        } else {
            if (firstTime) {
                Log.d("TEST","first time");
                removeDiseasesWithoutMainSymptom(currentSymptom);
                firstTime = false;
                createFinalMainSymptomList();
            }
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
        String nextSymptom= null;
        if (listOfAllMainSymptoms.isEmpty()) {
            Log.d("TEST","Cured");
            return "Cured";
        }
        nextSymptom = listOfAllMainSymptoms.get(0);
        return nextSymptom;
    }

    private String newRareQuestion() {
        if (!finalSymptoms.isEmpty()) {
            currentSymptom = finalSymptoms.get(0);
            finalSymptoms.remove(finalSymptoms.get(0));
            return currentSymptom;
        }
        return "Stop";
    }

    //to be called after each button press(kyllä, ei)
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
        new Handler().postDelayed(new Runnable() {
            public void run() {
                yesButton.setClickable(true);
                noButton.setClickable(true);
            }
        },100);


    }

    //to be called after user answers no to the question
    private void removeDiseases(String currentSymptom) {
        Log.d("TEST", "removeDisease");
        askedSymptoms.add(currentSymptom);
        for (int i = 0; i < DBM.getSizeOfDiseases(currentSymptom); i++) {
            String thisDisease = DBM.getDiseases(currentSymptom).get(i);
            if (DBM.getMainSymptoms(thisDisease).contains(currentSymptom)) {
                listOfAllDiseases.remove(thisDisease);
                Log.d("TEST","removed "+thisDisease);
            }

        }
    }
    private void removeDiseasesWithoutMainSymptom(String currentSymptom) {
        listOfAllMainSymptoms = new ArrayList<>();
        listOfAllMainSymptoms.add(currentSymptom);
        askedSymptoms.add(currentSymptom);
        for (int i = 0; i < DBM.getAllDiseases().size(); i++) {
            String thisDisease = DBM.getAllDiseases().get(i);
            if (!DBM.getMainSymptoms(thisDisease).contains(currentSymptom)) {
                listOfAllDiseases.remove(thisDisease);
                Log.d("TEST","removed1 "+thisDisease);
            }
        }
    }


    //gives power values for each disease
    private void createPower() {
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            powerMap.put(listOfAllDiseases.get(i), (float) 0.0);
        }
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
        int ageBias = DBM.getAgeBias(disease);
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
        return (float) 1.0/(abs((ageGroup - ageBias)) + 1);
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
            for (int j = 0; j < DBM.getRareSymptoms(listOfAllDiseases.get(i)).size(); j++) {
                if (!askedSymptoms.contains(DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j))) {
                    if (!finalSymptoms.contains(DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j))) {
                        finalSymptoms.add(DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j));
                    }
                }
            }
        }
        Log.d("TEST","llllll "+finalSymptoms);
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
