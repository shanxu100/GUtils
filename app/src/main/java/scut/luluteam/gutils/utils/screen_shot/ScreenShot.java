package scut.luluteam.gutils.utils.screen_shot;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;


import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.utils.DisplayUtil;
import scut.luluteam.gutils.utils.ImageUtil;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;


/**
 * 第一步:在某一个Activity中发起，请求截屏权限
 * Intent requsetScreenShotIntent=new Intent(mContext, ScreenShotActivity.class);
 * startActivity(requsetScreenShotIntent);
 * <p>
 * 第二步:ScreenShotActivity将相关的ResultCode和Intent保存在Application中。
 * 所以Application中需要添加相关
 * <p>
 * <p>
 * Created by guan on 5/31/17.
 */

public class ScreenShot {

    private static final String TAG = "ScreenShot";
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    private static Intent ScreenShotData;
    private static int ScreenShotResultCode = -2;

    private HandlerThread handlerThread;
    private Handler handler;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScreenShot() {

        if (!hasInit()) {
            Log.e(TAG, "尚未获得用户截屏允许，请先完成初始化");
            return;
        }
        mediaProjectionManager = (MediaProjectionManager) App.getAppContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(ScreenShotResultCode, ScreenShotData);
        imageReader = ImageReader.newInstance(DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight(),
                PixelFormat.RGBA_8888,// 此处RGB_565 必须和下面 buffer处理一致的格式
                1);
    }

    public static void setInitData(int ScreenShotResultCode, Intent ScreenShotData) {
        ScreenShot.ScreenShotResultCode = ScreenShotResultCode;
        ScreenShot.ScreenShotData = ScreenShotData;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void start() {
        handlerThread = new HandlerThread("ScreenShotHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());


        //startVirtual
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                virtualDisplay = mediaProjection.createVirtualDisplay("screen-mirror", DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight(),
                        Resources.getSystem().getDisplayMetrics().densityDpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        imageReader.getSurface(), null, null);
            }
        }, 500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Image image = imageReader.acquireLatestImage();
                if (image != null) {
                    Log.e("ScreenShotHandlerThread", "获取屏幕成功");
                } else {
                    Log.e("ScreenShotHandlerThread", "获取屏幕失败");
                    return;
                }

                ImageUtil.saveImage(image, App.APP_DIR, "ScreenShot" + System.currentTimeMillis() + ".png");
                Log.e("ScreenShotHandlerThread", "Image写入成功");

                Log.e("ScreenShotHandlerThread", "onPostExecute:截屏成功");
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (virtualDisplay != null) {
                    virtualDisplay.release();
                    virtualDisplay = null;
                }
                if (mediaProjection != null) {
                    mediaProjection.stop();
                    mediaProjection = null;
                }
                Log.e("ScreenShotHandlerThread", "释放截屏资源");


            }
        }, 1500);

    }

    private boolean hasInit() {
        if (ScreenShotData != null && ScreenShotResultCode != -2) {
            return true;
        } else {
            return false;
        }
    }

}