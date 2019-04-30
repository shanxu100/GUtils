package scut.luluteam.gutils.view.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * @author Guan
 * @date Created on 2019/3/7
 */
public class WebViewManager {

    private Context context;
    private WebView webView;
    private ViewGroup webViewContainer;
    private String url;
    private MyWebViewClient myWebViewClient;
    private WebViewLoadInterface loadInterface;


    public WebViewManager(Context context, ViewGroup webViewContainer, String url) {
        this.context = context;
        this.webViewContainer = webViewContainer;
        this.url = url;
    }

    public WebViewManager(Context context, ViewGroup webViewContainer, String url, WebViewLoadInterface loadInterface) {
        this.context = context;
        this.webViewContainer = webViewContainer;
        this.url = url;
        this.loadInterface = loadInterface;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView() {
        webView = new WebView(context);
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        //为webview添加一张背景图，然后使背景图为透明，然后背景变成了xml布局的背景，融合在了一起
        //webView.setBackgroundResource(R.drawable.bottom_line_1);
        webView.setBackgroundColor(2);
        //webView.getBackground().setAlpha(0);

        this.webViewContainer.addView(webView);

        WebSettings webSettings = webView.getSettings();
        //支持JS
        webSettings.setJavaScriptEnabled(true);
        // 关键点
        webSettings.setUseWideViewPort(true);
        // 允许访问文件
        webSettings.setAllowFileAccess(true);
        // 支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        // 不加载缓存内容
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        myWebViewClient = new MyWebViewClient(context);
        myWebViewClient.setLoadInterface(loadInterface);
        webView.setWebViewClient(myWebViewClient);
        webView.setWebChromeClient(new MyWebChromeClient(context));

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        webView.loadUrl(url);

    }

    /**
     * 销毁WebView
     */
    public void destroyWebView() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }

    /**
     * 调用WebView中加载页面的JS方法的
     *
     * @param msg
     */
    public boolean onNewMsg(String functionName, String msg, final WebViewCallback callback) {

        if (myWebViewClient.getPageFinishedWebView() != null) {
            WebView view = myWebViewClient.getPageFinishedWebView();
            /**
             * 调用JS
             */

            String jsStr = "javascript:" + functionName + "('" + msg + "')";
            view.evaluateJavascript(jsStr, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    System.out.println("此处为 js 返回的结果:====" + value);
                    if (callback != null) {
                        callback.onValue(value);

                    }
                }
            });
            return true;
        } else {
            System.out.println("页面还未加载完成，稍后调用");
            return false;
        }
    }

    /**
     * 处理调用WebView中js方法后的返回结果
     */
    public interface WebViewCallback {
        void onValue(String json);
    }

    /**
     * WebView加载进度的接口
     */
    public interface WebViewLoadInterface {

        /**
         * 加载结束
         */
        void onLoadFinished();
    }


}
