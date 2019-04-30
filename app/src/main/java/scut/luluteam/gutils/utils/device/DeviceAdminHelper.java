package scut.luluteam.gutils.utils.device;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import scut.luluteam.gutils.app.App;


/**
 * @author Guan
 * @date Created on 2019/2/25
 */
public class DeviceAdminHelper {

    private static final String TAG = "DeviceAdminHelper";

    /**
     * 设备管理员
     */
    private DevicePolicyManager mDPM;
    /**
     * 四大组件名的封装类
     */
    private ComponentName mCompName;

    public DeviceAdminHelper() {
        if (mDPM == null) {
            // 获取设备管理员
            mDPM = (DevicePolicyManager) App.getAppContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        mCompName = new ComponentName(App.getAppContext(), MyDeviceAdminReceiver.class);
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
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "只有激活了管理员权限才能继续操作");
        mContext.startActivity(intent);
    }


    public boolean isAdminActive() {
        return mDPM.isAdminActive(mCompName);
    }

}
