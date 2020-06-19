package com.example.myworkoutapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
  private TextInputLayout fullNameTextInput;
  private TextInputLayout passwordTextInput;
  private TextInputLayout ageTextInput;
  private TextInputLayout heightTextInput;
  private TextInputLayout weightTextInput;
  private Button confirmButton;
  public static final String TAG = "TAG";

  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    setUp();
    confirmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!validateEmail() | !validateName() | !validatePassword()) {
          return;
        }
        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        User newUser = setUpUser();
        CollectionReference c = db.collection("users");
        // Log.w(TAG, c.getPath());
        c.document(newUser.getEmail()).set(newUser);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
      }
    });
  }


  public void setUp() {
    emailTextInput = findViewById(R.id.emailTextInput);
    fullNameTextInput = findViewById(R.id.fullnameTextInput);
    passwordTextInput = findViewById(R.id.passwordTextInput);
    confirmButton = findViewById(R.id.confirmButton);
    ageTextInput = findViewById(R.id.ageTextInput);
    heightTextInput = findViewById(R.id.heightTextInput);
    weightTextInput = findViewById(R.id.weightTextInput);
  }

  public boolean validateEmail() {
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

  public boolean validateName() {
    String fullnameInput = fullNameTextInput.getEditText().getText().toString().trim();
    if (fullnameInput.isEmpty()) {
      fullNameTextInput.setError("Field can't be empty");
      return false;
    } else {
      fullNameTextInput.setError(null);
      return true;
    }
  }

  public boolean validatePassword() {
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

  public User setUpUser() {
    final String emailInput = emailTextInput.getEditText().getText().toString().trim();
    final String fullNameInput = fullNameTextInput.getEditText().getText().toString().trim();
    String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
    int age = Integer.parseInt(ageTextInput.getEditText().getText().toString().trim());
    int weight = Integer.parseInt(weightTextInput.getEditText().getText().toString().trim());
    int height = Integer.parseInt(heightTextInput.getEditText().getText().toString().trim());
    return new User(emailInput, fullNameInput, passwordInput, age, weight, height);
  }
}


    /*
 auth
          .createUserWithEmailAndPassword("chan345@gmail.com", "chan@123")
          .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult result) {
              Log.w("hieu", "result=" + result.toString());
              Log.w("hieu", "current user: " + auth.getCurrentUser().getUid());
              Log.w("hieu", "result user: " + result.getUser().getUid());
              String uid = result.getUser().getUid();



              Map<String, Object> data = new HashMap<>();
              data.put("name", "my-name");
              data.put("email", "my-email@gmail.com");

              Log.w("hieu", Thread.currentThread().getName());
              CollectionReference c = db.collection("users");
              Log.w("hieu", c.getPath());
              c.document("wtf")
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Log.w("hieu", "again " + Thread.currentThread().getName());
                  }
                })
                .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                    Log.w("hieu", "why" + e.getMessage());
                  }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                    Log.w("hieu", "complete: " + task.isSuccessful());
                  }
                });
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.e("hieu", "what: " + e.getMessage());
            }
          });
      }
    });
  /*

    /*
   Map<String, Object> data = new HashMap<>();
        data.put(EMAIL, emailInput);
        data.put(FULLNAME, fullNameInput);
        data.put(PASSWORD, passwordInput);
        // Log.w("hieu", Thread.currentThread().getName());
        CollectionReference c = db.collection("users");
        // Log.w("hieu", c.getPath());
        c.document(emailInput)
          .set(data)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Log.w("hieu", "again " + Thread.currentThread().getName());
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.w("hieu", "why" + e.getMessage());
            }
          });

            private static final String FULLNAME = "fullName";
  private static final String EMAIL = "email";
  private static final String PASSWORD = "password";
  private static final String AGE = "age";
  private static final String WEIGHT = "weight";
  private static final String HEIGHT = "height";
     */




