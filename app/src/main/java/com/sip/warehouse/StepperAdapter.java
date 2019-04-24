package com.sip.warehouse;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";

    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {

        switch (position){
            case 0:
                final OneFragment step1 = new OneFragment();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;
            case 1:
                final TwoFragment step2 = new TwoFragment();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;
            case 2:
                final ThreeFragment step3 = new ThreeFragment();
                Bundle b3 = new Bundle();
                b3.putInt(CURRENT_STEP_POSITION_KEY, position);
                step3.setArguments(b3);
                return step3;
            case 3:
                final FourFragment step4 = new FourFragment();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;
            case 4:
                final FiveFragment step5 = new FiveFragment();
                Bundle b5 = new Bundle();
                b5.putInt(CURRENT_STEP_POSITION_KEY, position);
                step5.setArguments(b5);
                return step5;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position){
            case 0:
                return new StepViewModel.Builder(context)
                        .setTitle("Exterior") //can be a CharSequence instead
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Interior") //can be a CharSequence instead
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Machine") //can be a CharSequence instead
                        .create();
            case 3:
                return new StepViewModel.Builder(context)
                        .setTitle("Kaki-Kaki") //can be a CharSequence instead
                        .create();
            case 4:
                return new StepViewModel.Builder(context)
                        .setTitle("Accesories") //can be a CharSequence instead
                        .create();
        }
        return null;
    }

}
