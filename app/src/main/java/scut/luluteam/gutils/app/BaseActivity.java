package scut.luluteam.gutils.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import scut.luluteam.gutils.utils.CustomActivityManager;

/**
 * Created by guan on 5/26/17.
 */

public class BaseActivity extends AppCompatActivity {


    /**
     * 自定义Activity样式
     */
    private CustomActivityManager.CustomBuilder customBuilder;

    /**
     * 监听网络状态
     */
    protected static int networkState;
    //protected NetworkStateReceiver networkStateReceiver;


    protected String TAG = this.getClass().getSimpleName();
    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册监听网络的广播
//        networkStateReceiver=new NetworkStateReceiver();
//        IntentFilter networkFilter=new IntentFilter();
//        networkFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(networkStateReceiver,networkFilter);
//        Log.e(TAG,"开始注册");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注销广播
        //unregisterReceiver(networkStateReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
        AppManager.getInstance().printStack();
    }

    private void init() {
        AppManager.getInstance().addActivity(this);
        AppManager.getInstance().printStack();
        customBuilder = new CustomActivityManager.CustomBuilder(BaseActivity.this);

    }


    //region 定制Activity风格

    /**
     * 获取定制Activity的Builder
     *
     * @return
     */
    protected CustomActivityManager.CustomBuilder getCustomBuilder() {
        return customBuilder;
    }


    //======================================================================


    //endregion

}
