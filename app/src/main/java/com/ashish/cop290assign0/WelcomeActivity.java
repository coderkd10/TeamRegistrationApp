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

    public void proceed(View v){
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        WelcomeActivity.this.finish();
    }
}
