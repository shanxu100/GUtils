package scut.luluteam.gutils.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;


public class WebViewActivity extends BaseActivity {
    WebView mWebView;
    WebChromeClient webChromeClient;
    WebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCustomBuilder()
                .setNoTitle(true)
                .build();
        setContentView(R.layout.activity_web_view);
        init();
    }

    private void init() {
        mWebView = (WebView) this.findViewById(R.id.test_webview);
        mWebView.loadUrl("http://i-test.com.cn/yjyProject/dm/getfps?school=山东省淄博峨庄小学&discipline=&teacher=&grade=&cl=&minSup=10&classification=true&activity=");
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultFontSize((int) 20);
        webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("webview title：" + title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                System.out.println("webview progress:" + newProgress + "%");
            }
        };

        webViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("结束加载了");
            }
        };

        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        /**
         * APM监控
         */
        //WebViewTools.setUpWithWebView(mWebView, webViewClient);
    }

}
