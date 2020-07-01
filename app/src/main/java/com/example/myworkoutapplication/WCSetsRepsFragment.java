package com.example.myworkoutapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;


public class WCSetsRepsFragment extends Fragment  {
    private EditText description;
    private EditText s1Reps;
    private EditText s2Reps;
    private EditText s3Reps;
    private String WCdesc;
    private String WCtype;
    private int S1reps;
    private int S2reps;
    private int S3reps;
    private Button wcsrSubmitButton;
    private User currUser = MainActivity.getCurrentUser();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Date currentTime = Calendar.getInstance().getTime();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wc_sets_reps, container, false);
        setData(root);
        Bundle bundle = getArguments();
        if(bundle != null){
            WCtype = bundle.getString("woType");
        }
        wcsrSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WCdesc = description.getText().toString().trim();
                S1reps = Integer.parseInt(s1Reps.getText().toString().trim());
                S2reps = Integer.parseInt(s2Reps.getText().toString().trim());
                S3reps = Integer.parseInt(s3Reps.getText().toString().trim());
               setWClog(WCtype, WCdesc, currentTime, S1reps, S2reps, S3reps);
            }
        });

        return root;
    }

    public void setData(View root){
        description = root.findViewById(R.id.WCSRdesc);
        s1Reps = root.findViewById(R.id.s1reps);
        s2Reps = root.findViewById(R.id.s2reps);
        s3Reps = root.findViewById(R.id.s3reps);
        wcsrSubmitButton = root.findViewById(R.id.WCSRsubmitButton);
    }
    public void setWClog(String workoutType, String workoutDesc, Date date, int rep1, int rep2, int rep3) {
        UserWC currWCUser = new UserWC();
        currWCUser.setWorkoutType(workoutType);
        currWCUser.setWorkoutDesc(workoutDesc);
        currWCUser.setDate(date);
        currWCUser.setRep1(rep1);
        currWCUser.setRep2(rep2);
        currWCUser.setRep3(rep3);
        String WCName = "WClog" + "-" + currUser.getEmail();
        db.collection(WCName).add(currWCUser);
    }
}