package scut.luluteam.gutils.service.floatwindow;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import scut.luluteam.gutils.R;

/**
 * Created by guan on 5/31/17.
 */

public class FloatWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowView.Small smallWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowView.Big bigWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams smallWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static WindowManager.LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    private static String TAG = "FloatWindowManager";

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param appContext 必须为应用程序的Context.
     */
    public static void createSmallWindow(Context appContext) {
        WindowManager windowManager = getWindowManager(appContext);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        int screenWidth = metric.widthPixels;
        int screenHeight = metric.heightPixels;
        if (smallWindow == null) {

            smallWindow = new FloatWindowView.Small(appContext);

            if (smallWindowParams == null) {
                smallWindowParams = new WindowManager.LayoutParams();
                smallWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                smallWindowParams.format = PixelFormat.RGBA_8888;
                smallWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                smallWindowParams.width = FloatWindowView.Small.viewWidth;
                smallWindowParams.height = FloatWindowView.Small.viewHeight;
                smallWindowParams.x = screenWidth;
                smallWindowParams.y = screenHeight / 2;
            }
            smallWindow.setParams(smallWindowParams);
            windowManager.addView(smallWindow, smallWindowParams);
//                Log.e(TAG, "FloatWindowView.Small.viewWidth" + FloatWindowView.Small.viewWidth +
//                        "\tFloatWindowView.Small.viewHeight" + FloatWindowView.Small.viewHeight);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param appContext 必须为应用程序的Context.
     */
    public static void removeSmallWindow(Context appContext) {
        if (smallWindow != null) {
            WindowManager windowManager = getWindowManager(appContext);
            windowManager.removeView(smallWindow);
            smallWindow = null;
            Log.e(TAG, "removeSmallWindow");
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param appContext 必须为应用程序的Context.
     */
    public static void createBigWindow(Context appContext) {
        WindowManager windowManager = getWindowManager(appContext);
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);

        if (bigWindow == null) {
            bigWindow = new FloatWindowView.Big(appContext);

            if (bigWindowParams == null) {
                bigWindowParams = new WindowManager.LayoutParams();
                //它置于所有应用程序之上，状态栏之下
                bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                //背景变成透明
                bigWindowParams.format = PixelFormat.RGBA_8888;
                //在显示该悬浮窗的时候，依然可以对悬浮窗外的任务进行操作
//                bigWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
//                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                //设置背景的暗度
                bigWindowParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                bigWindowParams.dimAmount = 0.3f;
                //在屏幕中间显示
                bigWindowParams.gravity = Gravity.CENTER;
                bigWindowParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                bigWindowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            windowManager.addView(bigWindow, bigWindowParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBigWindow(Context context) {
        if (bigWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(bigWindow);
            bigWindow = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context 可传入应用程序上下文。
     */
    public static void updateUsedPercent(Context context) {
        if (smallWindow != null) {
            TextView percent_btn = (TextView) smallWindow.findViewById(R.id.small_tv);
            percent_btn.setText("balabala");
        }
    }


    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return smallWindow != null || bigWindow != null;
    }


    //==========================================================================================

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


}
