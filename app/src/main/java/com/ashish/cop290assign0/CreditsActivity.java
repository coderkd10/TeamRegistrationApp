package com.ashish.cop290assign0;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class CreditsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    public void exit(View view){
        finish(); // closes the activity
    }
    public void openForm(View view){
        Intent intent = new Intent(CreditsActivity.this,MainActivity.class);
        startActivity(intent); //starting new intent
        finish(); //closing current activity
    }

    //opens url (in the default browser) depending on the view which is clicked
    public void handleLinks(View view){
        String url = "";
        switch(view.getId()){
            case R.id.kedia_github:
                url = "https://github.com/coderkd10";
                break;
            case R.id.kedia_twiiter:
                url = "https://twitter.com/kediaabhishek10";
                break;
            case R.id.ashish_github:
                url = "https://github.com/r-ashish";
                break;
            case R.id.ashish_twiiter:
                url = "https://twitter.com/ashrnjn";
                break;
            case R.id.arnav_github:
                url = "https://github.com/AK101111";
                break;
            case R.id.arnav_twiiter:
                url = "https://twitter.com/c2quadisdbest";
                break;
        }
        if(!url.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
}
