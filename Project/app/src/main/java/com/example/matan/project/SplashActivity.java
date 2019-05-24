package com.example.matan.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                boolean result =pref.getBoolean("IsCheck", false);
                data = pref.getString("Email", null);

                if(result) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("EMAIL",data);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

