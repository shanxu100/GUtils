package scut.luluteam.gutils.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by guan on 5/31/17.
 */

public class ImageUtil {
    private static String TAG = "ImageUtil";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean saveImage(Image image, String parentPath, String imageName) {
        String path = parentPath + File.separator + imageName;

        Log.e(TAG, "1");
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        //每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        //总的间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        FileOutputStream out = null;
        Log.e(TAG, "2");
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                Bitmap.Config.ARGB_8888);
        Log.e(TAG, "3");
        bitmap.copyPixelsFromBuffer(buffer);
        Log.e(TAG, "4");
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();
        File fileImage = null;
        if (bitmap != null) {
            try {

                if (TextUtils.isEmpty(path)) {
                    //mLocalUrl = mContext.getExternalFilesDir("screenshot").getAbsoluteFile() + "/" + SystemClock.currentThreadTimeMillis() + ".png";
                }
                fileImage = new File(path);

                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                }
                //fileImage.createNewFile();
                Log.e(TAG, "5");
                out = new FileOutputStream(fileImage);
                if (out != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                }
                Log.e(TAG, "6");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
                //fileImage = null;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
                //fileImage = null;
            } finally {
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                bitmap.recycle();
            }
        }
        Log.e(TAG, "7");
        return true;
    }
}
