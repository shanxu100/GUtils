package scut.luluteam.gutils.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by guan on 5/19/17.
 */

public class DisplayUtil {

    private static int SCREENWIDTH = 0;
    private static int SCREENHEIGHT = 0;
    private static int DensityDpi = 0;
    private static int StatusBarHeight = 0;

    //但例模式：饿汉式
    private static DisplayUtil displayUtil = new DisplayUtil();

    private static String TAG = "DisplayUtil";

    @Deprecated
    public static int getScreenWidth() {
        return SCREENWIDTH;
    }

    @Deprecated
    public static int getScreenHeight() {
        return SCREENHEIGHT;
    }

    @Deprecated
    public static int getStatusBarHeight() {
        return StatusBarHeight;
    }

    //=======================
    //新的API
    //==================

    public static int getScreenWidth(Context mContext) {
        if (SCREENWIDTH == 0) {
            displayUtil.saveScreenInfo(mContext);
        }
        return SCREENWIDTH;
    }

    public static int getScreenHeight(Context mContext) {
        if (SCREENHEIGHT == 0) {
            displayUtil.saveScreenInfo(mContext);
        }
        return SCREENHEIGHT;
    }

    public static int getDensityDpi(Context mContext) {
        if (DensityDpi == 0) {
            displayUtil.saveScreenInfo(mContext);
        }
        return DensityDpi;
    }

    public static int getStatusBarHeight(Context mContext) {
        if (StatusBarHeight == 0) {
            displayUtil.saveStatusBarHeight(mContext);
        }
        return StatusBarHeight;
    }


    //=================================================
    public static void init(Context mContext) {
        displayUtil.saveScreenInfo(mContext);
        displayUtil.saveStatusBarHeight(mContext);
    }

    private DisplayUtil() {
    }


    /**
     * 获取并保存屏幕大小
     *
     * @param mContext
     */
    private void saveScreenInfo(Context mContext) {

        if (SCREENWIDTH != 0 && SCREENHEIGHT != 0) {
            return;
        }
        //通过Application获取屏幕信息
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        SCREENWIDTH = metric.widthPixels;
        // 屏幕高度（像素）
        SCREENHEIGHT = metric.heightPixels;
        DensityDpi = metric.densityDpi;

        Log.d(TAG, "SCREENWIDTH:\t" + SCREENWIDTH +
                "\tSCREENHEIGHT:\t" + SCREENHEIGHT +
                "\tDensityDpi:\t" + DensityDpi);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int saveStatusBarHeight(Context mContext) {
        if (StatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                StatusBarHeight = mContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StatusBarHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     */
    public static int dpToPx(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 转 px
     *
     */
    public static int spToPx(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     */
    public static float pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }

    /**
     * px 转 sp
     *
     */
    public static float pxToSp(Context context, float pxValue) {
        return (pxValue / context.getResources().getDisplayMetrics().scaledDensity);
    }


}
