package com.ashish.cop290assign0;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ProgressDialog progressDialog;
    EditText teamNameTextBox,entry1TextBox,name1TextBox,entry2TextBox,name2TextBox,entry3TextBox,name3TextBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        teamNameTextBox = (EditText)findViewById(R.id.teamName);
        entry1TextBox = (EditText)findViewById(R.id.entry1);
        name1TextBox = (EditText)findViewById(R.id.name1);
        entry2TextBox = (EditText)findViewById(R.id.entry2);
        name2TextBox = (EditText)findViewById(R.id.name2);
        entry3TextBox = (EditText)findViewById(R.id.entry3);
        name3TextBox = (EditText)findViewById(R.id.name3);
        EditText[] editTextViews = {teamNameTextBox,entry1TextBox,name1TextBox,entry2TextBox,name2TextBox};
        addRedAsterisk(editTextViews);
    }
    private void addRedAsterisk(EditText[] e_array){
        for(EditText e : e_array) {
            String text = e.getHint().toString();
            String asterisk = " *";
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(text);
            int start = builder.length();
            builder.append(asterisk);
            int end = builder.length();
            builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            e.setHint(builder);
        }
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
    private ArrayList<NameValuePair> getData(){
        String teamName = teamNameTextBox.getText().toString();
        String entry1 = entry1TextBox.getText().toString();
        String name1 = name1TextBox.getText().toString();
        String entry2 = entry2TextBox.getText().toString();
        String name2 = name2TextBox.getText().toString();
        String entry3 = entry3TextBox.getText().toString();
        String name3 = name3TextBox.getText().toString();
        ArrayList<NameValuePair> data = new ArrayList<>();
        data.add(new BasicNameValuePair("teamname", teamName));
        data.add(new BasicNameValuePair("entry1", entry1));
        data.add(new BasicNameValuePair("name1", teamName));
        data.add(new BasicNameValuePair("entry2", entry2));
        data.add(new BasicNameValuePair("name2", name2));
        data.add(new BasicNameValuePair("entry3", entry3));
        data.add(new BasicNameValuePair("name3", name3));
        if(teamName.isEmpty()){
            teamNameTextBox.setError("Team name can't be empty!");
            data = null;
        }
        if(entry1.isEmpty()) {
            entry1TextBox.setError("Entry1 can't be empty!");
            data = null;
        }
        if(name1.isEmpty()) {
            name1TextBox.setError("Name1 can't be empty!");
            data = null;
        }
        if(entry2.isEmpty()){
            entry2TextBox.setError("Entry2 can't be empty!");
            data = null;
        }
        if(name2.isEmpty()){
            name2TextBox.setError("Name2 can't be empty!");
            data = null;
        }
        if(entry3.isEmpty() && !name3.isEmpty()){
            entry3TextBox.setError("Name3 entered, please enter entry for this name!");
            data = null;
        }
        if(name3.isEmpty() && !entry3.isEmpty()){
            name3TextBox.setError("Entry3 entered, please enter name for this entry!");
            data = null;
        }
        return data;
    }
    public void onSubmit(View v){
        String url = "http://agni.iitd.ernet.in/cop290/assign0/register/";
        ArrayList<NameValuePair> data = getData();
        if(data!=null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Sending data to the server.....");
            progressDialog.show();
            new sendDataToServer(data).execute(url);
        }

    }
    class sendDataToServer extends AsyncTask<String, Void, String> {
		ArrayList<NameValuePair> data;
		public sendDataToServer(ArrayList<NameValuePair> d){
			data = d;
		}
        protected String doInBackground(String... urls) {
            PostRequest request = new PostRequest(urls[0], data);
            String result = request.post();
            return result;
        }

        protected void onPostExecute(String res) {
            try {
                progressDialog.dismiss();
                if(res.equalsIgnoreCase("internetNotAvailable")){
                    make_dialog("Uh-oh","Looks like you're not connected to the internet.Please check your internet connection and try again.","Ok");
                }
                else if(res.toLowerCase().contains("exception")){
                    make_dialog("Some error occured!",res,"Ok");
                }
                else {
                    JSONObject response = new JSONObject(res);
                    if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Data not posted!"))
                        make_dialog("Data not posted!","Some required fields are missing!","Ok");
                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("User Already Registered"))
                        make_dialog("Data not posted!","One or more users with given details have already registered.","Ok");
                    else if(response.getString("RESPONSE_MESSAGE").equalsIgnoreCase("Registration completed")) {
                        make_dialog("Data posted!", "Registration completed", "Ok");
                        resetTextBoxes();
                    }
                    else
                        make_dialog("Umm...","Unexpected response from the server.\nResponse : "+res,"Ok");
                }

            } catch (Exception e) {
                make_dialog("Unable to send the data!","Please check your internet connection and try again.","Ok");
            }
        }
    }
    private void resetTextBoxes(){
        teamNameTextBox.setText("");
        entry1TextBox.setText("");
        name1TextBox.setText("");
        entry2TextBox.setText("");
        name2TextBox.setText("");
        entry3TextBox.setText("");
        name3TextBox.setText("");
    }
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
