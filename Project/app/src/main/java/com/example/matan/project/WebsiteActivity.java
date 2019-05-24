package com.example.matan.project;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;



public class WebsiteActivity extends AppCompatActivity {

    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        String start = "https://";
        String URL= getIntent().getStringExtra("URL");
        if(!URL.contains("https://")){
            URL = start.concat(URL);
        }



        webview =(WebView)findViewById(R.id.webview);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(URL);
        webview.clearCache(true);
    }
    @Override
    public void onBackPressed(){
        if (webview.canGoBack()){
            webview.goBack();
        } else {
            Intent intent = new Intent(WebsiteActivity.this, HomeActivity.class);
            intent.putExtra("EMAIL" ,getIntent().getStringExtra("EMAIL"));
            startActivity(intent);
            finish();
            super.onBackPressed();
        }
    }
}
