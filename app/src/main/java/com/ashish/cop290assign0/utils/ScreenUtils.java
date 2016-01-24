package com.ashish.cop290assign0.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ashish.cop290assign0.R;
import com.ashish.cop290assign0.data.FormData;
import com.ashish.cop290assign0.data.Member;
import com.github.siyamed.shapeimageview.CircularImageView;

/**
 * Collection of static methods to manipulate objects on the screen / display.
 * @author Abhishek Kedia
 */
public class ScreenUtils {
    public static String getStringFromTextView(View v, int id) {
        try {
            return ((TextView) v.findViewById(id)).getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setTextInTextView(View v, int id, CharSequence text) {
        try {
            ((TextView) v.findViewById(id)).setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setErrorInEditText(View v, int id, CharSequence error) {
        try {
            ((EditText) v.findViewById(id)).setError(error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeErrorFromEditText(View v, int id) {
        setErrorInEditText(v, id, null);
    }

    public static void setMemberImageInCircularImageView(View view, int id, Bitmap memberImage) {
        try {
            CircularImageView memberImageView = (CircularImageView) view.findViewById(id);
            if (memberImage != null)
                memberImageView.setImageBitmap(memberImage);
            else
                memberImageView.setImageResource(R.mipmap.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setImageBorderInCircularImageView(View view, int id, int color) {
        try {
            ((CircularImageView) view.findViewById(id)).setBorderColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setMemberImageAndBorderInCircularImageView(View view, int id, Bitmap memberImage) {
        setMemberImageInCircularImageView(view, id, memberImage);
        if(memberImage == null)
            setImageBorderInCircularImageView(view,id,Color.parseColor("#ffffff"));
        else
            setImageBorderInCircularImageView(view,id,Color.parseColor("#ff3C16"));
    }

    public static void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] base64StringToByteArray(String base64String) {
        try {
            return Base64.decode(base64String, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap base64StringToBitmap(String base64String) {
        byte[] b = base64StringToByteArray(base64String);
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    //Adds asterisk to editTexts using SpannableString(used for selective formatting of strings ex. color,on click url).
    public static void addRedAsteriskToEditText(EditText editText) {
        try {
            String text = editText.getHint().toString();
            String asterisk = " *";
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(text);
            int start = builder.length();
            builder.append(asterisk);
            int end = builder.length();
            builder.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.setHint(builder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addRedAsteriskToEditText(View v, int id) {
        addRedAsteriskToEditText((EditText) v.findViewById(id));
    }

    //Creates a progressDialog
    public static ProgressDialog createProgressDialog(View view, String title, String description){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(description);
        progressDialog.show();
        return progressDialog;
    }


    //creates and shows a custom dialog
    //TODO make the header of dialog colored according to success or failure. RED if failed, green if success.
    public static void makeDialog(View view, String title, String msg, String buttonText){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //ask why??
        dialog.setContentView(R.layout.error_dialog_layout);
        dialog.setCancelable(false);
        ((TextView)dialog.findViewById(R.id.title_text)).setText(title);
        ((TextView)dialog.findViewById(R.id.error_text)).setText(msg);
        ((Button)dialog.findViewById(R.id.error_button)).setText(buttonText);
        (dialog.findViewById(R.id.error_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void setTextInTextView(Dialog v, int id, CharSequence text) {
        try {
            ((TextView) v.findViewById(id)).setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setMemberImageInCircularImageView(Dialog view, int id, Bitmap memberImage) {
        try {
            CircularImageView memberImageView = (CircularImageView) view.findViewById(id);
            if (memberImage != null)
                memberImageView.setImageBitmap(memberImage);
            else
                memberImageView.setImageResource(R.mipmap.ic_launcher);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setImageBorderInCircularImageView(Dialog view, int id, int color) {
        try {
            ((CircularImageView) view.findViewById(id)).setBorderColor(color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setMemberImageAndBorderInCircularImageView(Dialog view, int id, Bitmap memberImage) {
        setMemberImageInCircularImageView(view,id,memberImage);
        if(memberImage == null)
            setImageBorderInCircularImageView(view,id,Color.parseColor("#ffffff"));
        else
            setImageBorderInCircularImageView(view,id,Color.parseColor("#ff3C16"));
    }
    //TODO move to a new class
    public static void makeConfirmationDialog(View view, FormData formData, final View.OnClickListener submitClickHandler) {
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_dialog_layout);
        dialog.setCancelable(false);

//        dialog.show();
//        view = dialog.getCurrentFocus(); //TODO improve this
        setTextInTextView(dialog, R.id.detail1, formData.getMember(1).toFormattedString());
        setMemberImageAndBorderInCircularImageView(dialog, R.id.img1, formData.getMember(1).getImageBitmap());
        setTextInTextView(dialog, R.id.detail2, formData.getMember(2).toFormattedString());
        setMemberImageAndBorderInCircularImageView(dialog, R.id.img2, formData.getMember(2).getImageBitmap());
        if(formData.getIsFilled(3)) {
            setTextInTextView(dialog,R.id.detail3,formData.getMember(3).toFormattedString());
            setMemberImageAndBorderInCircularImageView(dialog, R.id.img3, formData.getMember(3).getImageBitmap());
        } else {
            dialog.findViewById(R.id.student3_layout).setVisibility(View.GONE);
        }

        //set button listener
        //cancel button
        (dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        (dialog.findViewById(R.id.submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                submitClickHandler.onClick(v); //TODO improve
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
