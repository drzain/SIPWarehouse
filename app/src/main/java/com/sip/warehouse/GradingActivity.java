package com.sip.warehouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class GradingActivity extends AppCompatActivity implements StepperLayout.StepperListener {


    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayoutGrading);

        mStepperAdapter = new StepperAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(this, "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        this.finish();
    }
}
