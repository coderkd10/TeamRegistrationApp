package com.ashish.cop290assign0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

public final class PagerFragment extends Fragment {
    private String name,entryCode;
    private Bitmap image;
    public static PagerFragment newInstance(int p) {
        PagerFragment fragment = new PagerFragment();
//        try {
//            byte[] b = Base64.decode(img, Base64.DEFAULT);
//            fragment.image = BitmapFactory.decodeByteArray(b, 0, b.length);
//        }catch(Exception e){
//            fragment.image = null;
//        }
        fragment.image = null;
        fragment.position = p;
        return fragment;
    }
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//            mContent = savedInstanceState.getString(KEY_CONTENT);
//        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        View child;
        if(position == 0) {
            child = inflater.inflate(R.layout.team_input_layout, null);
            layout.addView(child);

        }
        else {
            child = inflater.inflate(R.layout.student_info_layout, null);
            layout.addView(child);
            addOnTextChangeListener(layout);
            ((TextView) layout.findViewById(R.id.member_no)).setText("#" + position);
        }
        setOnClickListeners(layout);
        return layout;
    }

    private void setOnClickListeners(final LinearLayout layout){

        layout.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0) {
                    String entryCode = ((EditText) layout.findViewById(R.id.entryCode)).getText().toString().trim();
                    String name = ((EditText) layout.findViewById(R.id.name)).getText().toString().trim();
                    boolean isEntryCodeValid = InputValidator.validateEntryCode(entryCode);
                    boolean isNameValid = InputValidator.validateName(name);
                    if (!entryCode.isEmpty() && !name.isEmpty()) {
                        boolean isInvalid = false;
                        if (!isEntryCodeValid) {
                            ((EditText) layout.findViewById(R.id.entryCode)).setError("Invalid entry no!");
                            isInvalid = true;
                        }
                        if (!isNameValid) {
                            ((EditText) layout.findViewById(R.id.name)).setError("Invalid name!");
                            isInvalid = true;
                        }
                        if (isInvalid) return;
                        MainActivity.names[position-1] = name;
                        MainActivity.entryCodes[position-1] = entryCode;
                        ((TextView) layout.findViewById(R.id.display_entry_code)).setText(entryCode);
                        ((TextView) layout.findViewById(R.id.display_name)).setText(name);
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
                    }
                }else{
                    String teamName = ((EditText) layout.findViewById(R.id.team_name)).getText().toString().trim();
                    if(!teamName.isEmpty()){
                        MainActivity.teamName = teamName;
                        ((TextView) layout.findViewById(R.id.display_team_name)).setText(teamName);
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
                    }
                }
            }
        });
        layout.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.findViewById(R.id.display_layout).setVisibility(View.GONE);
                layout.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
            }
        });
    }

    private void addOnTextChangeListener(final LinearLayout layout){
        final EditText editText = ((EditText)layout.findViewById(R.id.entryCode)); //also entry number text box
        //EditText entryNumBox = (EditText) layout.findViewById(R.id.entryCode);
        final EditText nameBox = (EditText) layout.findViewById(R.id.name);
        final View okBttn = layout.findViewById(R.id.save_data);
        final CircularImageView personImgView = (CircularImageView) layout.findViewById(R.id.img);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (InputValidator.validateEntryCode(editText.getText().toString()) && count < 11)
                    fetchAndEditStudentDetails(editText.getText().toString(), editText, nameBox, okBttn, personImgView);
            }
        });
    }

    private void fetchAndEditStudentDetails(String entryCode, final EditText entryNumBox, final EditText nameBox, final View okBttn, final CircularImageView personImgView){
        /**
         * TODO
         * 1. Fetch Details of student with entry number `entryCode`
         * 2. Change picture received according to the picture received
         * 3. Change name according to name received
         * 4. Click done button
         */
        MainActivity.mLdapFetcher.getAndHandleStudentDetails(entryCode, new LdapFetcher.studentJsonDataHandler() {
            @Override
            public void onGetJson(JSONObject studentDataJson) {
                try {
                    if (studentDataJson.getBoolean("isValid")) {
                        entryNumBox.setText(studentDataJson.getString("entryNumber"));
                        nameBox.setText(studentDataJson.getString("name"));
                        if (studentDataJson.has("img")) {
                            byte[] b = Base64.decode(studentDataJson.getString("img"), Base64.DEFAULT);
                            Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                            personImgView.setImageBitmap(bmp);
                        } else {
                            personImgView.setImageResource(R.mipmap.ic_launcher);
                        }
                        //TODO: close keyboard. Following approaches not working!
                        //okBttn.requestFocus();
                        //personImgView.clearFocus();
                        okBttn.performClick();
                    }
                    ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(KEY_CONTENT, mContent);
    }
    private int dp2Pix(float i){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }
}
