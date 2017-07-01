package scut.luluteam.gutils.service.floatwindow;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {

    Context appContext;//一定是Application的context

    private String TAG = "FloatWindowService";
    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();
    /**
     * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
     */
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
        Log.d(TAG, "float Window service onCreate");

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * 开启Activity，申请悬浮窗权限
         */
        Intent floatWindowIntent = new Intent(appContext, FloatWinPermissionActivity.class);
        floatWindowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(floatWindowIntent);

        /**
         *
         * 开启定时器，每隔0.5秒刷新一次：创建并循环显示悬浮窗
         */
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask_ShowAllTime(), 0, 500);
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
        Log.d(TAG, "float Window service destroy");
    }


    /**
     * 设置悬浮窗的显示方式：本app在前台时，不显示悬浮窗；本app后台工作时，显示悬浮窗
     */
    private class RefreshTask_ShowAppRunningBackground extends TimerTask {

        @Override
        public void run() {
            if (!isAppForeground() && !FloatWindowManager.isWindowShowing()) {
                // 当前界面不是本应用程序，且没有悬浮窗显示，则创建悬浮窗。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.createSmallWindow(appContext);
                    }
                });
            } else if (isAppForeground() && FloatWindowManager.isWindowShowing()) {
                // 当前界面是本应用程序，且有悬浮窗显示，则移除悬浮窗。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.removeSmallWindow(appContext);
                        FloatWindowManager.removeBigWindow(appContext);
                    }
                });
            }
        }

    }

    /**
     * 设置悬浮窗的显示方式:悬浮窗一直显示
     */
    private class RefreshTask_ShowAllTime extends TimerTask {

        @Override
        public void run() {
            if (!FloatWindowManager.isWindowShowing()) {
                // 当前没有悬浮窗显示，则创建悬浮窗。
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Log.e("Refresh", "start to showWithAnim");
                        FloatWindowManager.createSmallWindow(appContext);
                    }
                });
            }
        }

    }

    //===========================用于判断状态的辅助方法=================================================

    /**
     * 判断app是否处于前台
     *
     * @return
     */
    private boolean isAppForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(getPackageName()) &&
                    processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }


    //==============================================================================

    /**
     * 内部类，用于统一管理悬浮窗：创建、删除、更新等等
     */

}
