package com.example.technews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class webActivity extends AppCompatActivity {

    WebView webView;
    String url;
    ProgressDialog pd;

    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack();
        }
        else
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        url = getIntent().getStringExtra("url");

        webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        pd.dismiss();
    }
}
