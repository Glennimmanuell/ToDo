package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::startMainActivity, SPLASH_DELAY);
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, loginUser.class);
        startActivity(intent);
        finish();
    }
}
