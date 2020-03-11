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

/**
 * This class takes care of this apps play function
 * uses ImageManeger and DataBaseManager
 * @see ImageManager
 * @see DataBaseManager
 * @author Max Kaarla
 * @author Jarno Tiainen (layout)
 * @version 1.0
 */
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
    private boolean allMainQuestionsAsked;
    private boolean declareDisease;
    private boolean firstTime;


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

    /**
     * This method is called when yes button is clicked and calls NewQuestion and updateUi methods.
     * It locks buttons so that they can't be double clicked.
     * @param v view of the app
     */
    public void onYesButtonClick(View v) {
        if(allMainQuestionsAsked) {
            askedSymptoms.add(currentSymptom);
        }
        yesButton.setClickable(false);
        noButton.setClickable(false);
        allMainQuestionsAsked = true;
        newQuestion();
        updateUI();
    }

    /**
     * This method removes all diseases that person can't have based on his/her gender.
     */
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

    /**
     * Is called No-button is clicked. Removes all diseases that have the asked mainSymptom.
     * When all main questions have been asked this method only updates Ui
     * and calls for NnewQuestion method
     * Calls newQuestion and updateUi methods
     * @param v view of the app
     */
    public void onNoButtonClick(View v) {
        yesButton.setClickable(false);
        noButton.setClickable(false);
        if (!allMainQuestionsAsked) {
            listOfAllMainSymptoms.remove(currentSymptom);
            Log.d("TEST","listOfAllMainSymptoms = "+ listOfAllMainSymptoms);
        }
        newQuestion();
        updateUI();
    }

    /**
     * Decides what will be the next questioned symptom and
     * if currentSymptom = "Stop" this method calls calculateResult and sets declareDisease to true
     * if currentSymptom = "Cured" this method only sets declareDisease to true
     */
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

    /**
     * returns next main symptoms name for the next question.
     * Returns "Cured" if listOfAllMainSymptoms is empty
     * @return next symptom name or "Cured"
     */
    private String newMainQuestion() {
        String nextSymptom;
        if (listOfAllMainSymptoms.isEmpty()) {
            return "Cured";
        }
        nextSymptom = listOfAllMainSymptoms.get(0);
        return nextSymptom;
    }

    /**
     * returns next main symptoms name for the next question
     * or "Stop" if all finalSymptoms list is empty.
     * @return next symptom name or "Stop"
     */
    private String newRareQuestion() {
        if (!finalSymptoms.isEmpty()) {
            currentSymptom = finalSymptoms.get(0);
            finalSymptoms.remove(finalSymptoms.get(0));
            return currentSymptom;
        }
        return "Stop";
    }

    /**
     * Updates question text and changes doctor image with thisImageManager object
     * Starts ResultScreen activity if declareDisease = true
     */
    private void updateUI() {
        ImageView doctorImage = findViewById(R.id.doctorImage);
        doctorImage.setImageResource(thisImageManager.updateImage());
        if (!declareDisease) {
            question.setText("Kuuluuko oireisiisi " + currentSymptom.toLowerCase() + "?");
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

    /**
     * removes all diseases that don't have parameter's main symptom
     * @param currentSymptom currently asked mainSymptom
     */
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

    /**
     * creates hashmap that contains Disease names and their powers
     * power is diseases probability based on users answers
     */
    private void createPower() {
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            powerMap.put(listOfAllDiseases.get(i), (float) 0.0);
        }
    }

    /**
     * Calculates parameter's diseases finalPower and updates it to power hasmap.
     * Uses ageBonus and sexBonus methods
     * @param askedDisease Disease's name
     */
    private void updateFinalPower(String askedDisease) {

        float containedSymptoms = 0;
        ArrayList<String> allSymptoms = new ArrayList<>();
        for (int i = 0; i < DBM.getSizeOfSymptoms(askedDisease); i++) {
            allSymptoms.add(DBM.getSymptoms(askedDisease).get(i));
        }
        Log.d("TEST","askedSymptoms "+askedSymptoms);
        for (int i = 0; i < askedSymptoms.size(); i++) {
            if (allSymptoms.contains(askedSymptoms.get(i))) {
                Log.d("TEST",""+askedDisease+" "+containedSymptoms);
                containedSymptoms++;
            } else {
                containedSymptoms--;
                Log.d("TEST",""+askedDisease+" "+containedSymptoms);
            }
        }
        int balance = 5;
        powerMap.put(askedDisease,
                (containedSymptoms + balance*
                        ageBonus(askedDisease) + balance *
                        sexBonus(askedDisease)));
        Log.d("TEST","power = "+containedSymptoms +" "+ balance*
                        ageBonus(askedDisease) + " " +balance *
                        sexBonus(askedDisease) +" / " + askedDisease);
    }

    /**
     * Calculates age bonus for updateFinalPower. Return value is max 1 and min 0.2 based on
     * distance between diseases ageBias and users ageGroup.
     * AgeGroup is 1-5. Group 0 = noGroup, group 1 = 0-12, Group 2 = 12-21,
     * Group 3 = 21-40, Group 4 = 40-60, Group 5 60-
     * @param disease disease's name
     * @return float age bonus
     */
    float ageBonus(String disease) {
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

    /**
     * Calculates sexBonus for updateFinalPower method. Return value is max 2.0 and min 0.0.
     * Calculation is based on users sex and diseases sexBonus. Sex bonus
     * is a float number from -1.0-1.0 meaning -1.0 is only females' disease
     * and 1.0 is only males' disease
     * @param disease disease's name
     * @return sexBonus as a float from 0.0 to 2.0
     */
    float sexBonus(String disease) {
        boolean isMale = DBM.getIsMale();
        float sexBias = DBM.getSexBias(disease);
        if (isMale) {
            return abs(sexBias + 1);
        }
        return abs(sexBias - 1);
    }

    /**
     * Adds all remaining diseases' rare symptoms to finalSymptom list
     */
    private void createFinalMainSymptomList() {
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            for (int j = 0; j < DBM.getRareSymptoms(listOfAllDiseases.get(i)).size(); j++) {
                if (!askedSymptoms.contains(DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j))) {
                    if (!finalSymptoms.contains(
                            DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j))
                    ) {
                        finalSymptoms.add(DBM.getRareSymptoms(listOfAllDiseases.get(i)).get(j));
                    }
                }
            }
        }
    }

    /**
     * chooses the best result based on power hashmaps power values
     * and sets bestResults value to be that Diseases name
     */
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
