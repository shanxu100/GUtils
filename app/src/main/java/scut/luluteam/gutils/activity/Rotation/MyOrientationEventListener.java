package scut.luluteam.gutils.activity.Rotation;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.WindowManager;

/**
 * @author Guan
 * @date Created on 2019/5/5
 */
public class MyOrientationEventListener extends OrientationEventListener {

    private static final String TAG = "OrientationEvent";

    private WindowManager mWindowManager;
    private int mLastOrientation = -1;

    private static final int ANGLE_0 = 0;
    private static final int ANGLE_90 = 90;
    private static final int ANGLE_180 = 180;
    private static final int ANGLE_270 = 270;

    private Context context;
    private OnChangeOrientationListener listener;


    public MyOrientationEventListener(Context context) {
        super(context);
        this.context = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setListener(OnChangeOrientationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOrientationChanged(int orientation) {


        int value = getCurentOrientationEx(orientation);
        if (value == mLastOrientation) {
            //角度不够，不旋转屏幕
            return;
        }
        Log.i(TAG, "旋转屏幕：value=" + value);


        if (mLastOrientation != -1 && !isWideScrren()) {
            //横竖屏切换由Activity中的Button控制
            //所以，如果当前是竖屏，不做处理
            //如果mLastOrientation==-1，说明是系统刚刚初始化，这个时候需要return
            return;
        }

        mLastOrientation = value;

        if (listener != null) {
            if (value == ANGLE_270) {
                Log.i(TAG, "正向横屏显示");
                listener.onLandscape(true);
            } else if (value == ANGLE_90) {
                Log.i(TAG, "反向横屏显示");
                listener.onLandscape(false);
            }
        }


    }

    public boolean isWideScrren() {
        Display display = mWindowManager.getDefaultDisplay();
        Point pt = new Point();
        display.getSize(pt);
        return pt.x > pt.y;
    }


    private int getCurentOrientationEx(int orientation) {
        int value = 0;
        if (orientation >= 315 || orientation < 45) {
            // 0度
            value = ANGLE_0;
        }
        if (orientation >= 45 && orientation < 135) {
            // 90度
            value = ANGLE_90;
        }
        if (orientation >= 135 && orientation < 225) {
            // 180度
            value = ANGLE_180;
        }
        if (orientation >= 225 && orientation < 315) {
            // 270度
            value = ANGLE_270;
        }
        return value;

    }


    /**
     * 感应角度变化，并做出回调
     */
    interface OnChangeOrientationListener {


        /**
         * 横屏分正向横屏（靠左手方向横屏）和反向横屏（靠右手方向横屏）
         *
         * @param isNormalLandscape true：正向横屏；false：反向横屏
         */
        void onLandscape(boolean isNormalLandscape);


        /**
         * 竖屏
         */
        void onPortrait();
    }

}
