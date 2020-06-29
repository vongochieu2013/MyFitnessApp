package com.example.myworkoutapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;


public class WorkoutCompanionFragment extends Fragment {
    //variables
    private Date currentTime = Calendar.getInstance().getTime();
    private Button pushButton;
    private Button pullButton;
    private Button legsButton;
    private TextView wcLog; // will be used with loadNotes() to display workout history
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User currentUser;
    private UserWC userwc = new UserWC();
    private static String DATE = "date";// used in loadNotes()
    private CollectionReference  WClogHistory; // used in loadNotes()

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workout_companion, container, false);
        setData(root);

        //buttons
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userwc.setWorkoutType("Push");
                setWClog(userwc.getWorkoutType(),currentTime);
            }
        });

        pullButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userwc.setWorkoutType("Pull");
                setWClog(userwc.getWorkoutType(),currentTime);
            }
        });
        legsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userwc.setWorkoutType("Legs");
                setWClog(userwc.getWorkoutType(),currentTime);
            }
        });

        Log.w("chris", "working"); // <-- for testing

        // loadNotes(root); <---- THIS IS NOT WORKING!!
        return root;
    }

    //setters
    public void setData(View root){
        pushButton = root.findViewById(R.id.PushButton);
        pullButton = root.findViewById(R.id.PullButton);
        legsButton = root.findViewById(R.id.LegsButton);
        wcLog = root.findViewById(R.id.WClog);
    }

    public void setWClog(String workoutType, Date date) {
        UserWC currentUserWC = new UserWC(workoutType, date);
        currentUser = MainActivity.getCurrentUser();
        String CCName = "WClog" + "-" + currentUser.getEmail();
        db.collection(CCName).add(currentUserWC);
    }


   /* public void loadNotes(View v){ // for wcLog textview
        WClogHistory
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    UserWC userWCHistory = documentSnapshot.toObject(UserWC.class);
                    workoutType = userWCHistory.getWorkoutType().trim();
                    data +=  currentTime + " - The workout is: " + workoutType;
                    data += "\n\n";
                }
                wcLog.setText(data);
            }
        });
    }

    */

}