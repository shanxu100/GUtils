package scut.luluteam.gutils.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import scut.luluteam.gutils.utils.DisplayUtil;


/**
 * Created by guan on 5/19/17.
 */

public class App extends Application {

    private String TAG = "G_App";
    private static Context appContext;

    /**
     * 本应用所用到的目录路径
     */
    private static String BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String APP_DIR = BASE_DIR + "/" + "GUtils";


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        initApp();
        //ShowUtil.UIToast(appContext, "onCreate");

    }


    private void initApp() {

        /**
         * APM
         */
//        APMInstance apmInstance = APMInstance.getInstance();	//得到单例对象
//        apmInstance.setSendStrategy(APMInstance.SEND_INSTANTLY);	//选择上报策略
//        apmInstance.start(this);	//开始监控

        /**
         * 保存屏幕尺寸
         */
        DisplayUtil.init(getApplicationContext());

        /**
         * 创建app的文件夹
         */
        File appDir_file = new File(APP_DIR);
        if (!appDir_file.exists()) {
            appDir_file.mkdirs();
        }

        /**
         * 启动 悬浮窗:此处两行代码应该由具体的Activity来调用。这里仅测试
         */
        //Intent intent = new Intent(appContext, FloatWindowService.class);
        //startService(intent);

        /**
         * 开启Mqtt Service
         */
//        Intent mqttIntent = new Intent(appContext, MQTTService.class);
//        startService(mqttIntent);

        LeakCanary.install(this);

    }


    public static Context getAppContext() {
        return appContext;
    }


    /**
     * 版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = appContext.getPackageManager();
            packageInfo = pm.getPackageInfo(appContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名
     * @return
     */
    public static String getVersionName()//获取版本号
    {
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "unknown version name";
        }
    }

}
