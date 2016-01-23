package com.ashish.cop290assign0;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashish.cop290assign0.utils.ScreenUtils;

public final class TeamNameFragment extends DetailsEntryFragment {
    private static final String TAG = TeamNameFragment.class.getSimpleName();

    public static TeamNameFragment newInstance() {
        return new TeamNameFragment();
    }

    private String getTeamNameFromForm() {
        return MainActivity.mFormData.getTeamName();
    }
    private void setTeamNameInForm(String teamName) {
        MainActivity.mFormData.setTeamName(teamName);
    }
    private boolean getIsfilled() {
        return MainActivity.mFormData.getIsFilled(0);
    }
    private void setIsfilled(boolean isFilled) {
        MainActivity.mFormData.setIsFilled(0,isFilled);
    }
    private String getFilledTeamName() {
        return ScreenUtils.getStringFromTextView(getView(), R.id.team_name).trim();
    }
    private void setTeamName(String teamName) {
        ScreenUtils.setTextInTextView(getView(), R.id.team_name, teamName);
    }
    private void markRequired() {
        ScreenUtils.addRedAsteriskToEditText(getView(), R.id.team_name);
    }
    private void setEmptyTeamNameError() {
        ScreenUtils.setErrorInEditText(getView(), R.id.team_name, "Team name can't be empty");
    }
    private boolean isValidUserInput() {
        if(getFilledTeamName().isEmpty()) {
            setEmptyTeamNameError();
            return false;
        } else {
            return true;
        }
    }
    private void saveFilledTeamNameToForm() {
        setTeamNameInForm(getFilledTeamName());
    }

    private void displayTeamName(String teamName) {
        ScreenUtils.setTextInTextView(getView(), R.id.display_team_name, teamName);
    }
    private void displayFilledTeamName() {
        displayTeamName(getFilledTeamName());
    }
    private void makeDisplayTeamNameScrollable() {
        try {
            ((TextView) getView().findViewById(R.id.display_team_name)).setMovementMethod(new ScrollingMovementMethod());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void switchToEditDetailsMode() {
        try {
            getView().findViewById(R.id.display_layout).setVisibility(View.GONE);
            getView().findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void switchToDisplayDetailsMode() {
        try {
            getView().findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.input_layout).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateViewMode() {
        if(getIsfilled())
            switchToDisplayDetailsMode();
        else
            switchToEditDetailsMode();
    }

    public boolean isRequired() {
        return true;
    }
    public boolean isCompletelyFilled() {
        if(getIsfilled())
            return true;
        return isValidUserInput();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!getIsfilled()) {
            if(!getFilledTeamName().isEmpty())
                outState.putString("filledTeamName",getFilledTeamName());
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, String.format("onCreateView called. savedInstanceState:%s, inflater:%s, container:%s", savedInstanceState, inflater, container));
        View view = inflater.inflate(R.layout.team_input_layout, null);
        setOnClickListeners(view);
        return view;
    }

    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, String.format("onActivityCreated called. savedInstanceState:%s", savedInstanceState));
        super.onActivityCreated(savedInstanceState);
        markRequired();
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("filledTeamName"))
                setTeamName(savedInstanceState.getString("filledEntryNumber"));
            else
                setTeamName(getTeamNameFromForm());
        }
        displayTeamName(getTeamNameFromForm());
        makeDisplayTeamNameScrollable();
        updateViewMode();
    }

    private void onSaveFilledDetails() {
        Log.i(TAG,String.format("save_data. entered:%s",getFilledTeamName()));
        if(getFilledTeamName().isEmpty()) {
            setEmptyTeamNameError(); //TODO trim filledTeamName
            return;
        }
        saveFilledTeamNameToForm();
        setIsfilled(true);
        displayFilledTeamName();
        ScreenUtils.hideKeyboard(getView()); //hide keyboard
        updateViewMode();
    }

    private void setOnClickListeners(final View view){
        view.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveFilledDetails();
            }
        });
        view.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsfilled(false);
                updateViewMode();
            }
        });
    }
}
