package com.example.myworkoutapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.FirstPartyScopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private Button SignUpButton;
    private Button SignInButton;
    private TextInputLayout emailMain;
    private TextInputLayout passwordMain;
    private TextView AttemptText;

    String emailInput;
    String passwordInput;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignInButton = findViewById(R.id.SignInButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        emailMain = findViewById(R.id.emailMain);
        passwordMain = findViewById(R.id.passwordMain);
        AttemptText = findViewById(R.id.AttemptText);
        AttemptText.setText("Attempts remaining: 5");

        mAuth.signOut();

        FirebaseUser user = mAuth.getCurrentUser() ;
        if (user != null) {
            finish();
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
        }

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInfo();
            }
        });

    }

    private void validateInfo() {
        emailInput = emailMain.getEditText().getText().toString().trim();
        passwordInput = passwordMain.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            emailMain.setError("Email is required");
            decreaseAttempt();
            return;
        }
        if (passwordInput.isEmpty()) {
            passwordMain.setError("Password is required");
            decreaseAttempt();
            return;
        }
        mAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Email Address or Password", Toast.LENGTH_SHORT).show();
                    decreaseAttempt();
                }
            }
        });
    }

    private void decreaseAttempt() {
        counter--;
        AttemptText.setText("No of attempts remaining: " + counter);
        if (counter == 0) {
            SignInButton.setEnabled(false);
        }
    }

    /*
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accountSet.add("admin//admin"); //Admin account: username: admin // password: admin
        SignInButton = findViewById(R.id.SignInButton);
        SignUpButton = findViewById(R.id.SignUpButton);
        usernameMain = findViewById(R.id.usernameMain);
        passwordMain = findViewById(R.id.passwordMain);

        mCheckBox = findViewById(R.id.checkBox);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        checkSharedPreferences();

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckBox.isChecked()) {
                    mEditor.putString(getString(R.string.check_box), "True");
                    mEditor.commit();

                    String name = usernameMain.getEditText().getText().toString().trim();
                    mEditor.putString(getString(R.string.username), name);
                    mEditor.commit();

                    String password = passwordMain.getEditText().getText().toString().trim();
                    mEditor.putString(getString(R.string.password), password);
                    mEditor.commit();
                } else {
                    mEditor.putString(getString(R.string.check_box), "False");
                    mEditor.commit();

                    mEditor.putString(getString(R.string.username), "");
                    mEditor.commit();

                    String password = passwordMain.getEditText().getText().toString().trim();
                    mEditor.putString(getString(R.string.password), "");
                    mEditor.commit();
                }

                if (!validateInfo()) {
                    String invalid = "Incorrect Username or Password.\nPlease try again.";
                    Toast.makeText(getApplicationContext(), invalid, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                intent.putExtra("email", emailInput);
                intent.putExtra("username", usernameInput);
                // intent.putExtra("password", passwordInput);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                emailInput = data.getStringExtra("email");
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                String accountInfo = username + "//" + password;
                accountSet.add(accountInfo);
            }
        }
    }


    private boolean validateInfo() {
        usernameInput = usernameMain.getEditText().getText().toString().trim();
        passwordInput = passwordMain.getEditText().getText().toString().trim();
        String check = usernameInput + "//" + passwordInput;
        if (accountSet.contains(check)) {
            return true;
        }
        return false;
    }

    private void checkBox() {
        if (mCheckBox.isChecked()) {
            mEditor.putString(getString(R.string.check_box), "True");
            mEditor.commit();

            String name = emailMain.getEditText().getText().toString().trim();
            mEditor.putString(getString(R.string.username), name);
            mEditor.commit();

            String password = passwordMain.getEditText().getText().toString().trim();
            mEditor.putString(getString(R.string.password), password);
            mEditor.commit();
        } else {
            mEditor.putString(getString(R.string.check_box), "False");
            mEditor.commit();

            mEditor.putString(getString(R.string.username), "");
            mEditor.commit();

            String password = passwordMain.getEditText().getText().toString().trim();
            mEditor.putString(getString(R.string.password), "");
            mEditor.commit();
        }
    }

     */

}