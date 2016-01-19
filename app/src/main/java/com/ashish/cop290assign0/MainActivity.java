package com.ashish.cop290assign0;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ashish.cop290assign0.data.FormData;
import com.ashish.cop290assign0.utils.LdapFetcher;
import com.ashish.cop290assign0.utils.PostRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static LdapFetcher mLdapFetcher;
    public static PostRequest mPostRequest;

    public static FormData mFormData;
    static ViewPager pager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt("currentPage"));
            mFormData = (FormData) savedInstanceState.getSerializable("formData");
        }
        adapter.setVals(4, mFormData);
        pager.setAdapter(adapter);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        mLdapFetcher = new LdapFetcher(requestQueue);
        mPostRequest = new PostRequest(requestQueue);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", pager.getCurrentItem());
        outState.putSerializable("formData",mFormData);
    }

    //Initializing UI components
    private void init(){
        mFormData = FormData.initialize();
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    //Adds asterisk to editTexts using SpannableString(used for selective formatting of strings ex. color,on click url).
//    private void addRedAsterisk(EditText[] e_array){
//        for(EditText e : e_array) {
//            String text = e.getHint().toString();
//            String asterisk = " *";
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(text);
//            int start = builder.length();
//            builder.append(asterisk);
//            int end = builder.length();
//            builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            e.setHint(builder);
//        }
//    }




   // Extracts and returns all the required data from the editTexts in Map
    private static Map<String, String> getData(){
        Map<String, String> data = new HashMap<String, String>();
        data.put("teamname", mFormData.getTeamName());
        data.put("entry1", mFormData.getMember(1).getEntryNumber());
        data.put("name1", mFormData.getMember(1).getName());
        data.put("entry2", mFormData.getMember(2).getEntryNumber());
        data.put("name2", mFormData.getMember(2).getName());
        data.put("entry3", mFormData.getMember(3).getEntryNumber());
        data.put("name3", mFormData.getMember(3).getName());
        //Checking for input errors
        if(!isValidInput(mFormData))
            data = null;
        return data;
    }

    private static boolean isValidInput(FormData formData) {
        return isValidInput(formData.getTeamName(),
                mFormData.getMember(1).getEntryNumber(),mFormData.getMember(1).getName(),
                mFormData.getMember(2).getEntryNumber(),mFormData.getMember(2).getName(),
                mFormData.getMember(3).getEntryNumber(),mFormData.getMember(3).getName());
    }

    //Checks for valid input and shows error accordingly.
    private static boolean isValidInput(String teamName, String entry1,
                               String name1,String entry2,
                               String name2,String entry3,String name3){
        if(teamName.isEmpty()){
            pager.setCurrentItem(0, true);
            ((EditText)pager.getChildAt(0).findViewById(R.id.team_name)).setError("Team name can't be empty!");
            return false;
        }
        if(entry1.isEmpty()) {
            pager.setCurrentItem(1, true);
            ((EditText)pager.getChildAt(1).findViewById(R.id.entryCode)).setError("Entry1 can't be empty!");
            return false;
        }
        if(name1.isEmpty()) {
            pager.setCurrentItem(1, true);
            ((EditText)pager.getChildAt(1).findViewById(R.id.name)).setError("Name1 can't be empty!");
            return false;
        }
        if(entry2.isEmpty()){
            pager.setCurrentItem(2, true);
            ((EditText)pager.getChildAt(2).findViewById(R.id.entryCode)).setError("Entry2 can't be empty!");
            return false;
        }
        if(name2.isEmpty()){
            pager.setCurrentItem(2, true);
            ((EditText)pager.getChildAt(2).findViewById(R.id.name)).setError("Name2 can't be empty!");
            return false;
        }
        if(entry3.isEmpty() && !name3.isEmpty()){
            pager.setCurrentItem(3, true);
            ((EditText)pager.getChildAt(3).findViewById(R.id.entryCode)).setError("Name3 entered, please enter entry for this name!");
            return false;
        }
        if(name3.isEmpty() && !entry3.isEmpty()){
            pager.setCurrentItem(3,true);
            ((EditText)pager.getChildAt(3).findViewById(R.id.name)).setError("Entry3 entered, please enter name for this entry!");
            return false;
        }
        return true;
    }
    //Called on submit button click
    public static void onSubmit(final View v){
        Map<String,String> data = getData();
        if(data!=null) {
            //Creating a progressDialog to shows while the data is being posted.
            final ProgressDialog pDialog = createProgressDialog(v,"Please wait", "Sending data to the server.....");
            Log.d("onSubmit", data.toString());
            //posting to the server
            mPostRequest.post(Config.SERVER_URL, getData(),
                    new PostRequest.ServerResponseHandler() {
                        @Override
                        public void handle(String response) {
                            //TODO
                            pDialog.dismiss();
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Data not posted!"))
                                    make_dialog(v,"Data not posted!", "Some required fields are missing!","Ok");
                                else if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("User Already Registered"))
                                    make_dialog(v,"Data not posted!","One or more users with given details have already registered.","Ok");
                                else if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Registration completed")) {
                                    make_dialog(v, "Data posted!", "Registration completed", "Ok");
                                    //resetTextBoxes();
                                }
                            } catch (JSONException jsonException) {
                                make_dialog(v,"Umm...","Unexpected response from the server, contact server administrator!","Ok");
                            } catch (Exception e) {
                                make_dialog(v,"Uh-oh","Something bad happened. Might be aliens!","Ok");
                            }
                        }
                    },
                    new PostRequest.ErrorHandler(){
                        @Override
                        public void handle(VolleyError error) {
                            pDialog.dismiss();
                            make_dialog(v,"Uh-oh","Looks like you're not connected to the internet.Please check your internet connection and try again.","Ok");
                        }
                    }
            );
        } else {
            //data is null
            //some input is invalid
            //TODO handle invalid input case
        }
    }


    //Creates a progressDialog
    private static ProgressDialog createProgressDialog(View v,String title,String description){
        ProgressDialog progressDialog = new ProgressDialog(v.getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(description);
        progressDialog.show();
        return progressDialog;
    }

    //creates and shows a custom dialog
    private static void make_dialog(View v,String title,String msg,String button_text){
        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog_layout);
        //dialog.setCancelable(false);
        ((TextView)dialog.findViewById(R.id.title_text)).setText(title);
        ((TextView)dialog.findViewById(R.id.error_text)).setText(msg);
        ((Button)dialog.findViewById(R.id.error_button)).setText(button_text);
        (dialog.findViewById(R.id.error_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
