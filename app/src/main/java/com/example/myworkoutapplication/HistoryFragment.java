package com.example.myworkoutapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

public class HistoryFragment extends Fragment {
  private CardView RTHistoryButton;
  private CardView CCHistoryButton;
  private CardView WCHistoryButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_history, container, false);
    setData(root);
    RTHistoryButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new RunningTrackerHistoryFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_container, fragment).commit();
      }
    });

    CCHistoryButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new CaloriesCounterHistoryFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_container, fragment).commit();
      }
    });

    WCHistoryButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new WorkoutCompanionHistoryFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragment_container, fragment).commit();
      }
    });

    return root;
  }

  public void setData(View root) {
    RTHistoryButton = root.findViewById(R.id.RTHistoryButton);
    CCHistoryButton = root.findViewById(R.id.CCHistoryButton);
    WCHistoryButton = root.findViewById(R.id.WCHistoryButton);

  }
}