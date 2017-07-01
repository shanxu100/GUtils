package scut.luluteam.gutils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import scut.luluteam.gutils.utils.NetUtil;


public class NetworkStateReceiver extends BroadcastReceiver {
    private String TAG="NetworkStateReceiver";
    private static NetworkStateListener networkStateListener;

    public static void setNetworkStateListener(NetworkStateListener listener)
    {
        networkStateListener=listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //根据广播来判断当前网络状态——不准确
        Log.e(TAG,intent.getAction());
//        Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//        if (null != parcelableExtra) {
//            NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//            NetworkInfo.State state = networkInfo.getState();
//            Log.e(TAG,"networkInfo.getState():"+networkInfo.getState()+networkInfo.getTypeName()+networkInfo.isAvailable());
//            boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
//            Log.e(TAG, "isConnected:" + isConnected);
//        }
        //仅仅获取当前网络发生了改变这个动作
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int networkState = NetUtil.getNetworkState(context);
            // 接口回调传过去状态的类型
            if (networkStateListener!=null)
            {
                networkStateListener.onNetStateChange(networkState);
            }

        }
    }

    public interface NetworkStateListener {
        void onNetStateChange(int networkState);
    }
}
