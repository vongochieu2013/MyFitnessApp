package com.example.myworkoutapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;

public class StartWorkoutFragment extends Fragment {
  private Chronometer chronometer;
  private Button startButton;
  private Button pauseButton;
  private Button resetButton;
  private EditText workOutTypeText;
  private long pauseOffSet;
  private boolean running;
  private MediaPlayer player;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private User currentUser = MainActivity.getCurrentUser();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_startworkout, container, false);
    setData(root);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!running) {
          chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffSet);
          chronometer.start();
          playMusic();
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
          pauseMusic();
          running = false;
        }
      }
    });
    resetButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!running) {
          String workoutType = workOutTypeText.getEditableText().toString().trim();
          String chroText = chronometer.getText().toString().substring(6);
          Date date = new Date();
          Toast.makeText(getContext(), "At " + date + "\nYou did a " + workoutType + " workout in: " + chroText, Toast.LENGTH_LONG).show();
          setUserHistory(date, chroText, workoutType);
          chronometer.setBase(SystemClock.elapsedRealtime());
          stopMusic();
          pauseOffSet = 0;
        }
      }
    });
    return root;
  }


  public void setData(View root) {
    chronometer = root.findViewById(R.id.chronometer);
    startButton = root.findViewById(R.id.startwoButton);
    pauseButton = root.findViewById(R.id.pausewoButton);
    resetButton = root.findViewById(R.id.resetButton);
    workOutTypeText = root.findViewById(R.id.workOutType);
    chronometer.setFormat("Time: %s");
    chronometer.setBase(SystemClock.elapsedRealtime());
  }

  public void setUserHistory(Date date, String time, String workoutType) {
    UserHistory currentUserHistory = new UserHistory(date, time, workoutType);
    currentUser = MainActivity.getCurrentUser();
    String historyName = "history" + "-" + currentUser.getEmail();
    db.collection(historyName).add(currentUserHistory);
  }


  public void playMusic() {
    if (player == null) {
      player = MediaPlayer.create(getContext(), R.raw.pop_workoutsong);
      player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          stopPlayer();
        }
      });
    }
    player.start();
  }

  public void pauseMusic() {
    if (player != null) {
      player.pause();
    }
  }

  public void stopMusic() {
    stopPlayer();
  }

  private void stopPlayer() {
    if (player != null) {
      player.release();
      player = null;
    }
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
