package com.chensiwen.edugame.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chensiwen.edugame.R;

public class WebviewActivity extends AppCompatActivity {
    private static final String TAG = "WebviewActivity";
    private WebView webView;
//    private String url = "http://owl.shuqiread.com/?appfunction=an_bmk,1_clo,1_smjs,1&soft_id=1&ver=171116&preVer=&appVer=10.6.3.58&platform=an&placeid=8593&imei=865790024354920&mac=&sdk=22&wh=1080x1920&imsi=null&msv=3&enc=635121525686799675&sn=259098729838&vc=a9cb&mod=YQ601&manufacturer=smartisan&brand=SMARTISAN&net_type=wifi&first_placeid=src8593&aak=0de7ac&user_id=875892007&utype=pre_vip&net=4&net_env=4&coreType=0&rom=5.1.1&skinId=999&skinVersion=1.0&skinVersionPrefix=1&utdid=Vmlpc0Z3cGh4NGdEQUhsaHlvQ2VMUGRx&writer_switch=1&sq_pg_param=owlst#!/class_id/19";
    private String url = "http://owl.shuqiread.com/?sq_pg_param=owlst#!/class_id/20";
//    private String url = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
//                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d(TAG, "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.d(TAG, "onReceivedError() called with: view = [" + view + "], request = [" + request + "], error = [" + error + "]");
                super.onReceivedError(view, request, error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl(url);

    }
}
