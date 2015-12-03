package com.smartsense.gifkar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class TermsandCondtionsActivity extends AppCompatActivity {
    WebView wvTerms;
    private ImageView btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.center_action_bar, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.actionBarTitle);
        titleTextView.setText(getResources().getString(R.string.terms));
        btBack = (ImageView) v.findViewById(R.id.btActionBarBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setCustomView(v);
        setContentView(R.layout.activity_termsand_condtions);
        wvTerms = (WebView) findViewById(R.id.webView);
        wvTerms.setWebViewClient(new MyBrowser());
        wvTerms.getSettings().setLoadsImagesAutomatically(true);
        wvTerms.getSettings().setJavaScriptEnabled(true);
        wvTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvTerms.loadUrl("http://www.google.com");
//        wvTerms.loadUrl("http://www.google.com");
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
