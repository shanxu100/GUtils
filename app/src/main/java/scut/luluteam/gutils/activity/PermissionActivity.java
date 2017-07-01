package scut.luluteam.gutils.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;


import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import scut.luluteam.gutils.R;
import scut.luluteam.gutils.view.G_AlertDialog;

/**
 * 将该Activity继承Activity，并设置成透明主题
 */
@RuntimePermissions
public class PermissionActivity extends Activity {
    private String TAG = "PermissionActivity";
    private G_AlertDialog permissionAlertDialog;
    private PermissionRequest request;

    /**
     * 如何定义真正的常量数数组
     */
    //static final String[] PERMISSIONLIST = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        init();

    }

    private void init() {
        /**
         * 设置“提示重新申请权限”的对话框
         */
        permissionAlertDialog = new G_AlertDialog(this);
        permissionAlertDialog.setMeesage("请赋予相关权限，\n\n否则程序无法征程运行");
        permissionAlertDialog.setCanceledOnTouchOutside(false);
        permissionAlertDialog.setCallback(new G_AlertDialog.YesOrNoDialogCallback() {
            @Override
            public void onClickButton(G_AlertDialog.ClickedButton button, String message) {
                if (button == G_AlertDialog.ClickedButton.POSITIVE) {
                    request.proceed();
                    permissionAlertDialog.dismiss();
                } else if (button == G_AlertDialog.ClickedButton.NEGATIVE) {
                    request.cancel();
                    permissionAlertDialog.dismiss();
                }
            }
        });

        //handlerThread.

        /**
         * 这个方法要在 编译 之后才会出现 —— 委托
         */
        // NOTE: delegate the permission handling to generated method
        PermissionActivityPermissionsDispatcher.showMultiPermissionWithCheck(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    //======================================================================================

    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO})
    void showMultiPermission() {
        Log.e(TAG, "权限申请成功");
        PermissionActivity.this.finish();
    }

    @OnShowRationale({Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO})
    void showRationale(final PermissionRequest request) {
        /**
         * 向用户解释，需要重新申请权限
         */
        this.request = request;
        permissionAlertDialog.show();
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO})
    void showDenied() {
        /**
         * 在多个权限申请过程中，有一个或多个申请失败，就会调用这里“一次”
         */
        Log.e(TAG, "拒绝权限申请 ……");
        PermissionActivity.this.finish();

    }

    @OnNeverAskAgain({Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO})
    void showNever() {
        Log.e(TAG, "不再提示申请权限 ……");
        PermissionActivity.this.finish();

    }


}
