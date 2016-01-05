package com.example.buddy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;

public class ViewForm extends AppCompatActivity {
android.webkit.WebView form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_form);
        String url=getIntent().getExtras().getString("url");
        form=(android.webkit.WebView)findViewById(R.id.webView);
        form.setWebViewClient(new MyBrowser());
        form.getSettings().setLoadsImagesAutomatically(true);
        form.getSettings().setJavaScriptEnabled(true);

        form.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        form.loadUrl(url);
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
