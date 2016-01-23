package com.ashish.cop290assign0;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ashish.cop290assign0.utils.ScreenUtils;

public final class TeamNameFragment extends Fragment {
    private String teamName;
    boolean isFilled;

    public static TeamNameFragment newInstance(boolean isFilled, String teamName) {
        TeamNameFragment fragment = new TeamNameFragment();
        fragment.isFilled = isFilled;
        fragment.teamName = teamName;
        return fragment;
    }

    private String getFilledTeamName() {
        return ScreenUtils.getStringFromTextView(getView(), R.id.team_name);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFilled",isFilled);
        outState.putString("teamName", teamName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.isFilled = savedInstanceState.getBoolean("isFilled");
            this.teamName = savedInstanceState.getString("teamName");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.team_input_layout, null);
        init(view);
        setOnClickListeners(view);
        return view;
    }

    private void init(View view){
//        Log.d(TeamNameFragment.class.getSimpleName(),String.format("init called: isFilled:%b, teamName:%s", isFilled, teamName));
        if(isFilled){
            view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.input_layout).setVisibility(View.GONE);
        }
        ((TextView)view.findViewById(R.id.display_team_name)).setText(teamName);
        ((TextView)view.findViewById(R.id.display_team_name)).setMovementMethod(new ScrollingMovementMethod());
        ((EditText)view.findViewById(R.id.team_name)).setText(teamName);
        addRedAsterisk((EditText) view.findViewById(R.id.team_name));
    }


    //Adds asterisk to editTexts using SpannableString(used for selective formatting of strings ex. color,on click url).
    private void addRedAsterisk(EditText e){
        String text = e.getHint().toString();
        String asterisk = " *";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(text);
        int start = builder.length();
        builder.append(asterisk);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        e.setHint(builder);
    }


    private void setOnClickListeners(final View view){

        view.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teamName = ((EditText) view.findViewById(R.id.team_name)).getText().toString().trim();
                if(!teamName.isEmpty()){
                    //MainActivity.teamName = teamName;
                    MainActivity.mFormData.setTeamName(teamName);   //refactor
                    ((TextView) view.findViewById(R.id.display_team_name)).setText(teamName);
                    //MainActivity.isFilled[position] = true;
                    isFilled = true;
                    MainActivity.mFormData.setIsFilled(0,true);
                    ScreenUtils.hideKeyboard(v); //hidden keyboard
                    view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.input_layout).setVisibility(View.GONE);
                } else{
                    ((EditText) view.findViewById(R.id.team_name)).setError("Team name can't be empty");
                }
            }
        });
        view.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.isFilled[position] = false;
                isFilled = false;
                MainActivity.mFormData.setIsFilled(0, false);

                view.findViewById(R.id.display_layout).setVisibility(View.GONE);
                view.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
            }
        });
    }
}
