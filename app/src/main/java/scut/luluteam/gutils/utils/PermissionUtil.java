package scut.luluteam.gutils.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luluteam on 2017/11/22.
 */

public class PermissionUtil {

    private static final String TAG = "PermissionUtil";
    private static final String REQUEST_PERMISSION_MSG = "保证应用正常运行，请授予相关权限";

    private PermissionUtil() {
        throw new AssertionError("PermissionUtil");
    }


    //==========================================================================================
    //region 权限检查相关方法
    //==========================================================================================

    /**
     * 检测单个权限是否授权
     *
     * @param mContext
     * @param permission
     * @return true 已授权 false 未授权
     */
    public static boolean checkPermission(Context mContext, String permission) {
        if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "已授予权限:" + permission);
            return true;
        } else {
            Log.e(TAG, "未授予权限:" + permission);
            return false;
        }
    }


    /**
     * 检查权限是否成功赋予
     */
    public static boolean checkPermission(Context mContext, String[] permissions) {
        boolean result = true;
        for (String permission : permissions) {
            result = checkPermission(mContext, permission);
        }
        return result;
    }


    /**
     * 检测多个权限是否授权，并返回未授权的权限列表
     *
     * @param mContext
     * @param permissions 数组模式
     * @return 未授权的权限字符串
     */
    public static List<String> getNotGrantPermissions(Context mContext, String[] permissions) {
        return getNotGrantPermissions(mContext, Arrays.asList(permissions));
    }

    /**
     * 检测多个权限是否授权，并返回未授权的权限列表
     *
     * @param mContext
     * @param permissions 列表模式
     * @return 未授权的权限字符串
     */
    public static List<String> getNotGrantPermissions(Context mContext, List<String> permissions) {

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(mContext, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 检查 系统App 拥有的权限
     * 如：android:get_usage_stats
     *
     * @param context
     * @param op
     * @return
     */
    public static boolean checkSystemAppPermission(Context context, String op) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(op, android.os.Process.myUid(), context.getPackageName());
        boolean result = (mode == AppOpsManager.MODE_ALLOWED);
        if (result) {
            Log.e(TAG, "已经授予权限permission:" + op);
        } else {
            Log.e(TAG, "未授予权限permission:" + op);
        }
        return result;
    }

    //==================================================================================
    //endregion权限检查方法结束,
    // region权限申请方法开始
    //==================================================================================

    /**
     * 请求单个权限
     *
     * @param mContext
     * @param permission  单个权限
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, String permission, int requestCode) {
        requestPermission(mContext, new String[]{permission}, requestCode);
    }

    /**
     * 请求多个权限
     *
     * @param mContext
     * @param permissions 权限数组
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) mContext, permissions, requestCode);
    }

    /**
     * 请求多个权限
     *
     * @param mContext
     * @param permissions 权限列表
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, List<String> permissions, int requestCode) {
        requestPermission(mContext, permissions.toArray(new String[permissions.size()]), requestCode);
    }

    //==================================================================================
    //endregion 权限申请方法结束权限其他方法开始
    //==================================================================================


    /**
     * 向用户解释为什么需要申请权限
     * 返回false：表示用户之前彻底禁止了该权限
     * 返回true：如果用户之前只是拒绝了该权限，那么会弹出dialog，向用户解释
     *
     * @param mContext
     * @param permission
     * @return
     */
    public static boolean showRequestPermissionRationale(Context mContext, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
            new AlertDialog.Builder(mContext)
                    .setMessage(REQUEST_PERMISSION_MSG)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            return true;
        }
        return false;
    }

}
