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

import static java.lang.StrictMath.abs;

public class PlayActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    DatabaseT database;
    DataBaseManager DBM;

    Button yesButton;
    Button noButton;
    TextView question;
    ImageManager thisImageManager;

    ArrayList<String> listOfAllDiseases;
    ArrayList<String> positiveSymptoms;
    ArrayList<String> askedSymptoms;
    ArrayList<String> finalSymptoms;
    HashMap<String, Float> powerMap;

    String currentSymptom;
    String result;
    boolean allMainQuestionsAsked;
    boolean declareDisease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        database = Room.databaseBuilder(this, DatabaseT.class, "Database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        DBM = new DataBaseManager(this);

        //only for testing
        createDiseases();

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
        if(!allMainQuestionsAsked) {
            removeDiseases(currentSymptom);
        }
        newQuestion();
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
            for(int i = 0; i < DBM.getSizeOfMainSymptoms(nextDisease); i++) {
                if (!positiveSymptoms.contains(DBM.getMainSymptoms(nextDisease).get(i))) {
                    return DBM.getMainSymptoms(nextDisease).get(i);
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
            Intent intent = new Intent(PlayActivity.this, ResultScreen.class);
            intent.putExtra(EXTRA_MESSAGE, result);
            startActivity(intent);
            finish();
        }
    }

    //to be called after user answers no to the question
    private void removeDiseases(String currentSymptom) {
        Log.d("TEST","removeDisease");

        askedSymptoms.add(currentSymptom);
        for (int i = 0; i < DBM.getSizeOfDiseases(currentSymptom); i++) {
            String thisDisease = DBM.getDiseases(currentSymptom).get(i);
            if(DBM.getMainSymptoms(thisDisease).contains(currentSymptom))
            {
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
    public void createPower() {
        for(int i = 0; i < listOfAllDiseases.size(); i++) {
            powerMap.put(listOfAllDiseases.get(i), (float) 0.0);
        }
    }

    //updates disease's power
    public void updatePower(String thisDisease) {
        int containedSymptoms = 0;
        for (int i = 0; i < DBM.getSizeOfMainSymptoms(thisDisease); i++) {
            if (askedSymptoms.contains(DBM.getMainSymptoms(thisDisease).get(i))) {
                containedSymptoms++;
            }
        }
        powerMap.put(thisDisease, (float) containedSymptoms/DBM.getSizeOfMainSymptoms(thisDisease));
    }

    //need to add age and sex bonuses back
    public void updateFinalPower(String askedDisease) {

        int containedSymptoms = 0;
        ArrayList<String> allSymptoms = new ArrayList<>();
        for(int i = 0; i < DBM.getSizeOfSymptoms(askedDisease); i++) {
            allSymptoms.add(DBM.getSymptoms(askedDisease).get(i));
        }
        for (int i = 0; i < askedSymptoms.size(); i++) {
            if (allSymptoms.contains(askedSymptoms.get(i))) {
                containedSymptoms++;
            } else {
                containedSymptoms--;
            }
        }
        powerMap.put(askedDisease, (float) containedSymptoms * ageBonus(askedDisease) * sexBonus(askedDisease) / allSymptoms.size());
    }

    //calculates ageBonus for updateFinalPower
    private float ageBonus(String disease) {
        float ageBias = DBM.getAgeBias(disease);
        int age = DBM.getAge();
        int ageGroup = 0;

        if (ageBias==0) {
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
        return abs(ageGroup-ageBias)+1;
    }

    //calculates sexBonus for updteFinalPower
    private float sexBonus(String disease) {
        boolean isMale = DBM.getIsMale();
        float sexBias = DBM.getSexBias(disease);
        if (isMale) {
            return abs(sexBias + 1);
        }
        return  abs(sexBias - 1);
    }

    private void createFinalMainSymptomList() {
        for(int i = 0; i < listOfAllDiseases.size(); i++) {
            for(int j = 0; j < DBM.getSizeOfSymptoms(listOfAllDiseases.get(i)); j++) {
                if(!askedSymptoms.contains(DBM.getSymptoms(listOfAllDiseases.get(i)).get(j))) {
                    finalSymptoms.add(DBM.getSymptoms(listOfAllDiseases.get(i)).get(j));
                }
            }
        }
    }

    private void calculateResult() {
        float bestResult = 0;
        for (int i = 0; i < listOfAllDiseases.size(); i++) {
            updateFinalPower(listOfAllDiseases.get(i));
            if(powerMap.get(listOfAllDiseases.get(i)) > bestResult) {
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

        database.getDiseaseDao().insertDisease(new Disease("Koronavirus", 0, 0));

        database.getMainSymptomDao().insertMainSymptom(new MainSymptom("Yskä"));
        database.getMainSymptomDao().insertMainSymptom(new MainSymptom("Lihaskipu"));
        database.getMainSymptomDao().insertMainSymptom(new MainSymptom("Nopeasti nouseva kuume"));

        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Veriyskä"));
        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Hengenahdistus"));
        database.getRareSymptomDao().insertRareSymptom(new RareSymptom("Nuha"));

        database.getSymptomDao().insertSymptom(new Symptom("Nopeasti nouseva kuume"));
        database.getSymptomDao().insertSymptom(new Symptom("Veriyskä"));
        database.getSymptomDao().insertSymptom(new Symptom("Hengenahdistus"));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Kuume"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Kuume"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Nuha"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Nuha"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Lihaskipu"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Lihaskipu"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Yskä"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Yskä"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Nopeasti nouseva kuume"),
                        database.getMainSymptomDao().getMainSymptomIdWithName("Nopeasti nouseva kuume"), null));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Nuha"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Nuha")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Hengenahdistus"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Hengenahdistus")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Veriyskä"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Veriyskä")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Tukkoisuus"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Tukkoisuus")));

        database.getJoinerDao().insertJoinerValue(new Joiner
                (database.getDiseaseDao().getDiseaseIdWithName("Koronavirus"),
                        database.getSymptomDao().getSymptomIdWithName("Kurkkukipu"), null,
                        database.getRareSymptomDao().getRareSymptomIdWithName("Kurkkukipu")));
    }
}
