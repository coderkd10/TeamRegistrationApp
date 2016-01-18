package com.ashish.cop290assign0;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    //EditText teamNameTextBox,entry1TextBox,name1TextBox,entry2TextBox,name2TextBox,entry3TextBox,name3TextBox;
    public static String[] names,entryCodes,images;
    public static String teamName;
    public static LdapFetcher mLdapFetcher;
    public static PostRequest mPostRequest;
    public static boolean[] isFilled;
    static ViewPager pager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt("currentPage"));
            isFilled = savedInstanceState.getBooleanArray("isFilled");
            teamName = savedInstanceState.getString("teamName");
            names = savedInstanceState.getStringArray("names");
            entryCodes = savedInstanceState.getStringArray("entryCodes");
            images = savedInstanceState.getStringArray("images");
        }
        //adapter.setVals(4, teamName, names, entryCodes, images, visibility);
        adapter.setVals(4, FormData.initialize(teamName,names,entryCodes,images,isFilled));
        pager.setAdapter(adapter);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        mLdapFetcher = new LdapFetcher(requestQueue);
        mPostRequest = new PostRequest(requestQueue);
    }
//    private List<Bitmap> getDummyImages(){
//        images = new ArrayList<>();
//        images.add(null);
//        images.add(null);
//        images.add(null);
//        return images;
//    }
//    private List<String> getDummyNames(){
//        names = new ArrayList<>();
//        names.add("");
//        names.add("");
//        names.add("");
//        return names;
//    }
//    private List<String> getDummyEntryCodes(){
//        entryCodes = new ArrayList<>();
//        entryCodes.add("");
//        entryCodes.add("");
//        entryCodes.add("");
//        return entryCodes;
//    }
    //Initializing UI components
    private void init(){
        teamName = "";
        names = new String[]{"","","",""};
        entryCodes = new String[]{"","","",""};
        images = new String[]{"","","",""};
        isFilled = new boolean[]{false,false,false,false};
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", pager.getCurrentItem());
        outState.putBooleanArray("isFilled", isFilled);
        outState.putString("teamName", teamName);
        outState.putStringArray("names", names);
        outState.putStringArray("entryCodes", entryCodes);
        outState.putStringArray("images", images);
    }


   // Extracts and returns all the required data from the editTexts in an ArrayList
    private static Map<String, String> getData(){
        Map<String, String> data = new HashMap<String, String>();
        data.put("teamname", teamName);
        data.put("entry1", entryCodes[1]);
        data.put("name1", names[1]);
        data.put("entry2", entryCodes[2]);
        data.put("name2", names[2]);
        data.put("entry3", entryCodes[3]);
        data.put("name3", names[3]);
        //Checking for input errors
        if(!isValidInput(teamName, entryCodes[1], names[1], entryCodes[2], names[2], entryCodes[3], names[3]))
            data = null;
        return data;
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

    //Posting data to the server asynchronously
//    class sendDataToServer extends AsyncTask<String, Void, String> {
//		ArrayList<NameValuePair> data;
//        ProgressDialog progressDialog;
//		public sendDataToServer(ArrayList<NameValuePair> d,ProgressDialog p){
//			data = d;
//            progressDialog = p;
//		}
//        protected String doInBackground(String... urls) {
//            PostRequest request = new PostRequest(urls[0], data);
//            String result = request.post();
//            return result;
//        }
//
//        protected void onPostExecute(String res) {
//            try {
//                progressDialog.dismiss();
//                //Checking post request result
//                if(res.equalsIgnoreCase("internetNotAvailable")){
//                    make_dialog("Uh-oh","Looks like you're not connected to the internet.Please check your internet connection and try again.","Ok");
//                }
//                else if(res.toLowerCase().contains("exception")){
//                    make_dialog("Some error occured!","There was some error in posting the data,contact server administrator.","Ok");
//                }
//                else {
//                    JSONObject response = new JSONObject(res);
//                    //Checking post request response
//                    if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Data not posted!"))
//                        make_dialog("Data not posted!", "Some required fields are missing!","Ok");
//                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("User Already Registered"))
//                        make_dialog("Data not posted!","One or more users with given details have already registered.","Ok");
//                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Registration completed")) {
//                        make_dialog("Data posted!", "Registration completed", "Ok");
//                        //resetTextBoxes();
//                    }
//                    else
//                        make_dialog("Umm...","Unexpected response from the server.\nResponse : "+res,"Ok");
//                }
//
//            } catch (Exception e) {
//                make_dialog("Unable to send the data!","Please check your internet connection and try again.","Ok");
//            }
//        }
//    }
//    //to clear editText contents
//    private void resetTextBoxes(){
//        teamNameTextBox.setText("");
//        entry1TextBox.setText("");
//        name1TextBox.setText("");
//        entry2TextBox.setText("");
//        name2TextBox.setText("");
//        entry3TextBox.setText("");
//        name3TextBox.setText("");
//    }
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
