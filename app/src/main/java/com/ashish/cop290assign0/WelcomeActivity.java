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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        showIcon();
    }

    //pop in animation for team icon
    private void showIcon(){
        View iconLayoutMain = findViewById(R.id.icon_layout_main);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f); //defining start and end alpha values for animation
        animation.setDuration(1200); //setting animation duration
        animation.setFillAfter(true);
        iconLayoutMain.startAnimation(animation);
    }

    //called on procced button click
    public void proceed(View v){
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
