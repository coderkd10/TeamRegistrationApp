package com.ashish.cop290assign0;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.ashish.cop290assign0.data.Member;
import com.ashish.cop290assign0.utils.InputValidator;
import com.ashish.cop290assign0.utils.LdapFetcher;
import com.ashish.cop290assign0.utils.ScreenUtils;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

public final class MemberFragment extends Fragment {
    boolean isInvalidatedByLdap;
    private int position = 0;
    private static final String TAG = MemberFragment.class.getSimpleName();

    @Override
    public String toString() {
        return String.format("%s{position:%d; isInvalidatedByLdap:%b}",TAG,position,isInvalidatedByLdap);
    }

    public static MemberFragment newInstance(int position) {
        Log.d(TAG, String.format("newInstance called. position:%d",position));
        MemberFragment fragment = new MemberFragment();
        fragment.isInvalidatedByLdap = false;
        fragment.position = position;
        return fragment;
    }

    private Member getMember() {
        return MainActivity.mFormData.getMember(position);
    }
    private boolean getIsfilled() {
        return MainActivity.mFormData.getIsFilled(position);
    }
    private void setIsfilled(boolean isFilled) {
        MainActivity.mFormData.setIsFilled(position,isFilled);
    }
    private String getFilledEntryNumber() {
        return ScreenUtils.getStringFromTextView(getView(), R.id.entryCode);
    }
    private String getFilledName() {
        return ScreenUtils.getStringFromTextView(getView(), R.id.name);
    }
    private void setEntryNumber(String entryNumber) {
        ScreenUtils.setTextInTextView(getView(), R.id.entryCode, entryNumber);
    }
    private void setName(String name) {
        ScreenUtils.setTextInTextView(getView(), R.id.name, name);
    }
    private void setImage(Bitmap image) {
        try {
            CircularImageView memberImageView = (CircularImageView) getView().findViewById(R.id.img);
            if (image != null)
                memberImageView.setImageBitmap(image);
            else
                memberImageView.setImageResource(R.mipmap.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setImageBorder(int color) {
        try {
            ((CircularImageView) getView().findViewById(R.id.img)).setBorderColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void markRequiredFields(){
        if (position != 3) {
            ScreenUtils.addRedAsteriskToEditText(getView(), R.id.entryCode);
            ScreenUtils.addRedAsteriskToEditText(getView(), R.id.name);
        }
    }
    private void setEmptyEntryNumberError() {
        ScreenUtils.setErrorInEditText(getView(), R.id.entryCode, "Entry number can't be empty!");
    }
    private void setInvalidEntryNumberError() {
        ScreenUtils.setErrorInEditText(getView(), R.id.entryCode, "Invalid entry number!");
    }
    private void setEmptyNameError() {
        ScreenUtils.setErrorInEditText(getView(), R.id.name, "Name can't be empty!");
    }
    private void setInvalidNameError() {
        ScreenUtils.setErrorInEditText(getView(),R.id.name, "Invalid name!");
    }
    private boolean isValidUserInput() {
        boolean isValid = true;
        if(getFilledEntryNumber().isEmpty()) {
            setEmptyEntryNumberError();
            isValid = false;
        }
        if(getFilledName().isEmpty()) {
            setEmptyNameError();
            isValid = false;
        }
        if(!isValid)
            return false;
        if(!InputValidator.isValidEntryCodeStructure(getFilledEntryNumber())) {
            setInvalidEntryNumberError();
            isValid = false;
        }
        if(!InputValidator.isValidName(getFilledName())) {
            setInvalidNameError();
            isValid = false;
        }
        return isValid && !isInvalidatedByLdap;
    }
    private void saveFilledDetailsToForm() {
        getMember().setEntryNumber(getFilledEntryNumber()).setName(getFilledName());
    }
    private void displayDetails(String entryNumber, String name) {
        ScreenUtils.setTextInTextView(getView(),R.id.display_entry_code,entryNumber);
        ScreenUtils.setTextInTextView(getView(), R.id.display_name, name);
    }
    private void displayFilledDetails() {
        displayDetails(getFilledEntryNumber(), getFilledName());
    }
    private void switchToEditDetailsMode() {
        try {
            if (position == 2 || position == 3) {
                getView().findViewById(R.id.submit_bttn).setVisibility(View.GONE);
            }
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
            if (position == 2 || position == 3)
                getView().findViewById(R.id.submit_bttn).setVisibility(View.VISIBLE);
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
        outState.putInt("position", position);
        if(!getIsfilled()) {
            if(!getFilledEntryNumber().isEmpty())
                outState.putString("filledEntryNumber",getFilledEntryNumber());
            if(!getFilledName().isEmpty())
                outState.putString("filledName",getFilledName());
        }
        Log.d(TAG, String.format("onSaveInstance called. position:%d; isInvalidatedByLdap:%b; outState:%s", position, isInvalidatedByLdap, outState));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, String.format("[%d] onCreate called. saveInstanceState:%s", position, savedInstanceState));
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.position = savedInstanceState.getInt("position");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, String.format("[%d] onCreateView called. savedInstanceState:%s, inflater:%s, container:%s", position, savedInstanceState, inflater, container));
        View view = inflater.inflate(R.layout.student_info_layout, null);
        ScreenUtils.setTextInTextView(view, R.id.member_no, "#" + position);
        addEntryNumberOnTextChangeListener(view);
        setOnClickListeners(view);
        return view;
    }

    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        Log.d(TAG, String.format("[%d] onActivityCreated called. savedInstanceState:%s", position, savedInstanceState));
        super.onActivityCreated(savedInstanceState);
        markRequiredFields();
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey("filledEntryNumber"))
                setEntryNumber(savedInstanceState.getString("filledEntryNumber"));
            else
                setEntryNumber(getMember().getEntryNumber());
            if (savedInstanceState.containsKey("filledName"))
                setName(savedInstanceState.getString("filledName"));
            else
                setName(getMember().getName());
        }
        displayDetails(getMember().getEntryNumber(), getMember().getName());
        if(getMember().getImage()!=null) {
            setImage(getMember().getImage());
            setImageBorder(Color.parseColor("#ff3C16"));
        }
        updateViewMode();
    }

    private void onSaveFilledDetails() {
        Log.i(TAG,String.format("[%d] save_data. entered:{%s,%s}; isInvalidatedByLdap:%b",position,getFilledEntryNumber(),getFilledName(),isInvalidatedByLdap));
        if(!isValidUserInput()) {
            Log.i(TAG,String.format("[%d] save_data. Not saved due to invalid user details",position));
            return;
        }
        saveFilledDetailsToForm();
        setIsfilled(true);
        displayFilledDetails();
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
        if(position != 0) {
            view.findViewById(R.id.submit_bttn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.onSubmit(v);
                }
            });
        }
    }

    private void addEntryNumberOnTextChangeListener(final View layout){
        final EditText entryNumberBox = ((EditText)layout.findViewById(R.id.entryCode)); //also entry number text box
        final EditText nameBox = (EditText) layout.findViewById(R.id.name);
        final View saveDataButton = layout.findViewById(R.id.save_data);
        final CircularImageView personImgView = (CircularImageView) layout.findViewById(R.id.img);

        entryNumberBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                //TODO: play here
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //TODO: play here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO play here
                //Log -> s,start,before,count
                isInvalidatedByLdap = false;
                if (InputValidator.isValidEntryCodeStructure(entryNumberBox.getText().toString()) && count < 11)
                    fetchAndEditStudentDetails(entryNumberBox.getText().toString(), entryNumberBox, nameBox, saveDataButton, personImgView);
                if (entryNumberBox.getText().toString().length() == 11) {
                    if (!InputValidator.isValidEntryCodeStructure(entryNumberBox.getText().toString())) {
                        //isFilled = false; //TODO try & remove this
                        entryNumberBox.setError("Invalid entry no!");
                    }
//                    else
//                        isFilled = true;    //TODO try & remove this
                }
            }
        });
        //entryNumberBox.removeTextChangedListener();
    }

    private void fetchAndEditStudentDetails(final String entryCode, final EditText entryNumBox, final EditText nameBox, final View okBttn, final CircularImageView personImgView){
        /**
         * TODO
         * 1. Fetch Details of student with entry number `entryCode`
         * 2. Change picture received according to the picture received
         * 3. Change name according to name received
         * 4. Click done button
         */
        MainActivity.mLdapFetcher.getAndHandleStudentDetails(entryCode,
                new LdapFetcher.studentJsonDataHandler() {
                    @Override
                    public void onGetJson(JSONObject studentDataJson) {
                        try {
                            if (studentDataJson.getBoolean("isValid")) {
                                //isFilled = true;
                                isInvalidatedByLdap = false;
                                nameBox.setError(null); //remove if there is any error marked in name box
                                entryNumBox.setText(studentDataJson.getString("entryNumber"));
                                nameBox.setText(studentDataJson.getString("name"));
                                if (studentDataJson.has("img")) {
                                    Bitmap img = ScreenUtils.base64StringToBitmap(studentDataJson.getString("img"));
                                    //MainActivity.images[position] = img;
                                    //MainActivity.mFormData.getMember(position).setImage(img);
                                    getMember().setImage(img);
                                    personImgView.setImageBitmap(getMember().getImage());
                                    personImgView.setBorderColor(Color.parseColor("#ff3C16"));
                                } else {
                                    //MainActivity.images[position] = "";
                                    //MainActivity.mFormData.getMember(position).setImage("");
                                    getMember().setImage(null);
                                    personImgView.setImageResource(R.mipmap.ic_launcher);
                                    personImgView.setBorderColor(Color.parseColor("#ffffff"));
                                }
                                okBttn.performClick();
                            } else {
                                isInvalidatedByLdap = true;
                                setInvalidEntryNumberError();
                            }
                            ;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new LdapFetcher.ldapRequestErrorHandler() {
                    @Override
                    public void handle(VolleyError error) {
//                        if (!entryCode.isEmpty()) {
//                            if (InputValidator.isValidEntryCodeStructure(entryCode)) {
//                                isFilled = true;
//                            }
//                        }
                    }
                }
        ); //TODO indent properly
    }

}
