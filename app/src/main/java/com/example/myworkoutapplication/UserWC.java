package com.example.myworkoutapplication;

import java.util.Date;

public class UserWC {
    private Date date;
    private String workoutType;


    public UserWC() {
        // no arg constructor
    }

    public UserWC(String workoutType, Date date) {
        this.date = date;
        this.workoutType = workoutType;

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }



}