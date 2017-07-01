package scut.luluteam.gutils.service.floatwindow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import scut.luluteam.gutils.R;

/**
 * 这里是悬浮窗的入口:透明的Activity
 */
@RuntimePermissions
public class FloatWinPermissionActivity extends Activity {

    private String TAG="FloatWinPermActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_flt_win_perm);


        /**
         * 申请权限：如果没有权限，则只能在本应用上方显示悬浮窗
         */
        FloatWinPermissionActivityPermissionsDispatcher.needSystemAlertWindowPermissinWithCheck(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FloatWinPermissionActivityPermissionsDispatcher.onActivityResult(this, requestCode);
    }

    /**
     * 申请这个“SYSTEM_ALERT_WINDOW”悬浮窗的权限，会通过Intent打开系统设置界面，然后手动赋予。
     * 如果赋予成功，则调用这里
     */
    @NeedsPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
    protected void needSystemAlertWindowPermissin() {
        Log.e(TAG, "权限申请成功");
        FloatWinPermissionActivity.this.finish();
    }


    @OnShowRationale(Manifest.permission.SYSTEM_ALERT_WINDOW)
    protected void onShowRationale(final PermissionRequest request) {
        //request.proceed();
    }

    /**
     * 由于国内深度定制系统，拒绝后不会调用这里
     */
    @OnPermissionDenied(Manifest.permission.SYSTEM_ALERT_WINDOW)
    protected void OnPermissionDenied() {
        Log.e(TAG, "拒绝权限申请 ……");
        FloatWinPermissionActivity.this.finish();

    }

    /**
     * 打开系统设置界面后，如果没有赋予权限，则调用这里
     */
    @OnNeverAskAgain(Manifest.permission.SYSTEM_ALERT_WINDOW)
    protected void OnNeverAskAgain() {
        Log.e(TAG, "不再提示申请权限 ……");
        FloatWinPermissionActivity.this.finish();
    }
}
