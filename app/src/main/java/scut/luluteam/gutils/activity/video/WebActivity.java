package scut.luluteam.gutils.activity.video;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.activity.video.webview.MyWebChromeClient;
import scut.luluteam.gutils.activity.video.webview.MyWebViewClient;
import scut.luluteam.gutils.app.BaseActivity;


public class WebActivity extends BaseActivity {
    private WebView mWebview;
    private WebSettings mWebSettings;
    private LinearLayout ll_debugInfo;
    private TextView beginLoading, endLoading, loading, mtitle;
    private FrameLayout fl_videoLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initUI();
        customeWebSetting(mWebview.getSettings());


        /**
         * 设置WebChromeClient类
         * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
         */
        mWebview.setWebChromeClient(new MyWebChromeClient(mContext));


        //设置WebViewClient类
        mWebview.setWebViewClient(new MyWebViewClient(mContext));

//        String url = " http://open.ys7.com/sdk/js/1.3/demo.html";
        String url = "http://125.216.242.147:8080/bathProject/go/playVideo";
        mWebview.loadUrl(url);


    }

    private void initUI() {
        ll_debugInfo = (LinearLayout) findViewById(R.id.ll_debugInfo);
        fl_videoLayout = (FrameLayout) this.findViewById(R.id.fl_videoLayout);
        beginLoading = (TextView) findViewById(R.id.text_beginLoading);
        endLoading = (TextView) findViewById(R.id.text_endLoading);
        loading = (TextView) findViewById(R.id.text_Loading);
        mtitle = (TextView) findViewById(R.id.title);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebview = new WebView(getApplicationContext());
        mWebview.setLayoutParams(params);
        fl_videoLayout.addView(mWebview);
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }


    /**
     * 配置WebSetting
     *
     * @param webSettings
     */
    private void customeWebSetting(WebSettings webSettings) {
        if (webSettings == null) {
            return;
        }
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        启用或禁用WebView中的文件访问
//        webSettings.setAllowFileAccess(true);
//      是否调节内容 是否全屏
//        webSettings.setLoadWithOverviewMode(true);

    }
}
