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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
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
  private TextInputLayout caloriesTextInput;
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
        if (!validateEmail() | !validateName() | !validatePassword() | !validateAge() | !validateWeight()) {
          return;
        }
        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        User newUser = setUpUser();
        CollectionReference c = db.collection("users");
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
    weightTextInput = findViewById(R.id.weightTextInput);
    caloriesTextInput = findViewById(R.id.caloriesTextInput);
  }

  public boolean validateExistedEmail() {
    String emailInput = emailTextInput.getEditText().getText().toString().trim();
    final boolean[] result = new boolean[1];
    db.collection("users").document(emailInput).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (Objects.requireNonNull(task.getResult()).exists()) {
          result[0] = false;
        } else {
          result[0] = true;
        }
      }
    });
    return result[0];
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
    String fullNameInput = fullNameTextInput.getEditText().getText().toString().trim();
    if (fullNameInput.isEmpty()) {
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

  public boolean validateAge() {
    String ageInput = ageTextInput.getEditText().getText().toString().trim();
    if (ageInput.isEmpty()) {
      ageTextInput.setError("Field can't be empty");
      return false;
    } else {
      ageTextInput.setError(null);
      return true;
    }
  }

  public boolean validateWeight() {
    String weightInput = weightTextInput.getEditText().getText().toString().trim();
    if (weightInput.isEmpty()) {
      weightTextInput.setError("Field can't be empty");
      return false;
    } else {
      weightTextInput.setError(null);
      return true;
    }
  }

  public User setUpUser() {
    final String emailInput = emailTextInput.getEditText().getText().toString().trim();
    final String fullNameInput = fullNameTextInput.getEditText().getText().toString().trim();
    String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
    int age = Integer.parseInt(ageTextInput.getEditText().getText().toString().trim());
    int weight = Integer.parseInt(weightTextInput.getEditText().getText().toString().trim());
    int calories = 0;
    if (!caloriesTextInput.getEditText().getText().toString().matches("")) {
      calories = Integer.parseInt(caloriesTextInput.getEditText().getText().toString().trim());
    }
    return new User(emailInput, fullNameInput, passwordInput, age, weight, calories);
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
  */





