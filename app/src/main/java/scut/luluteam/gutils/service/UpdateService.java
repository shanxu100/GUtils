package scut.luluteam.gutils.service;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;

import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.app.AppManager;
import scut.luluteam.gutils.utils.ChmodUtil;
import scut.luluteam.gutils.utils.ShowUtil;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;
import scut.luluteam.gutils.view.LoadingDialog;


/**
 *
 */
public class UpdateService extends Service {

    private Handler handler;
    private static String TAG = "UpdateService";
    private String downloadFilePath;
    private File directory;

    private String CheckURL = "";
    private String DownloadURL = "";

    /**
     * 第一次启动该Service的时候，延迟10s再检查更新
     */
    private long requestDelay = 10000;

    public UpdateService() {
        handler = new Handler();
        directory = AppManager.getInstance().currentActivity().getCacheDir();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.removeMessages(0, null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        }, requestDelay);
        requestDelay = 1000;
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
//        ShowUtil.Toast(getApplicationContext(), "正在检查更新...");
        OkHttpManager.CommonGetAsyn(CheckURL, null, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(OkHttpManager.State state, String result) {
                if (state == OkHttpManager.State.SUCCESS) {
//                    Log.e(TAG, "update=====" + result);
                    final CheckUpdateResult checkUpdateResult = new Gson().fromJson(result, CheckUpdateResult.class);
                    if (checkUpdateResult.isResult() && checkUpdateResult.getVersionCode() > App.getVersionCode()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                needUpdate(checkUpdateResult);
                            }
                        });
                    } else {
                    }
                } else {
                    ShowUtil.Toast(getApplicationContext(), "操作失败：" + result);
                }
            }
        });

    }


    /**
     * 检测更新完成后，需要更新
     */
    private void needUpdate(final CheckUpdateResult checkUpdateResult) {
        AlertDialog alertDialog;
        Log.e(TAG, "currentActivity()=" + AppManager.getInstance().currentActivity());
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
        ShowUtil.Toast(getApplicationContext(), "开始下载更新...");
        final LoadingDialog loadingDialog = new LoadingDialog(AppManager.getInstance().currentActivity());
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        String url = DownloadURL + checkUpdateResult.getPostfixURL();
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        downloadFilePath = directory + File.separator + fileName;
        Log.e(TAG, "update App===========download =" + downloadFilePath);
        OkHttpManager.download(url, directory.getAbsolutePath(), fileName, new OkHttpManager.ProgressListener() {
            @Override
            public void onProgress(long totalSize, long currSize, boolean done, int id) {
//                System.out.println("download=====" + currSize);
                if (done) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            installAPK(downloadFilePath);
                        }
                    });
                }
            }
        }, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(OkHttpManager.State state, final String result) {
                if (state != OkHttpManager.State.SUCCESS) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismiss();
                            ShowUtil.Toast(getApplicationContext(), "下载更新失败..." + result);
                        }
                    });

                }
            }
        });

    }


    @MainThread
    private void installAPK(String fileFillPath) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(fileFillPath);
        Uri uri;
        //对Android 版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // context.getPackageName() + ".fileprovider"  是配置中的authorities
            uri = FileProvider.getUriForFile(getApplicationContext(), "luluteam.bath.bathprojectas.fileProvider", apkFile);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            uri = Uri.fromFile(apkFile);
            //由于是放在了cache目录，所以没有执行的权限------一个坑
            ChmodUtil.chmod("777", apkFile.getAbsolutePath());
            install.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(install);
    }

    public class CheckUpdateResult {
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
