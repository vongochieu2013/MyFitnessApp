package com.example.myworkoutapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
  private String email;
  private String username;
  private TextView displayEmail;
  private TextView displayUsername;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_profiles, container, false);
    email = getArguments().getString("email");
    username = getArguments().getString("username");
    Spinner spinner = root.findViewById(R.id.spinnerAge);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.numbers, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
    spinner.setOnItemSelectedListener(this);
    displayEmail = root.findViewById(R.id.displayEmail);
    displayUsername = root.findViewById(R.id.displayUsername);
    displayEmail.setText(email);
    displayUsername.setText(username);
    return root;
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    String text = parent.getItemAtPosition(position).toString();
    Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }
}
