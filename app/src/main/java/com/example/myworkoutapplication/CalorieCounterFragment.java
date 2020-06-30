package com.example.myworkoutapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class CalorieCounterFragment extends Fragment {

  private Date currentTime = Calendar.getInstance().getTime();
  private TextView output1;
  private EditText mealDescriptionCCText;
  private EditText caloriesCCText;
  private String mealDescription;
  private int userCaloriesGoal;
  private int caloriesDay;
  private Button butt1;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private User currentUser;
  private CollectionReference userCCHistory;

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
        String allInput =  currentTime + " - " + mealDescription + " " + caloriesDay + "\n\n" ;
        output1.setText(allInput);
        setCC(mealDescription, caloriesDay, currentTime);
        checkForTotalCalories();
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
    userCaloriesGoal = currentUser.getCalories();
    String CCName = "CC" + "-" + currentUser.getEmail();
    userCCHistory = db.collection(CCName);
    userCCHistory.add(currentUserCC);
  }

  public void checkForTotalCalories() {
    final int[] totalCalories = {0};
    userCCHistory
      .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
          UserCC userHistory = documentSnapshot.toObject(UserCC.class);
          Date currentDate = userHistory.getDate();
          int currentCalories = userHistory.getCalories();
          Date date = new Date();
          SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
          String stringCurrentDate = DateFor.format(date); // It will be the current date.
          String stringCCDate = DateFor.format(currentDate); // It is the time from the userHistory

          if (stringCurrentDate.equals(stringCCDate)) {
            totalCalories[0] += currentCalories;
          }
        }
        if (totalCalories[0] >= userCaloriesGoal) {
          String result = String.format("You passed the goal for today since %d > %d", totalCalories[0], userCaloriesGoal);
          Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        } else {
          String result = String.format("You failed the goal for today since %d < %d", totalCalories[0], userCaloriesGoal);
          Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
      }
    });
  }

}