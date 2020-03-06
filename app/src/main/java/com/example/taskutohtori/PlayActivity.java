package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


public class PlayActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    Button yesButton;
    Button noButton;
    Button unsureButton;
    TextView question;
    ImageManager thisImageManager;

    ArrayList<String> listOfAllDiseases;
    ArrayList<String> positiveSymptoms;
    ArrayList<String> askedSymptoms;
    String currentSymptom;
    ArrayList<String> finalSymptoms;
    Boolean allMainQuestionsAsked;
    String result;
    boolean declareDisease;
    DatabaseT database;
    HashMap<String, Float> powerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        database = Room.databaseBuilder(this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        createDiseases();

        yesButton = findViewById(R.id.yesButton);
        noButton = findViewById(R.id.noButton);
        unsureButton = findViewById(R.id.unsureButton);
        question = findViewById(R.id.question);
        thisImageManager = new ImageManager();
        listOfAllDiseases = (ArrayList<String>) database.getDiseaseDao().getAllDiseaseNames();
        positiveSymptoms = new ArrayList<>();
        askedSymptoms = new ArrayList<>();
        finalSymptoms = new ArrayList<>();
        allMainQuestionsAsked = false;
        declareDisease = false;
        powerMap = new HashMap<>();
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
        Log.d("TEST","allMainsAsked "+allMainQuestionsAsked);
        if(!allMainQuestionsAsked) {
            removeDiseases(currentSymptom);
        }
        newQuestion();
        updateUI();
    }

    public void onUnsureButton(View v) {
        updateUI();
    }

    public void newQuestion() {
        
        if(!allMainQuestionsAsked) {
            currentSymptom = newMainQuestion();
        }
        else {
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

    public String newMainQuestion() {

        float maxPower = 0;
        String nextDisease = null;

        for(int i = 0; i < listOfAllDiseases.size(); i++) {
            float thisPower = powerMap.get(listOfAllDiseases.get(i));
            if (thisPower >= maxPower && thisPower != 1) {
                nextDisease = listOfAllDiseases.get(i);
                maxPower = thisPower;
            }
        }
        if(nextDisease != null) {
            for(int i = 0; i < database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(nextDisease)).size(); i++) {
                Log.d("TEST","name test"+ database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(nextDisease)).get(i).name);
                if (!positiveSymptoms.contains(new Symptom(database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(nextDisease)).get(i).name).name)) {
                    return new Symptom(database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(nextDisease)).get(i).name).name;
                }
            }
        }
        if(listOfAllDiseases.isEmpty()) {
            return "Cured";
        }
        createFinalMainSymptomList();
        allMainQuestionsAsked = true;
        return (newRareQuestion());
    }


    public String newRareQuestion() {
        if(!finalSymptoms.isEmpty()) {
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

        if(!declareDisease) {
            question.setText("Kuuluuko oireisiisi " + currentSymptom + "?");
        }
        else {
            Intent intent = new Intent(this,ResultScreen.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
            finish();
        }
    }

    //to be called after user answers no to the question
    private void removeDiseases(String currentSymptom) {
        Log.d("TEST","removeDisease");

        askedSymptoms.add(currentSymptom);
        Log.d("TEST","curren Symptom" +currentSymptom);
        Log.d("TEST","curren Symptom" +database.getJoinerDao().getDiseasesWithSymptomName(currentSymptom));
        Log.d("TEST","lenght" + database.getJoinerDao().getDiseasesWithSymptomName(currentSymptom).size());
        for (int i = 0; i < database.getJoinerDao().getDiseasesWithSymptomName(currentSymptom).size(); i++) {
            String thisDisease = database.getJoinerDao().getDiseasesWithSymptomName(currentSymptom).get(i);
            Log.d("TEST","mainSymptoms = "+database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(thisDisease)));
            Log.d("TEST","currentSymptom = "+currentSymptom);
            if(database.getJoinerDao().getMainSymptomNamesWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(thisDisease)).contains(currentSymptom))
            {
                Log.d("TEST","removing" + thisDisease);
                listOfAllDiseases.remove(thisDisease);
            }

        }
    }

    //to Be called after user answers yes to the question
    private void increaseDiseasePower(String currentSymptom) {
        positiveSymptoms.add(currentSymptom);
        askedSymptoms.add(currentSymptom);
        Log.d("TEST","positive symptoms ="+positiveSymptoms);
        Log.d("TEST","asked symptoms " +askedSymptoms);
        for (int i = 0; i < database.getJoinerDao().getDiseasesWithSymptomId(database.getDiseaseDao().getDiseaseIdWithName(currentSymptom)).size(); i++) {
            String thisDisease = database.getJoinerDao().getDiseasesWithSymptomId(database.getDiseaseDao().getDiseaseIdWithName(currentSymptom)).get(i).name;
            updatePower(thisDisease);
        }
    }

    public void createPower() {

        for(int i = 0; i < listOfAllDiseases.size(); i++) {
            powerMap.put(listOfAllDiseases.get(i), (float) 0.0);
        }
    }

    public void updatePower(String thisDisease) {

        int containedSymptoms = 0;
        for (int i= 0; i < database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(thisDisease)).size();i++) {
            if (askedSymptoms.contains(new Symptom(database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(thisDisease)).get(i).name).name)) {
                containedSymptoms++;
            }
        }
        powerMap.put(thisDisease, (float) containedSymptoms/database.getJoinerDao().getMainSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(thisDisease)).size());
    }

    public void updateFinalPower(String askedDisease) {

        int containedSymptoms = 0;
        ArrayList<String> allSymptoms = new ArrayList<>();
        for(int i= 0; i < database.getJoinerDao().getSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(askedDisease)).size(); i++) {
            allSymptoms.add(database.getJoinerDao().getSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(askedDisease)).get(i).name);
        }


        for (int i = 0; i < askedSymptoms.size(); i++) {
            if (allSymptoms.contains(askedSymptoms.get(i))) {
                containedSymptoms++;
            } else {
                containedSymptoms--;
            }
        }
        powerMap.put(askedDisease, (float) containedSymptoms / allSymptoms.size());
    }

    private void createFinalMainSymptomList() {
        for(int i = 0; i<listOfAllDiseases.size(); i++) {
            for(int j = 0; j < database.getJoinerDao().getSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(listOfAllDiseases.get(i))).size(); j++) {
                if(!askedSymptoms.contains(new Symptom(database.getJoinerDao().getSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(listOfAllDiseases.get(i))).get(j).name).name)) {
                    finalSymptoms.add(new Symptom(database.getJoinerDao().getSymptomsWithDiseaseId(database.getDiseaseDao().getDiseaseIdWithName(listOfAllDiseases.get(i))).get(j).name).name);
                }
            }
        }
    }

    private void calculateResult() {
        float bestResult = -9001;
        for (int i= 0; i < listOfAllDiseases.size(); i++) {
            updateFinalPower(listOfAllDiseases.get(i));
            if(powerMap.get(listOfAllDiseases.get(i)) >= bestResult) {
                result = listOfAllDiseases.get(i);
                bestResult = powerMap.get(listOfAllDiseases.get(i));
            }
        }
    }

    private void createDiseases() {

        database.clearAllTables();

        database.getDiseaseDao().insertDisease(new Disease("Flunssa", 0, 0));

        database.getMainSymptomDao().insertMainSymptom(new MainSymptom("Kuume"));
        database.getMainSymptomDao().insertMainSymptom(new MainSymptom("Nuha"));

        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Lihaskipu"));
        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Yskä"));
        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Tukkoisuus"));
        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Kurkkukipu"));

        database.getSymptomDao().insertSymptom(new Symptom("Kuume"));
        database.getSymptomDao().insertSymptom(new Symptom("Nuha"));
        database.getSymptomDao().insertSymptom(new Symptom("Lihaskipu"));
        database.getSymptomDao().insertSymptom(new Symptom("Yskä"));
        database.getSymptomDao().insertSymptom(new Symptom("Tukkoisuus"));
        database.getSymptomDao().insertSymptom(new Symptom("Kurkkukipu"));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"), 
                        database.getSymptomDao().getSymptomIdWithName("Kuume"), 
                        database.getMainSymptomDao().getMainSymptomIdWithName("Kuume"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"),
                        database.getSymptomDao().getSymptomIdWithName("Nuha"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Nuha"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"),
                        database.getSymptomDao().getSymptomIdWithName("Lihaskipu"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Lihaskipu")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"),
                        database.getSymptomDao().getSymptomIdWithName("Yskä"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Yskä")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"),
                        database.getSymptomDao().getSymptomIdWithName("Tukkoisuus"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Tukkoisuus")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Flunssa"),
                        database.getSymptomDao().getSymptomIdWithName("Kurkkukipu"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Kurkkukipu")));
    }
}
