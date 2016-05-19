package com.zzc.androidtrain.web;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zzc.androidtrain.util.ApkUtil;
import com.zzc.androidtrain.util.DeviceUtil;

/**
 * Created by fengruicong on 16-3-23.
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
        init(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init(context);
    }

    private void init(Context context) {
        //设置webview支持chrome调试 4.4以上系统支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                setWebContentsDebuggingEnabled(true);
            }
        }
        setWebViewSetting();
        setCookie();
    }

    private void setWebViewSetting() {
        WebSettings settings = /*webView.*/getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setBuiltInZoomControls(false);
        //设置UA user-agent
        String appVersion = "/version_name:" + ApkUtil.getVersionName(getContext()) + ";version_code:" + ApkUtil.getVersionCode(getContext());
        String deviceName = "/device_name:" + DeviceUtil.getDeviceName() + ";deviceid:" + DeviceUtil.getDeviceID(getContext());
        String osVersion = "/os version:" + DeviceUtil.getOsVersionStr() + ";" + DeviceUtil.getOsVersion();
        settings.setUserAgentString(settings.getUserAgentString() + ";QMMWD" + appVersion + deviceName + osVersion);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //开启DomStorage缓存
        settings.setDomStorageEnabled(true);
        settings.setGeolocationDatabasePath(getContext().getFilesDir().getPath());
        //启用数据库
        settings.setDatabaseEnabled(true);
    }

    /**
     * 设置Cookie
     */
    private void setCookie() {
//        String domainName;
//        if (ConstValue.SERVER_BY_HAOJIN_URL.equals(ConstValue.URL.SERVER_BY_HAOJIN.OFF_LINE_DEBUG.value)) {
//            domainName = "172.100.102.101:8785";
//        } else {
//            domainName = ".qfpay.com";
//        }
//
//        CookieSyncManager.createInstance(getContext());
//        CookieManager cookieManager = CookieManager.getInstance();
//        CookieSyncManager.getInstance().startSync();
//        CookieManager.allowFileSchemeCookies();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager.setAcceptThirdPartyCookies(/*webView*/this, true);
//        }
//        cookieManager.setAcceptCookie(true);
//        /*webView.*/
//        clearCache(true);
//        /*webView.*/
//        clearHistory();
//        boolean offlineDev = SPUtil.getInstance(getContext()).getBoolean(ConstValue.Sp.OFFLINE_DEV, false);
//        UserCache userCache = UserCache.getInstance(getContext());
//        if (userCache.hasLogin()) {
//            String sessionId = userCache.getCacheUser().getSessionid();
//            int qfUid = userCache.getCacheUser().getUserid();
//            /**
//             *  注：临时cookie(没有expires参数的cookie)不能带有domain选项。
//             当客户端发送一个http请求时，会将有效的cookie一起发送给服务器。
//             如果一个cookie的domain和path参数和URL匹配，那么这个cookie就是有效的。
//             一个URL中包含有domain和path，可以参
//             */
//            cookieManager.setCookie(domainName, "sessionid=" + sessionId);
//            cookieManager.setCookie(domainName, "qf_uid=" + qfUid);
//        }
//        if (offlineDev) {
//            cookieManager.setCookie(domainName, "mmfct=1");
//        }
//        CookieSyncManager.getInstance().sync();
//        Timber.i("seted cookie---" + cookieManager.getCookie(domainName));
    }
}
