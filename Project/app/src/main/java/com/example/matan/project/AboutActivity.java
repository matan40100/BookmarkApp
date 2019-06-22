package com.example.matan.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar header = (Toolbar) findViewById(R.id.header);
        TextView mTitle = (TextView) header.findViewById(R.id.toolbar_title);
        mTitle.setText("About");

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}
