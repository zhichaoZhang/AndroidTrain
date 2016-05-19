package com.zzc.androidtrain;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zzc.androidtrain.web.CustomWebView;

public class WebActivity extends AppCompatActivity {

    CustomWebView webView;
    Button btnRefresh;
    EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        webView = (CustomWebView)findViewById(R.id.web_view);
        webView.setWebChromeClient(getWebChromeClient());
        webView.setWebViewClient(getWebClient());
        btnRefresh = (Button)findViewById(R.id.btn_refresh);
        etUrl = (EditText)findViewById(R.id.et_url);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString().trim();
                if(!TextUtils.isEmpty(url)) {
                    Toast.makeText(getBaseContext(), url, Toast.LENGTH_LONG).show();
                    webView.loadUrl(url);
                }
            }
        });
    }

    @NonNull
    private WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //配置H5请求地理位置权限
            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                // 请求定位信息
                callback.invoke(origin, true, false);
            }
        };
    }

    private WebViewClient getWebClient() {
        WebViewClient webViewClient =
         new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        };
        return webViewClient;
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
