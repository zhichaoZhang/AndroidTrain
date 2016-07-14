package com.zzc.androidtrain.net;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zzc.androidtrain.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class HttpsTestActivity extends AppCompatActivity {
    private static final String TAG = "HttpsTestActivity";
    private final String cerName = "_.qfpay.com.cer";
    private final String cer12306Name = "kyfw.12306.cn.cer";
    //自签名证书 此服务器无法证明它是0.openapi2.qfpay.com；其安全证书来自*.qfpay.com
    /**
     * 默认访问此链接会报异常 javax.net.ssl.SSLPeerUnverifiedException: Hostname 0.openapi2.qfpay.com not verified:
     */
    private String url = "https://0.openapi2.qfpay.com/trade/v1/tradeinfo?needTerminalId=false&syssn=20160615015200020005902755";

    private String urlSecure = "https://o.qfpay.com/mchnt/init?version=1.9.8&app_name=Android";
    //由GeoTrust SSL CA颁发的安全证书
    /**
     * 默认访问此链接会正常返回
     */
    private String url2 = "https://api.qfpay.com/v1/trade/weixin_precreate";

    /**
     * 默认访问此链接因为是自认证证书会报异常 javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.
     */
    private String url12306 = "https://kyfw.12306.cn/otn/leftTicket/init";

    private String urlBaidu = "https://www.baidu.com";


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HttpsTestActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_https_test);
    }

    public void onClickValidateHttpsRequest(View view) {
        Thread requestThread = new Thread(new AsyncHttpsRequest(url2));
        requestThread.start();
    }

    public void onClickInvalidateHttpsRequest(View view) {
        Thread requestThread = new Thread(new AsyncHttpsRequest(url));
        requestThread.start();
    }

    public void onClickIgnoreCertificateHttpsRequest(View view) {
        Thread requestThread = new Thread(new AsyncHttpsRequest(url, false, true));
        requestThread.start();
    }

    public void onClick12306Request(View view) {
        Thread requestThread = new Thread(new AsyncHttpsRequest(url12306, false));
        requestThread.start();
    }

    public void onVerifyCerRequest(View view) {
        Thread requestThread = new Thread(new AsyncHttpsRequest(urlBaidu, false, true));
        requestThread.start();
    }

    private class AsyncHttpsRequest implements Runnable {

        private String strUrl = "";
        private boolean verifyHostName = false;
        private boolean verifyCertification = false;

        public AsyncHttpsRequest(String url) {
            this.strUrl = url;
        }

        public AsyncHttpsRequest(String url, boolean verifyHostName) {
            this.strUrl = url;
            this.verifyHostName = verifyHostName;
        }

        public AsyncHttpsRequest(String url, boolean verifyHostName, boolean verifyCertification) {
            this.strUrl = url;
            this.verifyHostName = verifyHostName;
            this.verifyCertification = verifyCertification;
        }

        @Override
        public void run() {
            InputStreamReader inputStreamReader = null;
            try {
                Log.d(TAG, "url----->" + strUrl);
                URL url = new URL(strUrl);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                if (verifyHostName) {
                    httpsURLConnection.setHostnameVerifier(HOSTNAME_VERIFIER);
                }
                if (verifyCertification) {
                    //从assets文件中读取证书文件,并由CertificateFactory生成证书
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate ca;
                    InputStream caInput = null;
                    try {
                        caInput = new BufferedInputStream(getAssets().open(cerName));
                        ca = cf.generateCertificate(caInput);
                    } finally {
                        if (caInput != null) {
                            caInput.close();
                        }
                    }

                    //创建一个包含我们信任证书的KeyStore
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null, null);
                    keyStore.setCertificateEntry("ca", ca);

                    //根据秘钥库生成信任管理器
                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);

                    //根据信任管理器创建SSL上下文
                    SSLContext sslContext = SSLContext.getInstance("TLSv1", "AndroidOpenSSL");
                    sslContext.init(null, tmf.getTrustManagers(), null);

                    //设置给HttpsUrlConnection
                    httpsURLConnection.setSSLSocketFactory(sslContext.getSocketFactory());

                }
                InputStream inputStream = httpsURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(new BufferedInputStream(inputStream));
                int c;
                StringBuilder stringBuilder = new StringBuilder();
                while ((c = inputStreamReader.read()) != -1) {
                    stringBuilder.append((char) c);
                }
                Log.d(TAG, "content:" + stringBuilder.toString());
                Log.d(TAG, "contentLength: " + httpsURLConnection.getContentLength());
                Log.d(TAG, "responseCode: " + httpsURLConnection.getResponseCode());
            } catch (CertificateException e) {
                System.out.println("-------证书验证异常----");
                e.printStackTrace();
            } catch (KeyStoreException e) {
                System.out.println("-------证书验证异常2----");
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.d(TAG, "hostname: " + hostname);
            try {
                Certificate[] certificates = session.getPeerCertificates();
                for (Certificate certificate : certificates) {
                    String publicKeyAlgorithm = certificate.getPublicKey().getAlgorithm();
                    Log.d(TAG, "publicKeyAlgorithm(公钥算法): " + publicKeyAlgorithm);

                }
                Log.d(TAG, "PeerHost(主机名): " + session.getPeerHost());
                Log.d(TAG, "PeerPort(端口号): " + session.getPeerPort());
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
            if (hostname != null && hostname.endsWith("qfpay.com")) {
                return true;
            } else {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify(hostname, session);
            }
        }
    };

}
