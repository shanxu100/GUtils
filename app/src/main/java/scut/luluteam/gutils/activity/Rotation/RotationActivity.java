package scut.luluteam.gutils.activity.Rotation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;

/**
 * @author Guan
 */
public class RotationActivity extends BaseActivity implements MyOrientationEventListener.OnChangeOrientationListener {

    // 屏幕方向改变监听器
    private MyOrientationEventListener mOrientationListener;
    // 是否是横屏
    private boolean mIsLand = false;
    // 是否点击
    private boolean mClick = false;
    // 点击进入横屏
    private boolean mClickLand = true;
    // 点击进入竖屏
    private boolean mClickPort = true;

    private Button mButton;

    private WindowManager mWindowManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
//        startListener();
        mButton = (Button) findViewById(R.id.btn_rotation);
        Log.e(TAG, "当前Orientation:" + getRequestedOrientation());

        mOrientationListener = new MyOrientationEventListener(mContext);
        mOrientationListener.setListener(this);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrientationListener.isWideScrren()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationListener.disable();
    }
    @Override
    public void onLandscape(boolean isNormalLandscape) {
        if (isNormalLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }

    @Override
    public void onPortrait() {

    }


}
