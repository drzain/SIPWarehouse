package com.sip.warehouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class GradingActivity extends AppCompatActivity implements StepperLayout.StepperListener {

    private TextView idGrading,assetDesc,platKendaraan,manYear,kmGrading,warna,chassisNo,machineNo,receiveDate;
    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);

        idGrading = (TextView) findViewById(R.id.idGrading);
        assetDesc = (TextView) findViewById(R.id.assetDesc);
        platKendaraan = (TextView) findViewById(R.id.platKendaraan);
        manYear = (TextView) findViewById(R.id.manYear);
        kmGrading = (TextView) findViewById(R.id.kmGrading);
        warna = (TextView) findViewById(R.id.warna);
        chassisNo = (TextView) findViewById(R.id.chassisNo);
        machineNo = (TextView) findViewById(R.id.machineNo);
        receiveDate = (TextView) findViewById(R.id.receiveDate);

        Intent intent = getIntent();
        String kik = intent.getStringExtra("kik");
        String asset_desc = intent.getStringExtra("asset_desc");
        String plat = intent.getStringExtra("plat");
        String man_year = intent.getStringExtra("man_year");
        String colour = intent.getStringExtra("colour");
        String chasis = intent.getStringExtra("chasis");
        String machine = intent.getStringExtra("machine");
        String receive_date = intent.getStringExtra("receive_date");
        String asset_type = intent.getStringExtra("asset_type");

        idGrading.setText(kik);
        assetDesc.setText(asset_desc);
        platKendaraan.setText(plat);
        manYear.setText(man_year);
        warna.setText(colour);
        chassisNo.setText(chasis);
        machineNo.setText(machine);
        receiveDate.setText(receive_date);

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
