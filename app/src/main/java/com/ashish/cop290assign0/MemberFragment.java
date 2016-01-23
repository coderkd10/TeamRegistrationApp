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
        return ScreenUtils.getStringFromTextView(getView(), R.id.entryCode).trim();
    }
    private String getFilledName() {
        return ScreenUtils.getStringFromTextView(getView(), R.id.name).trim();
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
        ScreenUtils.setErrorInEditText(getView(), R.id.name, "Invalid name!");
    }
    private void removeNameError() {
        ScreenUtils.removeErrorFromEditText(getView(),R.id.name);
    }
    private boolean isValidUserInput() {
        boolean isValid = true;
        if(getFilledEntryNumber().isEmpty()) {
            setEmptyEntryNumberError();
            isValid = false;
        } else if(!InputValidator.isValidEntryCodeStructure(getFilledEntryNumber())) {
            setInvalidEntryNumberError();
            isValid = false;
        }
        if(getFilledName().isEmpty()) {
            setEmptyNameError();
            isValid = false;
        } else if(!InputValidator.isValidName(getFilledName())) {
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
                    MainActivity.onSubmit(v); //TODO
                }
            });
        }
    }

    private void fillDetailsFromJson(JSONObject studentDataJson) {
        try {
            if (studentDataJson.getBoolean("isValid")) {
                removeNameError();
                setEntryNumber(studentDataJson.getString("entryNumber"));
                setName(studentDataJson.getString("name"));
                if(studentDataJson.has("img")) {
                    getMember().setImage(ScreenUtils.base64StringToBitmap(studentDataJson.getString("img"))); //TODO change
                    setImage(ScreenUtils.base64StringToBitmap(studentDataJson.getString("img")));
                    setImageBorder(Color.parseColor("#ff3C16"));
                } else {
                    getMember().setImage(null);
                    setImage(null);
                    setImageBorder(Color.parseColor("#ffffff"));
                }
                onSaveFilledDetails();
            } else {
                isInvalidatedByLdap = true;
                setInvalidEntryNumberError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntryNumberOnTextChangeListener(final View view){
        final EditText entryNumberEditText = ((EditText)view.findViewById(R.id.entryCode));
        if(entryNumberEditText == null)
            return;
        entryNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isInvalidatedByLdap = false;
                if (getFilledEntryNumber().length() != Config.ENTRY_NUMBER_LENGTH)
                    return;
                if(!InputValidator.isValidEntryCodeStructure(getFilledEntryNumber())) {
                    setInvalidEntryNumberError();
                    return;
                }
                if(count < 11) {
                    MainActivity.mLdapFetcher.getAndHandleStudentDetails(getFilledEntryNumber(), new LdapFetcher.studentJsonDataHandler() {
                        @Override
                        public void onGetJson(JSONObject studentDataJson) {
                            fillDetailsFromJson(studentDataJson);
                        }
                    });
                }
            }
        });
    }
}
