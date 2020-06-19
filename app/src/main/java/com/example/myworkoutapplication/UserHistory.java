package com.example.myworkoutapplication;

import java.util.Date;

public class UserHistory {
  private Date date;
  private String time;
  private String workoutType;

  public UserHistory() {
    // no arg constructor
  }

  public UserHistory(Date date, String time, String workoutType) {
    this.date = date;
    this.time = time;
    this.workoutType = workoutType;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getWorkoutType() {
    return workoutType;
  }

  public void setWorkoutType(String workoutType) {
    this.workoutType = workoutType;
  }
}
