package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;

public class SplashActivity extends Activity {

    private static final int SPLASH_DURATION = 3500; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation("open.json");
        animationView.loop(true);
        animationView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after the splash duration
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}