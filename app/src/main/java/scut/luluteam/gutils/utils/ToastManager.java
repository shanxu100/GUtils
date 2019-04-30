package scut.luluteam.gutils.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import scut.luluteam.gutils.app.App;


/**
 * 显示Toast和Log
 *
 * @author Guan
 */
public class ToastManager {

    private static Handler handler;

    int gravity = Gravity.BOTTOM;
    int duration = Toast.LENGTH_SHORT;
    boolean log = false;
    String Tag;

    String content = "";

    private ToastManager(String content) {
        this.content = content;
    }

    public static ToastManager newInstance(String content) {
        return new ToastManager(content);
    }

    public ToastManager setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public ToastManager setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * 同时打印日志
     *
     * @param Tag
     * @return
     */
    public ToastManager isLog(String Tag) {
        this.log = true;
        this.Tag = Tag;
        return this;
    }

    public void show() {
        showToast(content, gravity, duration, log, Tag);
    }


    /**
     * 支持在 <P>非UI线程<P/> 中显示Toast
     * 非线程安全
     *
     * @param msg
     * @param gravity
     * @param duration
     */
    private static void showToast(final String msg, final int gravity, final int duration, boolean log, String Tag) {

        if (log) {
            Log.e(Tag, msg);
        }
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(App.getAppContext(), msg, duration);
                toast.setGravity(gravity, 0, 0);
                toast.show();
            }
        });
    }

}
