package com.example.myworkoutapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunningTrackerHistoryFragment extends Fragment {
  private FirebaseFirestore db;
  private CollectionReference userHistory;
  private TextView textViewData;
  private Button goBackButton;
  private Button nextButton;
  private User currentUser;
  private static String DATE = "date";

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_runtracker_history, container, false);
    textViewData = root.findViewById(R.id.text_view_data);
    goBackButton = root.findViewById(R.id.RTGoBackButton);
    nextButton = root.findViewById(R.id.RTNextButton);
    setDataToDatabase();
    userHistory
      .orderBy(DATE, Query.Direction.DESCENDING)
      .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        String data = "";
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
          UserHistory userHistory = documentSnapshot.toObject(UserHistory.class);
          String workoutType = userHistory.getWorkoutType();
          Date date = userHistory.getDate();
          String time = userHistory.getTime();
          double distance = userHistory.getDistance();
          DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
          String strDate = dateFormat.format(date);
          data += "The workout is: " + workoutType;
          data += "\nThe date is: " + strDate + "\nThe time workout is: " + time;
          data += "\nThe distance is: " + distance + " miles";
          data += "\n\n";
        }
        textViewData.setText(data);
      }
    });

    goBackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new HistoryFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
      }
    });

    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new CaloriesCounterHistoryFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
      }
    });



    return root;
  }

  public void setDataToDatabase() {
    db = FirebaseFirestore.getInstance();
    currentUser = MainActivity.getCurrentUser();
    String historyName = "history" + "-" + currentUser.getEmail();
    userHistory = db.collection(historyName);

  }
}