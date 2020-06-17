package com.example.myworkoutapplication;

public class User {

  private String email;
  private String fullName;
  private String password;
  private int age;
  private int height;
  private int weight;


  public User() {

  }

  public User(String email, String fullName, String password, int age, int weight, int height) {
    this.email = email;
    this.fullName = fullName;
    this.password = password;
    this.age = age;
    this.weight = weight;
    this.height = height;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getfullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }
}
