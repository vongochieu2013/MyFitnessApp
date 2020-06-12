package com.example.myworkoutapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +       //at least 1 digit
                    //"(?=.*[a-z])" +       //at least 1 lower case letter
                    //"(?=.*[A-Z])" +       //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +  //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 6 characters
                    "$");
    private TextInputLayout emailTextInput;
    private TextInputLayout fullnameTextInput;
    private TextInputLayout passwordTextInput;
    private Button confirmButton;
    private String userID;
    private static final String TAG = "TAG";
    private static final String FULLNAME = "Fullname";
    private static final String EMAIL = "Email";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailTextInput = findViewById(R.id.emailTextInput);
        fullnameTextInput = findViewById(R.id.fullnameTextInput);
        passwordTextInput = findViewById(R.id.passwordTextInput);
        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmail() | !validateName()) {
                    return;
                }
                final String emailInput = emailTextInput.getEditText().getText().toString().trim();
                final String fullnameInput = fullnameTextInput.getEditText().getText().toString().trim();
                String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("users").document(userID);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put(FULLNAME, fullnameInput);
                                    user.put(EMAIL, emailInput);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "On Success: user profile is created for " + userID);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "OnFailure: " + e.toString());
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


    private boolean validateEmail() {
        String emailInput = emailTextInput.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailTextInput.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailTextInput.setError("Please enter a valid email address");
            return false;
        } else {
            emailTextInput.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String fullnameInput = fullnameTextInput.getEditText().getText().toString().trim();
        if (fullnameInput.isEmpty()) {
            fullnameTextInput.setError("Field can't be empty");
            return false;
        } else {
            fullnameTextInput.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            passwordTextInput.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordTextInput.setError("Password must contain at least one alphabet letter and at least 6 characters");
            return false;
        } else {
            passwordTextInput.setError(null);
            return true;
        }
    }
}