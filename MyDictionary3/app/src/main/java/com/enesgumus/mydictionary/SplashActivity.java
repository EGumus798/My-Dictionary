package com.enesgumus.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    // Süre uzunluğu (örneğin, 4000 milisaniye)
    private static final int SPLASH_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // SplashScreen süresi kadar bekleyip ardından MainActivity'e geçiş yap
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}

