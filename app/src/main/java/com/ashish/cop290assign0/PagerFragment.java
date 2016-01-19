package com.ashish.cop290assign0;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
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
import com.ashish.cop290assign0.data.Member;
import com.ashish.cop290assign0.utils.InputValidator;
import com.ashish.cop290assign0.utils.LdapFetcher;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

public final class PagerFragment extends Fragment {
    private Member member;
    private String teamName;
    private boolean isFilled;
    private int position = 0;

    public static PagerFragment newInstance(int position,boolean isFilled,String teamName, Member member) {
        PagerFragment fragment = new PagerFragment();
        fragment.position = position;
        fragment.isFilled = isFilled;
        fragment.member = new Member(member);
        fragment.teamName = teamName;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFilled", isFilled);
        outState.putInt("position",position);
        if(position==0) {
            String t = ((EditText) getView().findViewById(R.id.team_name)).getText().toString();
            outState.putString("teamName", t);
        }
        else {
            String n = ((EditText) getView().findViewById(R.id.name)).getText().toString();
            String e = ((EditText) getView().findViewById(R.id.entryCode)).getText().toString();
            outState.putSerializable("member",new Member(n,e));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            isFilled = savedInstanceState.getBoolean("isFilled");
            position = savedInstanceState.getInt("position");
            if(savedInstanceState.containsKey("teamName"))
                this.teamName = savedInstanceState.getString("teamName");
            else {
                this.member = (Member) savedInstanceState.getSerializable("member");
            }
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        View layout;
        if(position == 0) {
            layout = inflater.inflate(R.layout.team_input_layout, null);
        }
        else {
            layout = inflater.inflate(R.layout.student_info_layout, null);
            addOnTextChangeListener(layout);
            ((TextView) layout.findViewById(R.id.member_no)).setText("#" + position);
        }
        init(layout);
        setOnClickListeners(layout);
        return layout;
    }

    private void init(View layout){
        if(isFilled){
            layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
        }
        if(position == 0) {
            ((TextView)layout.findViewById(R.id.display_team_name)).setText(teamName);
            ((TextView)layout.findViewById(R.id.display_team_name)).setMovementMethod(new ScrollingMovementMethod());
            ((EditText)layout.findViewById(R.id.team_name)).setText(teamName);
        }else{
            ((TextView)layout.findViewById(R.id.display_name)).setText(member.getName());
            ((TextView)layout.findViewById(R.id.display_entry_code)).setText(member.getEntryNumber());
            if(member.getImage()!=null && !member.getImage().isEmpty())
                ((ImageView)layout.findViewById(R.id.img)).setImageBitmap(decodeBase64(member.getImage()));
            ((EditText)layout.findViewById(R.id.name)).setText(member.getName());
            ((EditText)layout.findViewById(R.id.entryCode)).setText(member.getEntryNumber());
        }
        isFilled = false;
    }



    private void setOnClickListeners(final View layout){

        layout.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position != 0) {
                    Log.d("save","Btn Pressed!");
                    String entryCode = ((EditText) layout.findViewById(R.id.entryCode)).getText().toString().trim();
                    String name = ((EditText) layout.findViewById(R.id.name)).getText().toString().trim();
                    boolean isEntryCodeValid = InputValidator.isValidEntryCodeStructure(entryCode);
                    boolean isNameValid = InputValidator.isValidName(name);
                    if (!entryCode.isEmpty() && !name.isEmpty()) {
                        if (!isEntryCodeValid) {
                            ((EditText) layout.findViewById(R.id.entryCode)).setError("Invalid entry no!");
                            isFilled = false;
                        }
                        if (!isNameValid) {
                            ((EditText) layout.findViewById(R.id.name)).setError("Invalid name!");
                            isFilled = false;
                        } else
                            ((EditText) layout.findViewById(R.id.name)).setError(null);
                        Log.d("--->>>", isFilled +"");
                        if (!isFilled) return;
                        Log.d("save_data",String.format("entry number = %s, name = %s",entryCode,name));
//                        MainActivity.names[position] = name;
//                        MainActivity.entryCodes[position] = entryCode;
                        MainActivity.mFormData.getMember(position).setName(name);
                        MainActivity.mFormData.getMember(position).setEntryNumber(entryCode);

                        ((TextView) layout.findViewById(R.id.display_entry_code)).setText(entryCode);
                        ((TextView) layout.findViewById(R.id.display_name)).setText(name);

                        //MainActivity.isFilled[position] = true;
                        MainActivity.mFormData.setIsFilled(position,true);

                        hideKeyboard(v); //hidden keyboard
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);

                        if(position == 2 || position == 3) {
                            layout.findViewById(R.id.submit_bttn).setVisibility(View.VISIBLE);
                        }

                    } else {
                        if(name.isEmpty()) {
                            ((EditText) layout.findViewById(R.id.name)).setError("Name cannot be empty!");
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
                        //MainActivity.teamName = teamName;
                        MainActivity.mFormData.setTeamName(teamName);
                        ((TextView) layout.findViewById(R.id.display_team_name)).setText(teamName);
                        //MainActivity.isFilled[position] = true;
                        MainActivity.mFormData.setIsFilled(position,true);
                        hideKeyboard(v); //hidden keyboard
                        layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                        layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
                    } else{
                        ((EditText) layout.findViewById(R.id.team_name)).setError("Team name can't be empty");
                    }
                }
            }
        });
        layout.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.isFilled[position] = false;
                MainActivity.mFormData.setIsFilled(position,false);

                if (position == 2 || position == 3) {
                    layout.findViewById(R.id.submit_bttn).setVisibility(View.GONE);
                }

                layout.findViewById(R.id.display_layout).setVisibility(View.GONE);
                layout.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
            }
        });
        if(position != 0) {
            layout.findViewById(R.id.submit_bttn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.onSubmit(v);
                }
            });
        }
    }

    private void addOnTextChangeListener(final View layout){
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
                if (InputValidator.isValidEntryCodeStructure(editText.getText().toString()) && count < 11)
                    fetchAndEditStudentDetails(editText.getText().toString(), editText, nameBox, okBttn, personImgView);
                if (editText.getText().toString().length() == 11) {
                    if (!InputValidator.isValidEntryCodeStructure(editText.getText().toString())) {
                        isFilled = false;
                        editText.setError("Invalid entry no!");
                    } else
                        isFilled = true;
                }
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
                                isFilled = true;
                                nameBox.setError(null); //remove if there is any error marked in name box
                                entryNumBox.setText(studentDataJson.getString("entryNumber"));
                                nameBox.setText(studentDataJson.getString("name"));
                                if (studentDataJson.has("img")) {
                                    String img = studentDataJson.getString("img");
                                    //MainActivity.images[position] = img;
                                    MainActivity.mFormData.getMember(position).setImage(img);
                                    personImgView.setImageBitmap(decodeBase64(img));
                                    personImgView.setBorderColor(Color.parseColor("#ff3C16"));
                                } else {
                                    //MainActivity.images[position] = "";
                                    MainActivity.mFormData.getMember(position).setImage("");
                                    personImgView.setImageResource(R.mipmap.ic_launcher);
                                    personImgView.setBorderColor(Color.parseColor("#ffffff"));
                                }
                                okBttn.performClick();
                            } else {
                                isFilled = false;
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
                            if (InputValidator.isValidEntryCodeStructure(entryCode)) {
                                isFilled = true;
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

    private int dp2Pix(float i){
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }

    private void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
