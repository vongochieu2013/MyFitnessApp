package com.example.myworkoutapplication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseManager {

  public FirebaseFirestore store;
  public FirebaseAuth auth;

  public FirebaseManager(FirebaseAuth auth, FirebaseFirestore store) {
    this.store = store;
    this.auth = auth;
  }

  public Task<AuthResult> create(User user) {
    return auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
  }
}
