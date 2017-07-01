package scut.luluteam.gutils.utils.lock_screen;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.utils.ShowUtil;


/**
 * Created by guan on 5/22/17.
 */

public class DeviceManager {

    /**
     * 设备管理员
     */
    private DevicePolicyManager mDPM;

    /**
     * 键盘锁管理器对象
     */
    private KeyguardManager mKeyguardManager;

    /**
     * 电源管理对象
     */
    private PowerManager powerManager;
    /**
     * 四大组件名的封装类
     */
    private ComponentName mCompName;

    /**
     * 唤醒屏幕后，屏幕亮持续的时间
     */
    private int wakeUpTimeout = 15;

    /**
     * 本类的一个单例
     */
    private static DeviceManager deviceManager;

    /**
     *
     */
    private boolean keepScreenLocked = false;

    private String TAG = "DeviceManager";

    private DeviceManager() {
        // 获取设备管理员
        mDPM = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 键盘锁管理器对象
        mKeyguardManager = (KeyguardManager) App.getAppContext().getSystemService(Context.KEYGUARD_SERVICE);
        // 申请权限
        mCompName = new ComponentName(App.getAppContext(), ScreenReceiver.class);

        powerManager = (PowerManager) App.getAppContext().getSystemService(Context.POWER_SERVICE);
    }

    /**
     * 单例模式：懒汉式
     *
     * @return
     */
    public static DeviceManager getInstance() {
        if (deviceManager == null) {
            synchronized (DeviceManager.class) {
                if (deviceManager == null) {
                    deviceManager = new DeviceManager();
                }
            }
        }
        return deviceManager;
    }


    //=============================API==============================================================

    /**
     * 用于处理“屏幕已经解锁”的系统广播，
     * 由自定义的BroadcastReceiver子类调用
     */
    public void onUnlockScreenMessage() {
        if (keepScreenLocked) {
            lockScreen();
        }
    }

    /**
     * 用于接收命令:持续锁屏、解除锁定
     *
     * @param keepScreenLocked
     */
    public void keepScreenLocked(boolean keepScreenLocked) {
        this.keepScreenLocked = keepScreenLocked;
        if (keepScreenLocked) {
            lockScreen();
        } else {
            wakeUp();
        }
    }

    /**
     * 激活管理员权限
     */
    public void activeAdmin(Context mContext) {
        if (isAdminActive()) {
            Log.v(TAG, "--已经获取管理员权限--");
            return;
        }
        Log.e(TAG, "没有管理员权限---启动系统activity让用户激活管理员权限");
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCompName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "只有激活了管理员权限才能锁屏,清理缓存");
        mContext.startActivity(intent);
    }


    //=========================本类内部实现功能的具体方法================================================

    /**
     * 锁屏
     */
    private void lockScreen() {
        if (isAdminActive() && !isLocked()) {
            // 已经获取管理员权限可以锁屏
            mDPM.lockNow();
        } else {
            ShowUtil.LogAndToast("操作失败，请先开通管理员权限");
        }
    }

    /**
     * 点亮屏幕
     * <p>
     * PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
     * <p>
     * SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
     * <p>
     * SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
     * <p>
     * FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
     * <p>
     * ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
     * <p>
     * ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     */
    private void wakeUp() {
        powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "Wake Up Screen")
                .acquire(wakeUpTimeout);
    }

    /**
     * 判断是否锁屏
     * 屏幕“亮”，表示有两种状态：a、未锁屏 b、目前正处于解锁状态 。这两种状态屏幕都是亮的
     * 屏幕“暗”，表示目前屏幕是黑的
     * <p>
     * 如果flag为true，表示有两种状态：a、屏幕是黑的 b、目前正处于解锁状态。
     * 如果flag为false，表示目前未锁屏
     *
     * @return
     */
    private boolean isLocked() {
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    /**
     * 判断是否获得管理员权限
     *
     * @return
     */
    private boolean isAdminActive() {
        return mDPM.isAdminActive(mCompName);
    }

}
