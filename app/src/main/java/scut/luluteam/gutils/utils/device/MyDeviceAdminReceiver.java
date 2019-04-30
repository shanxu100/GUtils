package scut.luluteam.gutils.utils.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.app.admin.DeviceAdminReceiver.ACTION_DEVICE_ADMIN_ENABLED;

/**
 * @author Guan
 */
public class MyDeviceAdminReceiver extends BroadcastReceiver {

    private static final String TAG = "MyDeviceAdminReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_DEVICE_ADMIN_ENABLED.equals(action)) {
            /**
             * 注意：对于处理“设备管理”的广播，可以继承DeviceAdminReceiver类，做专门处理。
             * 在此处，因为功能简单，就直接继承了BroadcastReceiver，做简单的处理
             */
            //申请管理员权限
            Log.v(TAG, "成功获取设备管理员权限");
        }
    }
}
