package com.ashish.cop290assign0;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    //EditText teamNameTextBox,entry1TextBox,name1TextBox,entry2TextBox,name2TextBox,entry3TextBox,name3TextBox;
    public static String[] names,entryCodes,images;
    public static String teamName;
    public static LdapFetcher mLdapFetcher;
    public static int[] visibility;
    ViewPager pager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(savedInstanceState != null) {
            pager.setCurrentItem(savedInstanceState.getInt("currentPage"));
            visibility = savedInstanceState.getIntArray("visibility");
            teamName = savedInstanceState.getString("teamName");
            names = savedInstanceState.getStringArray("names");
            entryCodes = savedInstanceState.getStringArray("entryCodes");
            images = savedInstanceState.getStringArray("images");
        }
        adapter.setVals(4, teamName, names, entryCodes, images, visibility);
        pager.setAdapter(adapter);
        CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(pager);
        mLdapFetcher = new LdapFetcher(getApplicationContext());
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
        names = new String[4];
        entryCodes = new String[4];
        images = new String[4];
        visibility = new int[4];
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
        outState.putIntArray("visibility", visibility);
        outState.putString("teamName", teamName);
        outState.putStringArray("names", names);
        outState.putStringArray("entryCodes", entryCodes);
        outState.putStringArray("images", images);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    //Extracts and returns all the required data from the editTexts in an ArrayList
//    private ArrayList<NameValuePair> getData(){
//        //Getting data from the editTexts
//        String teamName = teamNameTextBox.getText().toString();
//        String entry1 = entry1TextBox.getText().toString();
//        String name1 = name1TextBox.getText().toString();
//        String entry2 = entry2TextBox.getText().toString();
//        String name2 = name2TextBox.getText().toString();
//        String entry3 = entry3TextBox.getText().toString();
//        String name3 = name3TextBox.getText().toString();
//
//        //Adding data to ArrayList in NameValuePair form for sending to the server.
//        ArrayList<NameValuePair> data = new ArrayList<>();
//        data.add(new BasicNameValuePair("teamname", teamName));
//        data.add(new BasicNameValuePair("entry1", entry1));
//        data.add(new BasicNameValuePair("name1", name1));
//        data.add(new BasicNameValuePair("entry2", entry2));
//        data.add(new BasicNameValuePair("name2", name2));
//        data.add(new BasicNameValuePair("entry3", entry3));
//        data.add(new BasicNameValuePair("name3", name3));
//
//        //Checking for input errors
//        if(!isValidInput(teamName, entry1, name1, entry2, name2, entry3, name3))
//            data = null;
//        return data;
//    }

    //Checks for valid input and shows error accordingly.
//    private boolean isValidInput(String teamName, String entry1,
//                               String name1,String entry2,
//                               String name2,String entry3,String name3){
//        boolean result = true;
//        if(teamName.isEmpty()){
//            teamNameTextBox.setError("Team name can't be empty!");
//            result = false;
//        }
//        if(entry1.isEmpty()) {
//            entry1TextBox.setError("Entry1 can't be empty!");
//            result = false;
//        }
//        if(name1.isEmpty()) {
//            name1TextBox.setError("Name1 can't be empty!");
//            result = false;
//        }
//        if(entry2.isEmpty()){
//            entry2TextBox.setError("Entry2 can't be empty!");
//            result = false;
//        }
//        if(name2.isEmpty()){
//            name2TextBox.setError("Name2 can't be empty!");
//            result = false;
//        }
//        if(entry3.isEmpty() && !name3.isEmpty()){
//            entry3TextBox.setError("Name3 entered, please enter entry for this name!");
//            result = false;
//        }
//        if(name3.isEmpty() && !entry3.isEmpty()){
//            name3TextBox.setError("Entry3 entered, please enter name for this entry!");
//            result = false;
//        }
//        return result;
//    }
    //Called on submit button click
    public void onSubmit(View v){
//        ArrayList<NameValuePair> data = getData(); //getting all data from ediTexts
//        if(data!=null) {
//            //Creating a progressDialog to shows while the data is being posted.
//            ProgressDialog pDialog = createProgressDialog("Please wait", "Sending data to the server.....");
//            //posting to the server
//            new sendDataToServer(data,pDialog).execute(Config.SERVER_URL);
//        }

    }
    //Creates a progressDialog
    private ProgressDialog createProgressDialog(String title,String description){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(description);
        progressDialog.show();
        return progressDialog;
    }

    //Posting data to the server asynchronously
    class sendDataToServer extends AsyncTask<String, Void, String> {
		ArrayList<NameValuePair> data;
        ProgressDialog progressDialog;
		public sendDataToServer(ArrayList<NameValuePair> d,ProgressDialog p){
			data = d;
            progressDialog = p;
		}
        protected String doInBackground(String... urls) {
            PostRequest request = new PostRequest(urls[0], data);
            String result = request.post();
            return result;
        }

        protected void onPostExecute(String res) {
            try {
                progressDialog.dismiss();
                //Checking post request result
                if(res.equalsIgnoreCase("internetNotAvailable")){
                    make_dialog("Uh-oh","Looks like you're not connected to the internet.Please check your internet connection and try again.","Ok");
                }
                else if(res.toLowerCase().contains("exception")){
                    make_dialog("Some error occured!","There was some error in posting the data,contact server administrator.","Ok");
                }
                else {
                    JSONObject response = new JSONObject(res);
                    //Checking post request response
                    if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Data not posted!"))
                        make_dialog("Data not posted!", "Some required fields are missing!","Ok");
                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("User Already Registered"))
                        make_dialog("Data not posted!","One or more users with given details have already registered.","Ok");
                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Registration completed")) {
                        make_dialog("Data posted!", "Registration completed", "Ok");
                        //resetTextBoxes();
                    }
                    else
                        make_dialog("Umm...","Unexpected response from the server.\nResponse : "+res,"Ok");
                }

            } catch (Exception e) {
                make_dialog("Unable to send the data!","Please check your internet connection and try again.","Ok");
            }
        }
    }
    //to clear editText contents
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
    private void make_dialog(String title,String msg,String button_text){
        final Dialog dialog = new Dialog(MainActivity.this);
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
