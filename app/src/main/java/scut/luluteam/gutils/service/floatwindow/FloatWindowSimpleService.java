package scut.luluteam.gutils.service.floatwindow;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.utils.DisplayUtil;

import static scut.luluteam.gutils.utils.DisplayUtil.getStatusBarHeight;


/**
 * 这是一个简易的悬浮窗Service，只包含一个小窗口
 */
public class FloatWindowSimpleService extends Service {

    private WindowManager windowManager;

    /**
     * 布局
     */
    private LinearLayout simple_LL_view;//View的子类
    /**
     * 布局中的控件
     */
    private TextView small_tv;

    /**
     * 记录系统状态栏的高度
     */
    private int statusBarHeight = getStatusBarHeight();
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    /**
     * 误差
     */
    private float error = 10;


    public FloatWindowSimpleService() {
    }

    @Override
    public void onCreate() {

        windowManager = (WindowManager) App.getAppContext().getSystemService(Context.WINDOW_SERVICE);

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createFloatView();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        removeFloatView();
        super.onDestroy();
    }
    //========================================================================================

    /**
     * 点击悬浮窗按钮触发的事件
     */
    private void doClick() {
        //在这里写具体的事件
    }

    /**
     * 创建悬浮窗
     */
    private void createFloatView() {

        if (isFloatWindowShowing()) {
            return;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGBA_8888);
        params.x = 0;
        params.y = DisplayUtil.getScreenHeight() / 2;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        simple_LL_view = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_floatwindow_small, null);
        small_tv = (TextView) simple_LL_view.findViewById(R.id.small_tv);
        windowManager.addView(simple_LL_view, params);

        /**
         * 重点：捕获拖动以及点击事件。
         * 这种实现方式的用户体验不错
         * 注意：即使悬浮窗中的控件是Button，也不要添加onClickListener。因为以下对点击事件进行了新的定义
         */
        small_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                if (action == MotionEvent.ACTION_DOWN) {

                    // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                    xInView = motionEvent.getX();
                    yInView = motionEvent.getY();
                    xDownInScreen = motionEvent.getRawX();
                    yDownInScreen = motionEvent.getRawY() - statusBarHeight;
                    xInScreen = motionEvent.getRawX();
                    yInScreen = motionEvent.getRawY() - statusBarHeight;

                } else if (action == MotionEvent.ACTION_MOVE) {
                    xInScreen = motionEvent.getRawX();
                    yInScreen = motionEvent.getRawY() - statusBarHeight;
                    // 手指移动的时候更新小悬浮窗的位置
                    params.x = (int) (xInScreen - xInView);
                    params.y = (int) (yInScreen - yInView);
                    windowManager.updateViewLayout(simple_LL_view, params);

                } else if (action == MotionEvent.ACTION_UP) {
                    // 如果手指离开屏幕时，xDownInScreen和xInScreen相等(或在误差允许范围内)，
                    // 且yDownInScreen和yInScreen相等(或在误差允许范围内)，则视为触发了单击事件。
                    if (Math.abs(xInScreen - xDownInScreen) <= error
                            && Math.abs(yInScreen - yDownInScreen) <= error) {
                        /**
                         * 执行点击事件
                         */
                        doClick();
                    }
                }

                return true;
            }
        });
    }


    private void removeFloatView() {
        if (simple_LL_view != null) {
            windowManager.removeView(simple_LL_view);
            simple_LL_view = null;
        }

    }

    /**
     * 判断当前悬浮窗是否显示
     *
     * @return
     */
    private boolean isFloatWindowShowing() {
        return simple_LL_view != null;
    }

}
