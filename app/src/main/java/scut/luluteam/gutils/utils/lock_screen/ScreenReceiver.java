package scut.luluteam.gutils.utils.lock_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.app.admin.DeviceAdminReceiver.ACTION_DEVICE_ADMIN_ENABLED;

/**
 * Created by guan on 5/22/17.
 */

/**
 * 采用静态注册：
 * 这种方式的注册是常驻型的，也就是说当应用关闭后，如果有广播信息传来，MyReceiver也会被系统调用而自动运行。
 */

public class ScreenReceiver extends BroadcastReceiver {

    private String action = null;
    private String TAG = "ScreenStateReceiver";


    /**
     * 这两个action只能通过代码的形式注册，才能被监听到
     * 1
     * <action android:name="android.intent.action.SCREEN_ON" />
     * 2
     * <action android:name="android.intent.action.SCREEN_OFF" />
     *
     * @param context
     * @param intent
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_USER_PRESENT.equals(action)) {
            // 解锁
            Log.e(TAG, "ACTION_USER_PRESENT：屏幕已经解锁");
            //通知DeviceManager：屏幕已经解锁
            DeviceManager.getInstance().onUnlockScreenMessage();

        } else if (ACTION_DEVICE_ADMIN_ENABLED.equals(action)) {
            /**
             * 注意：对于处理“设备管理”的广播，可以继承DeviceAdminReceiver类，做专门处理。
             * 在此处，因为功能简单，就直接继承了BroadcastReceiver，做简单的处理
             */
            //申请管理员权限
            Log.v(TAG, "Succeed to Active Admin");
        }
    }


}
