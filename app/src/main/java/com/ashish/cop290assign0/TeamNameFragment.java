package com.ashish.cop290assign0;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ashish.cop290assign0.utils.ScreenUtils;

public final class TeamNameFragment extends Fragment {
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
        return ScreenUtils.getStringFromTextView(getView(), R.id.team_name);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!getIsfilled()) {
            if(!getFilledTeamName().isEmpty())
                outState.putString("filledTeamName",getFilledTeamName());
        }
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState != null) {
//            this.isFilled = savedInstanceState.getBoolean("isFilled");
//            this.teamName = savedInstanceState.getString("teamName");
//        }
//    }


    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, String.format("onCreateView called. savedInstanceState:%s, inflater:%s, container:%s", savedInstanceState, inflater, container));
        View view = inflater.inflate(R.layout.team_input_layout, null);
        //ScreenUtils.setTextInTextView(view, R.id.member_no, "#" + position);
        //addEntryNumberOnTextChangeListener(view);
        setOnClickListeners(view);
        return view;
    }

//    @Override
//    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
//        View view;
//        view = inflater.inflate(R.layout.team_input_layout, null);
//        init(view);
//        setOnClickListeners(view);
//        return view;
//    }

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

//    private void init(View view){
////        Log.d(TeamNameFragment.class.getSimpleName(),String.format("init called: isFilled:%b, teamName:%s", isFilled, teamName));
//        if(isFilled){
//            view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.input_layout).setVisibility(View.GONE);
//        }
//        ((TextView)view.findViewById(R.id.display_team_name)).setText(teamName);
//        ((EditText)view.findViewById(R.id.team_name)).setText(teamName);
//        addRedAsterisk((EditText) view.findViewById(R.id.team_name));
//    }


//    //Adds asterisk to editTexts using SpannableString(used for selective formatting of strings ex. color,on click url).
//    private void addRedAsterisk(EditText e){
//        String text = e.getHint().toString();
//        String asterisk = " *";
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        builder.append(text);
//        int start = builder.length();
//        builder.append(asterisk);
//        int end = builder.length();
//        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        e.setHint(builder);
//    }


    private void onSaveFilledDetails() {
        Log.i(TAG,String.format("save_data. entered:%s",getFilledTeamName()));
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

//    private void setOnClickListeners(final View view){
//
//        view.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String teamName = ((EditText) view.findViewById(R.id.team_name)).getText().toString().trim();
//                if(!teamName.isEmpty()){
//                    //MainActivity.teamName = teamName;
//                    MainActivity.mFormData.setTeamName(teamName);   //refactor
//                    ((TextView) view.findViewById(R.id.display_team_name)).setText(teamName);
//                    //MainActivity.isFilled[position] = true;
//                    isFilled = true;
//                    MainActivity.mFormData.setIsFilled(0,true);
//                    ScreenUtils.hideKeyboard(v); //hidden keyboard
//                    view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
//                    view.findViewById(R.id.input_layout).setVisibility(View.GONE);
//                } else{
//                    ((EditText) view.findViewById(R.id.team_name)).setError("Team name can't be empty");
//                }
//            }
//        });
//        view.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //MainActivity.isFilled[position] = false;
//                isFilled = false;
//                MainActivity.mFormData.setIsFilled(0, false);
//
//                view.findViewById(R.id.display_layout).setVisibility(View.GONE);
//                view.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
//            }
//        });
//    }
}
