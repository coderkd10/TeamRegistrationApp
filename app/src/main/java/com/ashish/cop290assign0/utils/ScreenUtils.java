package com.ashish.cop290assign0.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Collection of static methods to manipulate objects on the screen / display.
 * @author Abhishek Kedia
 */
public class ScreenUtils {
    public static String getStringFromEditText(View v, int id) {
        try {
            return ((EditText) v.findViewById(id)).getText().toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static void setTextInEditText(View v, int id, CharSequence text) {
        ((EditText) v.findViewById(id)).setText(text);
    }

    public static void setErrorInEditText(View v, int id, CharSequence error) {
        ((EditText) v.findViewById(id)).setError(error);
    }

    public static void removeErrorFromEditText(View v, int id) {
        setTextInEditText(v,id,null);
    }

    public static void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    public static Bitmap base64StringToBitmap(String i){
        byte[] b = Base64.decode(i, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    //Adds asterisk to editTexts using SpannableString(used for selective formatting of strings ex. color,on click url).
    public static void addRedAsteriskToEditText(EditText e) {
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
    public static void addRedAsteriskToEditText(View v, int id) {
        addRedAsteriskToEditText((EditText) v.findViewById(id));
    }

}