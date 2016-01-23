package com.ashish.cop290assign0;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;


public class WelcomeActivity extends ActionBarActivity {

    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        showIcon();
    }

    private void showIcon(){
        View icon = findViewById(R.id.icon);
        View iconLayout = findViewById(R.id.icon_layout);
        View iconLayoutMain = findViewById(R.id.icon_layout_main);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1200);
        //animation.setStartOffset(5000);
        animation.setFillAfter(true);
        iconLayoutMain.startAnimation(animation);
        //iconLayoutMain.animate().scaleY(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
    public void proceed(View v){
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
