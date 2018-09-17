package com.semicolon.rests.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.rests.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageView logo;
    TextView name;
    Animation animFadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo=findViewById(R.id.logo_img);
        name=findViewById(R.id.app_name_txt);

        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        animFadein.setAnimationListener(this);


        logo.startAnimation(animFadein);
        name.startAnimation(animFadein);

    }


    @Override
    public void onAnimationStart(Animation animation) {
        logo.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
