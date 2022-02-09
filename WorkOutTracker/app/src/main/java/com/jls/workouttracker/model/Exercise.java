package com.jls.workouttracker.model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Exercise implements Serializable {

    @Exclude
    private String key;
    private String name;
    private String muscle;
    private String img;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Exercise() {
    }

    public Exercise(String name, String muscle) {
        this.name = name;
        this.muscle = muscle;
    }

    public Exercise(String name, String muscle, String img) {
        this.name = name;
        this.muscle = muscle;
        this.img = img;
    }
}

