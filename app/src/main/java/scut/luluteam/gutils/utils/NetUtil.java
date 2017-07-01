package scut.luluteam.gutils.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by guan on 6/6/17.
 */

public class NetUtil {

    /**
     * 连接管理器
     */
    private static ConnectivityManager connectivityManager;
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    private static final int NETWORK_WIFI = 1;

    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_MOBILE;
            } else {
                return NETWORK_NONE;
            }
        } else {
            return NETWORK_NONE;
        }
    }
}
