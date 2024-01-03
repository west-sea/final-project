package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;

public class LoadingPopup extends AppCompatActivity {

    private ImageView img_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_loading);

        initialSet();

        Glide.with(this)
                .asGif()
                .load(R.raw.loading)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(img_gif);

        // 3초 후에 MainActivity로 전환
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingPopup.this, MainActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티를 종료하여 이전으로 돌아가기를 방지
        }, 3500); // 3초 지연
    }

    public void initialSet() {
        img_gif = findViewById(R.id.animationView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}