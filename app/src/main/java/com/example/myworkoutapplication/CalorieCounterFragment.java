package com.example.myworkoutapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class CalorieCounterFragment extends Fragment {

  private Date currentTime = Calendar.getInstance().getTime();
  private TextView output1;
  private EditText mealDescriptionCCText;
  private EditText caloriesCCText;
  private String mealDescription;
  private int caloriesDay;
  private Button butt1;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private User currentUser;



  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_caloriecounter, container, false);
    setData(root);
    butt1 = root.findViewById(R.id.submitCCButton);
    butt1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mealDescription = mealDescriptionCCText.getText().toString().trim();
        caloriesDay = Integer.parseInt(caloriesCCText.getText().toString().trim());
        Log.w("hieu", "the calories is: " + caloriesDay);
        Log.w("hieu", "The meal is: " + mealDescription);
        String allInput =  currentTime + " - " + mealDescription + " " + caloriesDay + "\n\n" ;
        output1.setText(allInput);
        setCC(mealDescription, caloriesDay, currentTime);
      }
    });
    return root;

  }

  public void setData(View root) {
    output1 = root.findViewById(R.id.CCtextView);
    mealDescriptionCCText = root.findViewById(R.id.mealDescriptionCCText);
    caloriesCCText = root.findViewById(R.id.caloriesCCText);
  }

  public void setCC(String mealDescription, int caloriesPerDay, Date date) {
    UserCC currentUserCC = new UserCC(date, mealDescription, caloriesPerDay);
    currentUser = MainActivity.getCurrentUser();
    String CCName = "CC" + "-" + currentUser.getEmail();
    db.collection(CCName).add(currentUserCC);
  }

}