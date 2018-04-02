package scut.luluteam.gutils.service;

import android.app.IntentService;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.app.AppManager;
import scut.luluteam.gutils.utils.ChmodUtil;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;

/**
 * 检查并自动更新
 */
public class UpdateService extends Service {

    private static String TAG = "UpdateService";

    private Handler handler;
    private String downloadFilePath;
    private File directory;
    private String WebHost = "";
    private String URL_checkUpdate = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler(Looper.getMainLooper());
        directory = getApplication().getCacheDir();
        WebHost = "http://125.216.242.147:8080/bathProject";//这里设置url
        URL_checkUpdate = WebHost + "/resources/app/androidApp.json";
        checkUpdate();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 发起检查更新的请求
     * <p>
     * VersionCode：2    给开发者比较版本是否有升级用的
     * VersionName：1.1  展示给用户看的
     */
    private void checkUpdate() {
        Log.e(TAG, " App.getVersionCode()=" + App.getVersionCode());
        OkHttpManager.CommonGetAsyn(URL_checkUpdate, null, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(final OkHttpManager.State state, final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (state == OkHttpManager.State.SUCCESS) {
                            CheckUpdateResult checkUpdateResult = new Gson().fromJson(result,
                                    CheckUpdateResult.class);
                            if (checkUpdateResult.isResult()) {
                                if (checkUpdateResult.getVersionCode() > App.getVersionCode()) {
                                    needUpdate(checkUpdateResult);
                                } else {
                                    stopAndShow("当前已是最新版本----" +
                                            "\nVersionName:" + App.getVersionName() +
                                            "\nVersionCode:" + App.getVersionCode());
                                }
                            } else {
                                stopAndShow("服务器返回数据失败：" + result);
                            }
                        } else {
                            stopAndShow("网络操作失败：" + result);
                        }
                    }
                });


            }
        });

    }


    /**
     * 检测更新完成后，需要更新
     */
    @MainThread
    private void needUpdate(final CheckUpdateResult checkUpdateResult) {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().currentActivity())
                .setTitle("更新提示")
                .setMessage("检测到新版本，是否更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doUpdate(checkUpdateResult);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stopAndShow("用户拒绝更新......");
                    }
                });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    /**
     * 执行更新的动作
     */
    private void doUpdate(CheckUpdateResult checkUpdateResult) {
        showToast("开始下载,请稍后");
        String url = WebHost + checkUpdateResult.getPostfixURL();
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        downloadFilePath = directory + "/" + fileName;
        //  /data/user/0/包名/cache/文件名
        Log.e(TAG, "update App===========download =" + downloadFilePath);
        OkHttpManager.download(url, directory.getAbsolutePath(), fileName, new OkHttpManager.ProgressListener() {
            @Override
            public void onProgress(long totalSize, long currSize, boolean done, int id) {
//                System.out.println("download=====" + currSize);
                if (done) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            installAPK(downloadFilePath);
                            stopAndShow("安装更新......");
                        }
                    });
                }
            }
        }, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state != OkHttpManager.State.SUCCESS) {
                    stopAndShow("下载文件失败");
                }
            }
        });

    }

    /**
     * 安装APK---未完成
     *
     * @param fileFillPath
     */
    private void installAPK(String fileFillPath) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(fileFillPath);
        Uri uri;
        //对Android 版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // context.getPackageName() + ".fileprovider"  是配置中的authorities
            uri = FileProvider.getUriForFile(getApplicationContext(), "scut.luluteam.gutils.fileProvider", apkFile);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            uri = Uri.fromFile(apkFile);
            //由于是放在了cache目录，所以没有执行的权限------一个坑
            ChmodUtil.chmod("777", apkFile.getAbsolutePath());
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(install);
    }

    /**
     * 通过Toast显示提示
     * 注意：Toast可以在UI线程中创建并显示。在非UI线程中，可以使用handler,使Toast运行在主线程中
     *
     * @param text
     */
    private void showToast(final String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * 停止Service，并打印原因
     *
     * @param reason
     */
    private void stopAndShow(final String reason) {
        showToast(reason);
        Log.e(TAG, reason);
        stopSelf();
    }


    public static class CheckUpdateResult {
        private boolean result;
        private int versionCode;
        private String postfixURL;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getPostfixURL() {
            return postfixURL;
        }

        public void setPostfixURL(String postfixURL) {
            this.postfixURL = postfixURL;
        }
    }


}
