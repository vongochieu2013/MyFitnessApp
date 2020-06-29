package com.example.myworkoutapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

public class CaloriesCounterHistoryFragment extends Fragment {
  private FirebaseFirestore db;
  private Button goBackButton;
  private Button nextButton;
  private User currentUser;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_caloriescounter_history, container, false);
    goBackButton = root.findViewById(R.id.CCGoBackButton);
    nextButton = root.findViewById(R.id.CCNextButton);
    setDataToDatabase();

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
        Fragment fragment = new WorkoutCompanionHistoryFragment();
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

  }
}