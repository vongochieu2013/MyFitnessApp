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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
  private TextInputLayout fullNameTextInput;
  private TextInputLayout passwordTextInput;
  private Button confirmButton;
  private String userId;
  private static final String TAG = "TAG";
  private static final String FULLNAME = "Fullname";
  private static final String EMAIL = "Email";
  private FirebaseAuth auth = FirebaseAuth.getInstance();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    setUp();
    auth
      .createUserWithEmailAndPassword("chan@gmail.com", "chan@123")
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
          db.collection("users")
            .document(uid)
            .set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Log.w("hieu", "again " + Thread.currentThread().getName());
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

  public void setUp() {
    emailTextInput = findViewById(R.id.emailTextInput);
    fullNameTextInput = findViewById(R.id.fullnameTextInput);
    passwordTextInput = findViewById(R.id.passwordTextInput);
    confirmButton = findViewById(R.id.confirmButton);

    confirmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!validateEmail() | !validateName()) {
          return;
        }
        final String emailInput = emailTextInput.getEditText().getText().toString().trim();
        final String fullnameInput = fullNameTextInput.getEditText().getText().toString().trim();
        String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
        auth.createUserWithEmailAndPassword(emailInput, passwordInput)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                userId = auth.getCurrentUser().getUid();
                Log.w("hieu", "userId=" + userId);
                DocumentReference documentReference = db.collection("users").document(userId);
                Map<String, Object> user = new HashMap<>();
                user.put(FULLNAME, fullnameInput);
                user.put(EMAIL, emailInput);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                    Log.d(TAG, "On Success: user profile is created for " + userId);
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
    String fullnameInput = fullNameTextInput.getEditText().getText().toString().trim();
    if (fullnameInput.isEmpty()) {
      fullNameTextInput.setError("Field can't be empty");
      return false;
    } else {
      fullNameTextInput.setError(null);
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