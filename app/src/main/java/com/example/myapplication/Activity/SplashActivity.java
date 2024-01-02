package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000; // 스플래시 화면을 표시할 시간 (3초)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LottieAnimationView animationView = findViewById(R.id.splash);

        if (getIntent().getBooleanExtra("loaded", false)) {
            // If MainActivity has informed about its loading completion, finish SplashActivity
            finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationView.setAnimation("open.json");
                    animationView.setVisibility(View.VISIBLE);
                    animationView.playAnimation();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}

