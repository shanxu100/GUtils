package scut.luluteam.gutils.utils;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 定制状态栏等显示
 * Created by Guan on 2018/3/8.
 */

public class CustomActivityManager {
    /**
     * 是否沉浸状态栏:
     **/
    private boolean isSetStatusBar = false;
    /**
     * 是否允许全屏
     **/
    private boolean noTitle = true;
    /**
     * 是否允许旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;

    /**
     * 设置状态栏颜色:对于Android 5.0有效
     */
    private int statusBarColor_Id = -1;

    private AppCompatActivity activity;

    public CustomActivityManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * 内部类：用于标识Activity的设置项
     */
    public static final class CustomBuilder {
        CustomActivityManager manager;

        public CustomBuilder(AppCompatActivity activity) {
            manager = new CustomActivityManager(activity);
        }

        /**
         * [是否允许全屏]
         *
         * @param noTitle
         */
        public CustomBuilder setNoTitle(boolean noTitle) {
            manager.noTitle = noTitle;
            return this;
        }

        /**
         * [是否设置沉浸状态栏]
         *
         * @param isSetStatusBar
         */
        public CustomBuilder setSteepStatusBar(boolean isSetStatusBar) {
            manager.isSetStatusBar = isSetStatusBar;
            return this;
        }

        /**
         * [是否允许屏幕旋转]
         *
         * @param isAllowScreenRoate
         */
        public CustomBuilder allowScreenRoate(boolean isAllowScreenRoate) {
            manager.isAllowScreenRoate = isAllowScreenRoate;
            return this;
        }

        public CustomBuilder setStatusBarColor_Id(int statusBarColor_Id) {
            manager.statusBarColor_Id = statusBarColor_Id;
            return this;
        }

        public void build() {
            manager.customActivity();
        }

    }

    /**
     * 根据CustomBuilder中的设置项，开始设置Activity
     */
    private void customActivity() {
        if (noTitle) {
            //设置取消标题栏。但AppCompatActivity对此无用
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //采用这种方式取消标题栏
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }
        if (isSetStatusBar) {
            /**
             * 开始设置：[沉浸状态栏]
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
        if (!isAllowScreenRoate) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        /**
         * 对于Android 4.4以上，状态栏设置为全透明
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            // Translucent status bar
//            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        if (statusBarColor_Id != -1) {
            //取消状态栏透明
            //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, statusBarColor_Id));
            }
        }


    }
}
