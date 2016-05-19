package com.zzc.androidtrain;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzc.androidtrain.app.BaseActivity;

/**
 * Support v4包种PrintHelper类支持打印图片/网页/文档
 *
 * Created by zczhang on 16/4/9.
 */
public class PrintActivity extends BaseActivity{
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        mWebView = (WebView)findViewById(R.id.web_view);
        initWebView();
    }

    private void initWebView() {
        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
        "testing, testing...</p></body></html>";
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        mWebView.loadDataWithBaseURL(null,htmlDocument,"text/html","UTF-8",null);
    }

    public void onPintBtnClick(View view) {
        PrintHelper printHelper = new PrintHelper(this);
        printHelper.setColorMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        printHelper.printBitmap("test print", bitmap);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onPrintWebViewBtnClick(View view) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter = mWebView.createPrintDocumentAdapter("Default");
        String jobName = getString(R.string.app_name) + "document";
        PrintJob printJob = printManager.print(jobName, printDocumentAdapter, new PrintAttributes.Builder().build());
    }
}
