package com.example.myworkoutapplication;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartWorkoutFragment extends Fragment {
  private Chronometer chronometer;
  private Button startButton;
  private Button pauseButton;
  private Button resetButton;
  private long pauseOffSet;
  private boolean running;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_startworkout, container, false);
    chronometer = root.findViewById(R.id.chronometer);
    startButton = root.findViewById(R.id.startwoButton);
    pauseButton = root.findViewById(R.id.pausewoButton);
    resetButton = root.findViewById(R.id.resetButton);
    chronometer.setFormat("Time: %s");
    chronometer.setBase(SystemClock.elapsedRealtime());
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!running) {
          chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffSet);
          chronometer.start();
          running = true;
        }
      }
    });
    pauseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (running) {
          chronometer.stop();
          pauseOffSet = SystemClock.elapsedRealtime() - chronometer.getBase();
          running = false;
        }
      }
    });
    resetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!running) {
          chronometer.setBase(SystemClock.elapsedRealtime());
          pauseOffSet = 0;
        }
      }
    });
    return root;
  }
}


        /*
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getContext(), "Done!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

         */
