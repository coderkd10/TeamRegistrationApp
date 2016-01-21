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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ashish.cop290assign0.data.FormData;
import com.ashish.cop290assign0.data.Member;
import com.ashish.cop290assign0.utils.InputValidator;
import com.ashish.cop290assign0.utils.LdapFetcher;
import com.ashish.cop290assign0.utils.ScreenUtils;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONObject;

public final class MemberFragment extends Fragment {
    private Member member;
    boolean isFilled;
    boolean isInvalidatedByLdap;
    private int position = 0;

    public static MemberFragment newInstance(int position, Member member) {
        MemberFragment fragment = new MemberFragment();
        fragment.isFilled = false;
        fragment.isInvalidatedByLdap = false;
        fragment.position = position;
        fragment.member = member;
        return fragment;
    }

    private String getFilledEntryNumber() {
        return ScreenUtils.getStringFromEditText(getView(),R.id.entryCode);
    }
    private String getFilledName() {
        return ScreenUtils.getStringFromEditText(getView(),R.id.name);
    }
    private void setEntryNumber(String entryNumber) {
        ScreenUtils.setTextInEditText(getView(),R.id.entryCode,entryNumber);
    }
    private void setName(String name) {
        ScreenUtils.setTextInEditText(getView(),R.id.name,name);
    }
    private void setImage(Bitmap image) {
        CircularImageView memberImageView = (CircularImageView) getView().findViewById(R.id.img);
        if(image != null)
            memberImageView.setImageBitmap(image);
        else
            memberImageView.setImageResource(R.mipmap.ic_launcher);
    }
    private void setImageBorder(int color) {
        ((CircularImageView) getView().findViewById(R.id.img)).setBorderColor(color);
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
    private FormData saveDataToForm() {
        member.setEntryNumber(getFilledEntryNumber()).setName(getFilledName());
        FormData formData = MainActivity.mFormData;
        formData.setMember(position - 1, member);
        formData.setIsFilled(position - 1, isFilled);
        return formData;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putBoolean("isFilled", isFilled);
        outState.putBoolean("isInvalidatedByLdap", isInvalidatedByLdap);
        outState.putSerializable("member", member.setEntryNumber(getFilledEntryNumber()).setName(getFilledName()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.isFilled = savedInstanceState.getBoolean("isFilled");
            this.isInvalidatedByLdap = savedInstanceState.getBoolean("isInvalidatedByLdap");
            this.position = savedInstanceState.getInt("position");
            this.member = (Member) savedInstanceState.getSerializable("member");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.student_info_layout, null);
        addEntryNumberOnTextChangeListener(view);
        ((TextView) view.findViewById(R.id.member_no)).setText("#" + position);
        init(view);
        setOnClickListeners(view);
        return view;
    }

    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        ((TextView)view.findViewById(R.id.display_name)).setText(member.getName());
        ((TextView)view.findViewById(R.id.display_entry_code)).setText(member.getEntryNumber());
        if(member.getImage()!=null)
            setImage(member.getImage());
        setEntryNumber(member.getEntryNumber());
        setName(member.getName());
    }

    private void init(View view){
        if(isFilled){
            view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.input_layout).setVisibility(View.GONE);
            if(position == 2 || position == 3)
                view.findViewById(R.id.submit_bttn).setVisibility(View.VISIBLE);
        }
        if (position != 3) {
            ScreenUtils.addRedAsteriskToEditText(view, R.id.entryCode);
            ScreenUtils.addRedAsteriskToEditText(view, R.id.name);
        }
    }

    private void setOnClickListeners(final View view){
        view.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryCode = getFilledEntryNumber();
                String name = getFilledName();
                Log.i(MemberFragment.class.getSimpleName(),String.format("save_data %d pressed. entered:{%s,%s}; member:%s; isFilled:%b; isInvalidatedByLdap:%b",position,entryCode,name,member,isFilled,isInvalidatedByLdap));
                if(!isValidUserInput())
                    return;
                isFilled = true;
                saveDataToForm();
                //TODO write in a method
                ((TextView) view.findViewById(R.id.display_entry_code)).setText(entryCode);
                ((TextView) view.findViewById(R.id.display_name)).setText(name);
                ScreenUtils.hideKeyboard(v); //hidden keyboard
                view.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                view.findViewById(R.id.input_layout).setVisibility(View.GONE);
                if(position == 2 || position == 3) {
                    view.findViewById(R.id.submit_bttn).setVisibility(View.VISIBLE);
                }
            }
        });
        view.findViewById(R.id.edit_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilled = false;
                MainActivity.mFormData.setIsFilled(position, false);

                if (position == 2 || position == 3) {
                    view.findViewById(R.id.submit_bttn).setVisibility(View.GONE);
                }

                view.findViewById(R.id.display_layout).setVisibility(View.GONE);
                view.findViewById(R.id.input_layout).setVisibility(View.VISIBLE);
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
                                    member.setImage(img);
                                    personImgView.setImageBitmap(img);
                                    personImgView.setBorderColor(Color.parseColor("#ff3C16"));
                                } else {
                                    //MainActivity.images[position] = "";
                                    //MainActivity.mFormData.getMember(position).setImage("");
                                    member.setImage(null);
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
