package com.ashish.cop290assign0;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

public final class PagerFragment extends Fragment {
    private String name,entryCode,image,teamName;
    private  int visibility;
    LinearLayout l;
    public static PagerFragment newInstance(int p,int v, String n,String e,String i,String t) {
        PagerFragment fragment = new PagerFragment();
//        try {
//            byte[] b = Base64.decode(img, Base64.DEFAULT);
//            fragment.image = BitmapFactory.decodeByteArray(b, 0, b.length);
//        }catch(Exception e){
//            fragment.image = null;
//        }
        fragment.position = p;
        fragment.visibility = v;
        fragment.name = n;
        fragment.entryCode = e;
        fragment.image = i;
        fragment.teamName = t;
        return fragment;
    }
    private int position = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if(savedInstanceState.containsKey("teamName"))
                teamName = savedInstanceState.getString("teamName");
            name = savedInstanceState.getString("name");
            entryCode = savedInstanceState.getString("entryCode");
        }
    }

    private void init(LinearLayout layout){
        if(visibility == 1){
            layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
        }
        if(position == 0) {
            ((TextView)layout.findViewById(R.id.display_team_name)).setText(teamName);
            ((TextView)layout.findViewById(R.id.display_team_name)).setMovementMethod(new ScrollingMovementMethod());
            ((EditText)layout.findViewById(R.id.team_name)).setText(teamName);
        }else{
            ((TextView)layout.findViewById(R.id.display_name)).setText(name);
            ((TextView)layout.findViewById(R.id.display_entry_code)).setText(entryCode);
            if(image!=null && !image.isEmpty())
                ((ImageView)layout.findViewById(R.id.img)).setImageBitmap(decodeBase64(image));
            ((EditText)layout.findViewById(R.id.name)).setText(name);
            ((EditText)layout.findViewById(R.id.entryCode)).setText(entryCode);
        }
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
        l = layout;
        init(layout);
        setOnClickListeners(layout);
        return layout;
    }
    private boolean isInvalid = false;
    
    private void setOnClickListeners(final LinearLayout layout){

        layout.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0) {
                    Log.d("save","Btn Pressed!");
                    String entryCode = ((EditText) layout.findViewById(R.id.entryCode)).getText().toString().trim();
                    String name = ((EditText) layout.findViewById(R.id.name)).getText().toString().trim();
                    Log.d("save_data",String.format("entry number = %s, name = %s",entryCode,name));
                    boolean isEntryCodeValid = InputValidator.validateEntryCode(entryCode);
                    boolean isNameValid = InputValidator.validateName(name);
                    if (!entryCode.isEmpty() && !name.isEmpty()) {
                        //boolean isInvalid = false;
                        //isInvalid = false; //cannot do this
                        if (!isEntryCodeValid) {
                            ((EditText) layout.findViewById(R.id.entryCode)).setError("Invalid entry no!");
                            isInvalid = true;
                        }
                        if (!isNameValid) {
                            ((EditText) layout.findViewById(R.id.name)).setError("Invalid name!");
                            isInvalid = true;
                        }
                        if (isInvalid) return;
                        MainActivity.names[position] = name;
                        MainActivity.entryCodes[position] = entryCode;
                        ((TextView) layout.findViewById(R.id.display_entry_code)).setText(entryCode);
                        ((TextView) layout.findViewById(R.id.display_name)).setText(name);
                        MainActivity.visibility[position] = 1;
                        hideKeyboard(v); //hidden keyboard
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
                    } else {
                        if(name.isEmpty()) {
                            ((EditText) layout.findViewById(R.id.name)).setError("Name cannot be empty!");
                            //TODO remove this error once user enters correct enry number and details are fetched from ldap.
                        }
                        if(entryCode.isEmpty()) {
                            ((EditText) layout.findViewById(R.id.entryCode)).setError("Entry Number cannot be empty!");
                        } else {
                            if(!isEntryCodeValid)
                                ((EditText) layout.findViewById(R.id.entryCode)).setError("Invalid entry no!");
                        }
                    }
                }else{
                    String teamName = ((EditText) layout.findViewById(R.id.team_name)).getText().toString().trim();
                    if(!teamName.isEmpty()){
                        MainActivity.teamName = teamName;
                        ((TextView) layout.findViewById(R.id.display_team_name)).setText(teamName);
                        MainActivity.visibility[position] = 1;
                        hideKeyboard(v); //hidden keyboard
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
                    }
                }
            }
        });
        layout.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.visibility[position] = 0;
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

    private void fetchAndEditStudentDetails(final String entryCode, final EditText entryNumBox, final EditText nameBox, final View okBttn, final CircularImageView personImgView){
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
                                isInvalid = false;
                                entryNumBox.setText(studentDataJson.getString("entryNumber"));
                                nameBox.setText(studentDataJson.getString("name"));
                                if (studentDataJson.has("img")) {
                                    String img = studentDataJson.getString("img");
                                    MainActivity.images[position] = img;
                                    personImgView.setImageBitmap(decodeBase64(img));
                                    personImgView.setBorderColor(Color.parseColor("#ff3C16"));
                                } else {
                                    MainActivity.images[position] = "";
                                    personImgView.setImageResource(R.mipmap.ic_launcher);
                                    personImgView.setBorderColor(Color.parseColor("#e7e7e7"));
                                }
                                //TODO: close keyboard. Following approaches not working!
                                //okBttn.requestFocus();
                                //personImgView.clearFocus();
                                okBttn.performClick();
                            } else {
                                isInvalid = true;
                                entryNumBox.setError("Invalid entry no!");
                            }
                            ;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new LdapFetcher.ldapRequestErrorHandler() {
                    @Override
                    public void handle(VolleyError error) {
                        if (!entryCode.isEmpty()) {
                            if (InputValidator.validateEntryCode(entryCode)) {
                                isInvalid = false;
                            }
                        }
                    }
                }
        ); //TODO indent properly
    }
    private Bitmap decodeBase64(String i){
        byte[] b = Base64.decode(i, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(position==0) {
            String t = ((EditText) l.findViewById(R.id.team_name)).getText().toString();
            outState.putString("teamName", t);
        }
        else {
            String n = ((EditText) l.findViewById(R.id.name)).getText().toString();
            String e = ((EditText) l.findViewById(R.id.entryCode)).getText().toString();
            outState.putString("name", n);
            outState.putString("entryCode", e);
        }
    }
    private int dp2Pix(float i){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }

    private void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
