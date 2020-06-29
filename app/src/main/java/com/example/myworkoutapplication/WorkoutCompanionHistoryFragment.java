package com.example.myworkoutapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

public class WorkoutCompanionHistoryFragment extends Fragment {
  private FirebaseFirestore db;
  private Button goBackButton;
  private User currentUser;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_wc_history, container, false);
    goBackButton = root.findViewById(R.id.WCGoBackButton);
    setData();

    goBackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Fragment fragment = new HistoryFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
      }
    });

    return root;
  }

  public void setData() {
    db = FirebaseFirestore.getInstance();
    currentUser = MainActivity.getCurrentUser();
  }
}