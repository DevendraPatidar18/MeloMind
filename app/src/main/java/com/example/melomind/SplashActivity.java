package com.example.melomind;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splogo = findViewById(R.id.splogo);


        // Load and display the GIF with Glide
        Glide.with(this)
                .asGif()
                .load(R.raw.music)
                .into(splogo);


       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent intent = new Intent(SplashActivity.this,MainActivity.class);
               startActivity(intent);
               finish();
           }
       },2000);
    }
}