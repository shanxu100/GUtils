package scut.luluteam.gutils.view.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Guan
 * @date Created on 2018/5/30
 */
public class MyWebViewClient extends WebViewClient {


    private Context mContext;

    private WebView pageFinishedWebView;

    private WebViewManager.WebViewLoadInterface loadInterface;

    public MyWebViewClient(Context context) {
        this.mContext = context;
    }

    public WebView getPageFinishedWebView() {
        return pageFinishedWebView;
    }

    public WebViewManager.WebViewLoadInterface getLoadInterface() {
        return loadInterface;
    }

    public void setLoadInterface(WebViewManager.WebViewLoadInterface loadInterface) {
        this.loadInterface = loadInterface;
    }

    /**
     * 设置加载前的函数
     *
     * @param view
     * @param url
     * @param favicon
     */
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        System.out.println("开始加载了");
    }

    /**
     * 这个方法干吗用？
     * 和 mWebview.loadUrl()有什么区别
     *
     * @param view
     * @param request
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 设置结束加载函数
     *
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        pageFinishedWebView = view;
        if (loadInterface != null) {
            loadInterface.onLoadFinished();
        }


    }


    /**
     * 在assets文件夹新建一个alert.js。只有一个方法，接受一个mseesage，并将message弹出。
     * 在onPageFinished方法回调的时候，将此js插入到WebView中。
     * 此处有坑，尝试了网上的各种方法，均不能成功，最后在stackoverflow上找到方法。
     * http://stackoverflow.com/questions/21552912/android-web-view-inject-local-javascript-file-to-remote-webpage
     */
    private void injectScriptFile(WebView view, String scriptFile) {
        InputStream input;
        try {
            input = mContext.getAssets().open(scriptFile);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            view.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
