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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void exit(View view){
        finish();
    }
    public void openForm(View view){
        Intent intent = new Intent(CreditsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
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
