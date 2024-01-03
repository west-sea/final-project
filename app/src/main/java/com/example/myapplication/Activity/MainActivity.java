package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    //branch 예제
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView animationView = findViewById(R.id.splash); // LottieAnimationView 가져오기
        animationView.setAnimation("open.json"); // 애니메이션 파일 설정

        // 애니메이션이 반복되지 않도록 설정하려면 주석 해제
        // animationView.loop(false);

        animationView.playAnimation(); // 애니메이션 실행

        Button numButton=findViewById(R.id.numbut);
        numButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LinkNumActivity.class);
                startActivity(intent);
            }
        });

        Button imgButton=findViewById(R.id.imgbut);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ImgActivity.class);
                startActivity(intent);
            }
        });

        Button freeButton=findViewById(R.id.freebut);
        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), FreeActivity.class);
                startActivity(intent);
            }
        });
    }
}