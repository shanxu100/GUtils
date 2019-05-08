package scut.luluteam.gutils.activity.Rotation;

import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.view.OrientationEventListener;
import android.view.Surface;

import scut.luluteam.gutils.app.BaseActivity;

/**
 * 自定义工具类
 * <p>
 * 实时的检测用户屏幕的位置改变，做到 重力感应 与 全屏/半屏按钮 逻辑的互容
 */
public class CLOrientationDetector extends OrientationEventListener {

    /**
     * 横竖屏BaseActivity
     */
    private BaseActivity mActivity;
    /**
     * 用户是否锁定屏幕
     */
    private boolean mIsLock = false;
    /**
     * 实时记录用户手机屏幕的位置
     */
    private int mOrientation = -1;

    /**
     * 构造方法
     *
     * @param activity
     */
    public CLOrientationDetector(BaseActivity activity) {
        super(activity);
        mActivity = activity;
    }

    /**
     * 设置用户屏幕是否被锁定
     *
     * @param isLock
     */
    public void setIsLock(boolean isLock) {
        mIsLock = isLock;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        //判null
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        //记录用户手机上一次放置的位置
        int mLastOrientation = mOrientation;

        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            //手机平放时，检测不到有效的角度

            //重置为原始位置 -1
            mOrientation = -1;
            return;
        }

        /**
         * 只检测是否有四个角度的改变
         */
        if (orientation > 350 || orientation < 10) {
            //0度，用户竖直拿着手机
            mOrientation = 0;

        } else if (orientation > 80 && orientation < 100) {
            //90度，用户右侧横屏拿着手机
            mOrientation = 90;

        } else if (orientation > 170 && orientation < 190) {
            //180度，用户反向竖直拿着手机
            mOrientation = 180;

        } else if (orientation > 260 && orientation < 280) {
            //270度，用户左侧横屏拿着手机
            mOrientation = 270;
        }

        //如果用户锁定了屏幕，不再开启代码自动旋转了，直接return
        if (mIsLock) {
            return;
        }

        //如果用户关闭了手机的屏幕旋转功能，不再开启代码自动旋转了，直接return
        try {
            /**
             * 1 手机已开启屏幕旋转功能
             * 0 手机未开启屏幕旋转功能
             */
            if (Settings.System.getInt(mActivity.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 0) {
                return;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        //当检测到用户手机位置距离上一次记录的手机位置发生了改变，开启屏幕自动旋转
        if (mLastOrientation != mOrientation) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @return 0 - 表示是竖屏  90 - 表示是左横屏(正向)  180 - 表示是反向竖屏  270表示是右横屏（反向）
     */
    public int getDisplayRotation() {

        int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }
}
