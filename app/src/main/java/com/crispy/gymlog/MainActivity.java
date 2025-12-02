package com.crispy.gymlog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;



import androidx.appcompat.app.AppCompatActivity;


import com.crispy.gymlog.database.GymLogRepository;
import com.crispy.gymlog.database.entities.GymLog;
import com.crispy.gymlog.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GymLogRepository repository;

    public static final String TAG = "DAC_GYMLOG";

    String mExercise = "";
    double mWeight = 0.0;
    int mReps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // vvv Boilerplate vvv
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // ^^^ Boilerplate ^^^

        repository = GymLogRepository.getRepository(getApplication());

        // Allows user to scroll the log on top
        binding.logDisplayTextView.setMovementMethod(new ScrollingMovementMethod());

        // Log the values that are input in the text fields
        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformationFromDisplay();
                insertGymLogRecord();
                updateDisplay();

            }
        });

    }

    private void insertGymLogRecord() {
        GymLog log = new GymLog(mExercise, mWeight, mReps);
        repository.insertGymLog(log);
    }

    /**
     * Using information gathered from the user, update the log at the top of the interface
     */
    private void updateDisplay() {
        String currentInfo = binding.logDisplayTextView.getText().toString();
        String newDisplay = String.format(Locale.US,
                "Exercise:%s%nWeight:%.2f%nReps:%d%n=-=-=-=%n%s",
                mExercise, mWeight, mReps, currentInfo);

        binding.logDisplayTextView.setText(newDisplay);
        Log.i(TAG, repository.getAllLogs().toString());
    }

    /**
     * Read in the values currently input and store them to local variables
     */
    private void getInformationFromDisplay() {
        mExercise = binding.exerciseInputEditText.getText().toString();

        try {
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from weight edit text.");
        }

        try {
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch (NumberFormatException e) {
            Log.d(TAG, "Error reading value from reps edit text.");
        }
    }
}