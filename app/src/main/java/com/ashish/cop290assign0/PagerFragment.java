package com.ashish.cop290assign0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public final class PagerFragment extends Fragment {
    private String name,entryCode;
    private Bitmap image;
    public static PagerFragment newInstance() {
        PagerFragment fragment = new PagerFragment();
//        try {
//            byte[] b = Base64.decode(img, Base64.DEFAULT);
//            fragment.image = BitmapFactory.decodeByteArray(b, 0, b.length);
//        }catch(Exception e){
//            fragment.image = null;
//        }
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
        View child = inflater.inflate(
                R.layout.student_info_layout, null);
        layout.addView(child);

        setOnClickListeners(layout);
        addOnTextChangeListener((EditText)layout.findViewById(R.id.entryCode));
        return layout;
    }

    private void setOnClickListeners(final LinearLayout layout){

        layout.findViewById(R.id.save_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entryCode = ((EditText) layout.findViewById(R.id.entryCode)).getText().toString().trim();
                String name = ((EditText) layout.findViewById(R.id.name)).getText().toString().trim();
                boolean isEntryCodeValid = InputValidator.validateEntryCode(entryCode);
                boolean isNameValid = InputValidator.validateName(name);
                if (!entryCode.isEmpty() && !name.isEmpty()) {
                    if (!isEntryCodeValid) {
                        ((EditText) layout.findViewById(R.id.entryCode)).setError("Invalid entry no!");
                        return;
                    }
                    if (!isNameValid) {
                        ((EditText) layout.findViewById(R.id.name)).setError("Invalid name!");
                        return;
                    }
                    ((TextView) layout.findViewById(R.id.display_entry_code)).setText(entryCode);
                    ((TextView) layout.findViewById(R.id.display_name)).setText(name);
                    layout.findViewById(R.id.display_layout).setVisibility(View.VISIBLE);
                    layout.findViewById(R.id.input_layout).setVisibility(View.GONE);
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

    private void addOnTextChangeListener(final EditText editText){
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
                if (InputValidator.validateEntryCode(editText.getText().toString()))
                    fetchStudentDetails(editText.getText().toString());
            }
        });
    }
    private void fetchStudentDetails(String entryCode){

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
