package scut.luluteam.gutils.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;
import scut.luluteam.gutils.R;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DownUploadService extends Service {

    public enum ServiceOperation {DOWNLOAD, UPLOAD}

    NotificationManager notificationManager;

    int ID_NOTIFICATION_DOWNLOAD = 89664;
    String TAG_NOTIFICATION_DOWNLOAD = "TAG_NOTIFICATION_DOWNLOAD";
    int ID_NOTIFICATION_UPLOAD = 4101;

    String TAG = "DownUploadService";

    public DownUploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Log.e(TAG, "DownUploadService is on Create");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * @param intent  包含operation类型和Params
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "DownUploadService is on Start");
        ServiceOperation operation = (ServiceOperation) intent.getSerializableExtra("operation");
        if (operation == ServiceOperation.DOWNLOAD) {

            //获取下载参数：url，name，path
            DownloadParams downloadParams = (DownloadParams) intent.getSerializableExtra("DownloadParams");
            //开始下载
            doDownload(downloadParams);


        } else if (operation == ServiceOperation.UPLOAD) {

            //获取参数：url，name，path
            UploadParams uploadParams = (UploadParams) intent.getSerializableExtra("UploadParams");
            //开始上传
            doUpload(uploadParams);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //notificationManager.dismiss(ID_NOTIFICATION_DOWNLOAD);
        Log.e(TAG, "Download and Upload Service has stopped");
    }


    //==============================================================================================

    /**
     * 执行下载
     *
     * @param downloadParams
     */
    private void doDownload(DownloadParams downloadParams) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_file_download_white_24dp);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setContentTitle(downloadParams.localName);//设置标题
        mBuilder.setContentText("详细内容:正在下载……");//设置详细内容
        mBuilder.setTicker("Ticker:正在下载……");//首先弹出来的，用于提醒的一行小字
        mBuilder.setWhen(System.currentTimeMillis());//设置时间
        mBuilder.setProgress(0, 0, false);//设置进度条
        final Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;//不自动清除
        //notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;//前台服务标记
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;//标记声音或者震动一次
        notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知之后，取消状态栏图标
        notificationManager.notify(ID_NOTIFICATION_DOWNLOAD, notification);//显示notification

        /**
         * 知识点：|=，位或. a|=b 等价于 a=a|b. a|b即ab都转换为二进制，然后按位做“或”运算
         */

        Log.d(TAG, "正在下载:" + downloadParams.toString());
        OkHttpManager.download(downloadParams.url, downloadParams.localPath, downloadParams.localName,
                new OkHttpManager.ProgressListener() {
                    @Override
                    public void onProgress(long totalSize, long currSize, boolean done, int id) {
                        Log.v(TAG, "totalSize:" + totalSize + "\tcurrSize:" + currSize);

                        mBuilder.setProgress((int) totalSize, (int) currSize, false);//false：显示下载的百分比；true：不显示百分比，只表示当前正在运行
                        notificationManager.notify(ID_NOTIFICATION_DOWNLOAD, mBuilder.build());//不断的更新进度条
                    }
                },
                new OkHttpManager.ResultCallback() {
                    @Override
                    public void onCallBack(OkHttpManager.State state, String result) {
                        Log.e(TAG, state + "\t" + result);
                        notificationManager.cancel(ID_NOTIFICATION_DOWNLOAD);//获得结果，销毁Notification
                    }
                });


    }

    /**
     * 执行上传
     *
     * @param uploadParams
     */
    private void doUpload(UploadParams uploadParams) {

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_file_upload_white_36dp);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setContentTitle(uploadParams.getFile().getName());//设置标题
        mBuilder.setContentText("详细内容:正在上传……");//设置详细内容
        mBuilder.setTicker("Ticker:正在上传……");//首先弹出来的，用于提醒的一行小字
        mBuilder.setWhen(System.currentTimeMillis());//设置时间
        mBuilder.setProgress(0, 0, false);//设置进度条
        final Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;//不自动清除
        //notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;//前台服务标记
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;//标记声音或者震动一次
        notification.flags |= Notification.FLAG_AUTO_CANCEL;//点击通知之后，取消状态栏图标
        notificationManager.notify(ID_NOTIFICATION_UPLOAD, notification);//显示notification

        OkHttpManager.upload(uploadParams.url,
                new File[]{uploadParams.getFile()},
                new String[]{uploadParams.fileKey},
                uploadParams.params,
                new OkHttpManager.ProgressListener() {
                    @Override
                    public void onProgress(long totalSize, long currSize, boolean done, int id) {
                        mBuilder.setProgress((int) totalSize, (int) currSize, false);//true：显示下载的百分比；false：不显示百分比，只表示当前正在运行
                        notificationManager.notify(ID_NOTIFICATION_UPLOAD, mBuilder.build());//不断的更新进度条
                    }
                }, new OkHttpManager.ResultCallback() {
                    @Override
                    public void onCallBack(OkHttpManager.State state, String result) {
                        Log.e(TAG, state + "\t" + result);
                        notificationManager.cancel(ID_NOTIFICATION_UPLOAD);//获得结果，销毁Notification
                    }
                });

    }

    /**
     * 内部类：定义下载的参数
     */
    public static class DownloadParams implements Serializable {
        public String localName;
        public String url;
        public String localPath;
        public HashMap<String, String> params;

        public DownloadParams(String localName, String url, String localPath, HashMap<String, String> params) {
            this.localName = localName;
            this.url = url;
            this.localPath = localPath;
            this.params = params;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("localName:" + localName + "\t");
            sb.append("url:" + url + "\t");
            sb.append("localPath:" + localPath + "\t");
            if (params == null) {
                sb.append("Params:" + "null");
            } else {
                sb.append("Params:" + "");
                for (Map.Entry entry : params.entrySet()) {
                    sb.append(entry.getKey() + ":" + entry.getValue() + ";");
                }
            }


            return sb.toString();
        }
    }

    /**
     * 内部类：定义上传的参数
     */
    public static class UploadParams implements Serializable {
        public String localPath;
        public String url;
        public String fileKey;
        public HashMap<String, String> params;
        private File file;


        public UploadParams(String localPath, String url, String filekey, HashMap<String, String> params) {
            this.localPath = localPath;
            this.url = url;
            this.fileKey = filekey;
            this.params = params;
        }

        public File getFile() {
            if (localPath == null || localPath == "") {
                return null;
            }

            file = new File(localPath);
            if (file.exists() && file.isFile()) {
                return file;
            } else {
                return null;
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("url:" + url + "\t");
            sb.append("localPath:" + localPath + "\t");
            sb.append("fileKey:" + fileKey + "\t");
            sb.append("Params:" + "");

            for (Map.Entry entry : params.entrySet()) {
                sb.append(entry.getKey() + ":" + entry.getValue() + ";");
            }

            return sb.toString();
        }
    }
}
