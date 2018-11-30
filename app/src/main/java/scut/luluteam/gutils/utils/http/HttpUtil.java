package scut.luluteam.gutils.utils.http;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by guan on 5/20/17.
 */
@Deprecated
public class HttpUtil {

    private static String TAG = "HttpUtil";

    /**
     * 此方法有缺陷，还需改善
     *
     * @param url_str
     */
    public static void getFileNameFromUrl(String url_str) {
        //URLDecoder decoder=new URLDecoder();
        url_str = "https://codeload.github.com/shanxu100/GUtils/zip/master";

        try {
            URL url = new URL(url_str);
            final URLConnection conn = url.openConnection();

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        conn.connect();
                        if (((HttpURLConnection) conn).getResponseCode() == 200) {
                            String filename = conn.getURL().getFile();
                            //String filename = file.substring(file.lastIndexOf('/') + 1);
                            //ShowUtil.Toast(mContext, filename);
                            Log.e(TAG, filename);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
