package com.sip.warehouse;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class GradingFragment extends Fragment implements StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
    private StepperAdapter mStepperAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grading, container, false);

        mStepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);

        mStepperAdapter = new StepperAdapter(getFragmentManager(), getActivity());
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
        return view;
    }

    @Override
    public void onCompleted(View completeButton) {
        Toast.makeText(getActivity(), "onCompleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(getActivity(), "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        getActivity().finish();
    }

}
