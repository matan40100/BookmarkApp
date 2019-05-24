package com.example.matan.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class HtmlActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);


        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/page_1.html");
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed(){
            Intent intent = new Intent(HtmlActivity.this, HomeActivity.class);
            intent.putExtra("EMAIL" ,getIntent().getStringExtra("EMAIL"));
            startActivity(intent);
            finish();
            super.onBackPressed();
    }
}
