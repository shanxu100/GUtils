package scut.luluteam.gutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by guan on 4/13/17.
 */
@Deprecated
public class PermissionUtil {

    private static String TAG = "GUtils.PermissionUtils";
    private static final int MY_PERMISSIONS_REQUEST = 3000;

    //Context mContext;
    private static ArrayList<String> permissionList;


    private PermissionUtil() {
        //this.mContext = mContext;
        this.permissionList = permissionList;
    }

    /**
     * @param permissionList
     */
    public static void setPermissionList(ArrayList<String> permissionList) {
        PermissionUtil.permissionList = permissionList;
    }

    /**
     * 主方法一：调用申请权限功能
     */
    public static void checkAndRequestPermissions(Context mContext, ArrayList<String> permissionList) {

        if (permissionList == null || permissionList.size() == 0) {
            return;
        }

        PermissionUtil.permissionList = permissionList;

        ArrayList<String> list = new ArrayList<>(PermissionUtil.permissionList);
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String permission = it.next();
            int hasPermission = ContextCompat.checkSelfPermission(mContext, permission);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                it.remove();
            }
        }

        if (list.size() == 0) {
            return;
        }
        String[] permissions = list.toArray(new String[0]);
        ActivityCompat.requestPermissions((Activity) mContext, permissions, MY_PERMISSIONS_REQUEST);

    }

    /**
     * 主方法二：对申请权限的结果进行处理
     * 申请权限后，需要在Activity中重写onRequestPermissionsResult()函数。
     * 在重写过程中，把该函数的参数传递到本函数中处理即可。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void resultCallback(int requestCode, @NonNull String[] permissions,
                                      @NonNull int[] grantResults, final Context mContext) {
        if (requestCode != MY_PERMISSIONS_REQUEST) {
            return;
        }
        // If request is cancelled, the result arrays are empty.
        int length = grantResults.length;
        boolean re_request = false;
        for (int i = 0; i < length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "权限授予成功:" + permissions[i]);
            } else {
                Log.e(TAG, "权限授予失败:" + permissions[i]);
                re_request = true;
            }
        }
//        if (re_request) {
//            //弹出对话框，提示用户重新授予权限
//            final G_AlertDialog permissionDialog = new G_AlertDialog(mContext);
//            permissionDialog.setCanceledOnTouchOutside(false);
//            permissionDialog.setMeesage("请授予相关权限，否则程序无法运行。\n\n点击确定，重新授予权限。\n点击取消，立即终止程序。\n");
//            permissionDialog.setCallback(new G_AlertDialog.YesOrNoDialogCallback() {
//                @Override
//                public void onClickButton(G_AlertDialog.ClickedButton button, String message) {
//                    if (button == G_AlertDialog.ClickedButton.POSITIVE) {
//                        permissionDialog.dismiss();
//                        //此处需要弹出手动修改权限的系统界面
//                        checkAndRequestPermissions(mContext, PermissionUtil.permissionList);
//                    } else if (button == G_AlertDialog.ClickedButton.NEGATIVE) {
//                        permissionDialog.dismiss();
//                        ((Activity) mContext).finish();
//                    }
//                }
//            });
//
//            permissionDialog.show();
//        }
    }
}
