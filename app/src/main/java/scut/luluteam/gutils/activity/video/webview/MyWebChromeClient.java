package scut.luluteam.gutils.activity.video.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author Guan
 * @date Created on 2018/5/30
 */
public class MyWebChromeClient extends WebChromeClient {

    private Context mContext;

    public MyWebChromeClient(Context context) {
        this.mContext = context;
    }

    //获取网站标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
        System.out.println("标题在这里");
//        mtitle.setText(title);
    }

    //获取加载进度
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress < 100) {
            String progress = newProgress + "%";
//            loading.setText(progress);
        } else if (newProgress == 100) {
            String progress = newProgress + "%";
//            loading.setText(progress);
        }
    }

    //Alert弹窗
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder b = new AlertDialog.Builder(mContext);
        b.setTitle("Alert");
        b.setMessage(message);
        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        });
        b.setCancelable(false);
        b.create().show();
        return true;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }


}
