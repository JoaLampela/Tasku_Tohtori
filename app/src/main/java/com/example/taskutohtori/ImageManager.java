package com.example.taskutohtori;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

//this object manages changing images of doctor and possibly other images as well if implemented

public class ImageManager extends AppCompatActivity {

    int currentIndex;

    private ArrayList<Integer> doctorImages;

    public ImageManager(){
        this.currentIndex = 0;
        this.doctorImages = new ArrayList<>();
        this.doctorImages.add(R.drawable.dohdori1_scaled);
        this.doctorImages.add(R.drawable.dohdori2_scaled);
        this.doctorImages.add(R.drawable.dohdori3_scaled);
        this.doctorImages.add(R.drawable.dohdori4_scaled);
        this.doctorImages.add(R.drawable.dohdori5_scaled);
        this.doctorImages.add(R.drawable.dohdori6_scaled);
        this.doctorImages.add(R.drawable.dohdori7_scaled);
    }


    public int updateImage() {
        int nextImageIndex = getRandomNumberInRange();
        if(nextImageIndex != currentIndex) {
            this.currentIndex = nextImageIndex;
            doctorImages.get(nextImageIndex);
            return doctorImages.get(nextImageIndex);
        }
        return updateImage();
    }
    private int getRandomNumberInRange() {
        Random r = new Random();
        return r.nextInt(this.doctorImages.size());
    }
}
