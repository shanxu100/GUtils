package scut.luluteam.gutils.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import scut.luluteam.gutils.app.App;


/**
 * Created by guan on 2/25/17.
 */

public class ShowUtil {

    private static Toast logToast;

    private static Handler handler;

    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义。
     * 抛出的异常语句不是必须的，但有了这一句可以防止在类的内部进行实例化
     */
    private ShowUtil() {
        throw new AssertionError();
    }

    /**
     * @param context
     * @param msg
     */
    public static void UIToast(final Context context, final String msg) {
        String TAG = context.toString();
        Log.e(TAG, msg);

        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (logToast != null) {
                    logToast.cancel();
                }
                logToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                logToast.show();
            }
        });

    }

    /**
     * 本方法仅适用于显示一些不重要的信息，无法指定TAG
     *
     * @param msg
     */
    public static void UIToast(final String msg) {
        UIToast(App.getAppContext(), msg);
    }


}
