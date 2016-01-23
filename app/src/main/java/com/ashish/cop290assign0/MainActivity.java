package com.ashish.cop290assign0;

import android.app.ProgressDialog;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ashish.cop290assign0.config.Config;
import com.ashish.cop290assign0.data.FormData;
import com.ashish.cop290assign0.mainActivityFragment.ViewPagerAdapter;
import com.ashish.cop290assign0.utils.LdapFetcher;
import com.ashish.cop290assign0.utils.PostRequest;
import com.ashish.cop290assign0.utils.ScreenUtils;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static LdapFetcher mLdapFetcher;
    public static PostRequest mPostRequest;

    public static FormData mFormData;
    static ViewPager pager;
    static ViewPagerAdapter adapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,String.format("onCreate called. savedInstanceState:%s",savedInstanceState));
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
        outState.putSerializable("formData", mFormData);
        Log.d(TAG, String.format("onSaveInstanceState called. outState:%s", outState));
    }

    //Initializing UI components
    private void init(){
        mFormData = FormData.initialize();
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

//    private static boolean isAllFilled() {
//        for(int i=0; i<=2; i++) {
//            if(!mFormData.getIsFilled(i)) {
//                pager.setCurrentItem(1, true);
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private static boolean isValidInput(FormData formData) {
//        return isValidInput(formData.getTeamName(),
//                mFormData.getMember(1).getEntryNumber(),mFormData.getMember(1).getName(),
//                mFormData.getMember(2).getEntryNumber(),mFormData.getMember(2).getName(),
//                mFormData.getMember(3).getEntryNumber(),mFormData.getMember(3).getName());
//    }
//
//    //TODO modularize
//    //Checks for valid input and shows error accordingly.
//    private static boolean isValidInput(String teamName, String entry1,
//                               String name1,String entry2,
//                               String name2,String entry3,String name3){
//        if(teamName.isEmpty()){
//            pager.setCurrentItem(0, true);
//            ((EditText)pager.getChildAt(0).findViewById(R.id.team_name)).setError("Team name can't be empty!");
//            return false;
//        }
//        if(entry1.isEmpty()) {
//            pager.setCurrentItem(1, true);
//            ((EditText)pager.getChildAt(1).findViewById(R.id.entryCode)).setError("Entry1 can't be empty!");
//            return false;
//        }
//        if(name1.isEmpty()) {
//            pager.setCurrentItem(1, true);
//            ((EditText)pager.getChildAt(1).findViewById(R.id.name)).setError("Name1 can't be empty!");
//            return false;
//        }
//        if(entry2.isEmpty()){
//            pager.setCurrentItem(2, true);
//            ((EditText)pager.getChildAt(2).findViewById(R.id.entryCode)).setError("Entry2 can't be empty!");
//            return false;
//        }
//        if(name2.isEmpty()){
//            pager.setCurrentItem(2, true);
//            ((EditText)pager.getChildAt(2).findViewById(R.id.name)).setError("Name2 can't be empty!");
//            return false;
//        }
//        if(entry3.isEmpty() && !name3.isEmpty()){
//            pager.setCurrentItem(3, true);
//            ((EditText)pager.getChildAt(3).findViewById(R.id.entryCode)).setError("Name3 entered, please enter entry for this name!");
//            return false;
//        }
//        if(name3.isEmpty() && !entry3.isEmpty()){
//            pager.setCurrentItem(3,true);
//            ((EditText)pager.getChildAt(3).findViewById(R.id.name)).setError("Entry3 entered, please enter name for this entry!");
//            return false;
//        }
//        return true;
//    }

    public static boolean isCompletelyFilled() {
        boolean isComplete = true;
        int incompleteFragmentMinIndex=0;
        for(int fragmentIndex = 3; fragmentIndex >= 0; fragmentIndex--) {
            try {
                if (!adapter.getFragment(fragmentIndex).isCompletelyFilled()) {
                    isComplete = false;
                    incompleteFragmentMinIndex = fragmentIndex;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(!isComplete) {
            pager.setCurrentItem(incompleteFragmentMinIndex,true);
        }
        return isComplete;
    }

    //Called on submit button click
    public static void onSubmit(final View v){
        Log.d(MainActivity.class.getSimpleName(),"Submit pressed. mFormData="+mFormData);
////        if(!isValidInput(mFormData))
////            return;
//
//        Map<String,String> data = mFormData.toMap();
//        if(!isAllFilled())
//            return;

        if(!isCompletelyFilled())
            return;

        Map<String,String> data = mFormData.toMap();
        if(data!=null) {
            //Creating a progressDialog to shows while the data is being posted.
            final ProgressDialog pDialog = ScreenUtils.createProgressDialog(v, "Please wait", "Sending data to the server.....");
            Log.d("onSubmit", data.toString());
            //posting to the server
            mPostRequest.post(Config.SERVER_URL, data,
                    new PostRequest.ServerResponseHandler() {
                        @Override
                        public void handle(String response) {
                            //TODO
                            pDialog.dismiss();
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Data not posted!"))
                                    ScreenUtils.makeDialog(v, "Data not posted!", "Some required fields are missing!", "Ok");
                                else if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("User Already Registered"))
                                    ScreenUtils.makeDialog(v, "Data not posted!", "One or more users with given details have already registered.", "Ok");
                                else if(jsonResponse.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Registration completed")) {
                                    ScreenUtils.makeDialog(v, "Data posted!", "Registration completed", "Ok");
                                    //resetTextBoxes();
                                }
                            } catch (JSONException jsonException) {
                                ScreenUtils.makeDialog(v, "Umm...", "Unexpected response from the server, contact server administrator!", "Ok");
                            } catch (Exception e) {
                                ScreenUtils.makeDialog(v, "Uh-oh", "Something bad happened. Might be aliens!", "Ok");
                            }
                        }
                    },
                    new PostRequest.ErrorHandler(){
                        @Override
                        public void handle(VolleyError error) {
                            pDialog.dismiss();
                            ScreenUtils.makeDialog(v, "Uh-oh", "Looks like you're not connected to the internet.Please check your internet connection and try again.", "Ok");
                        }
                    }
            );
        } else {
            //data is null
            //some input is invalid
            //TODO handle invalid input case
        }
    }


}
