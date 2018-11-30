package scut.luluteam.gutils.service.floatwindow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.ShowUtil;
import scut.luluteam.gutils.utils.screen_shot.ScreenShot;

/**
 * Created by guan on 5/31/17.
 */

public class FloatWindowView {

    public static class Big extends LinearLayout {

        private Button close_btn;
        private Button click_btn;
        private Button second_btn;

        private Context appContext;

        private String TAG = "FloatWindowBig";


        public Big(final Context appContext) {
            super(appContext);
            this.appContext = appContext;

            setCustomeFloatWindow();


        }

        private void setCustomeFloatWindow() {

            LayoutInflater.from(appContext).inflate(R.layout.view_floatwindow_big, this);
            View view = findViewById(R.id.big_window_layout);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);

            close_btn = (Button) findViewById(R.id.close_btn);
            click_btn = (Button) findViewById(R.id.click_btn);
            second_btn = (Button) findViewById(R.id.second_btn);

            close_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                    FloatWindowManager.removeBigWindow(appContext);
                    FloatWindowManager.removeSmallWindow(appContext);
                    Intent intent = new Intent(getContext(), FloatWindowService.class);
                    appContext.stopService(intent);

                }
            });


            click_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ClickUtil.setMouseClick(,750,610);
                    //((App)appContext).startScreenShot();
                    ScreenShot screenShot = new ScreenShot();
                    screenShot.start();
                    FloatWindowManager.removeBigWindow(appContext);
                }
            });

            second_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowUtil.Toast("balabala");
                }
            });
        }

        /**
         * 重写onTouchEvent事件，实现触摸button以外的区域，自动关闭该窗口
         * <p>
         * 导致该窗口中，只能有Button这样不调用onTouchEvent的控件
         *
         * @param event
         * @return
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                    Log.d(TAG, "onTouchEvent ACTION_DOWN");

                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "onTouchEvent ACTION_MOVE");

                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouchEvent ACTION_UP");
                    // 在触摸动作离开屏幕的时候，移除大悬浮窗，创建小悬浮窗
                    FloatWindowManager.removeBigWindow(appContext);
                    FloatWindowManager.createSmallWindow(appContext);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    //=======================================================================================

    public static class Small extends LinearLayout {


        /**
         * 记录小悬浮窗的宽度
         */
        public static int viewWidth;

        /**
         * 记录小悬浮窗的高度
         */
        public static int viewHeight;

        /**
         * 记录系统状态栏的高度
         */
        private static int statusBarHeight;

        /**
         * 用于更新小悬浮窗的位置
         */
        private WindowManager windowManager;

        /**
         * 小悬浮窗的参数
         */
        private WindowManager.LayoutParams mParams;

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
        private float error = 5;

        private Context appContext;


        public Small(Context context) {
            super(context);
            appContext = context;
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            setCustomeFloatWindow();
        }

        public void setCustomeFloatWindow() {
            LayoutInflater.from(appContext).inflate(R.layout.view_floatwindow_small, this);
            View view = findViewById(R.id.small_window_layout);
            viewWidth = view.getLayoutParams().width;
            viewHeight = view.getLayoutParams().height;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                    xInView = event.getX();
                    yInView = event.getY();
                    xDownInScreen = event.getRawX();
                    yDownInScreen = event.getRawY() - getStatusBarHeight();
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY() - getStatusBarHeight();
                    break;
                case MotionEvent.ACTION_MOVE:
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY() - getStatusBarHeight();
                    // 手指移动的时候更新小悬浮窗的位置
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP:
                    // 如果手指离开屏幕时，xDownInScreen和xInScreen相等(或在误差允许范围内)，
                    // 且yDownInScreen和yInScreen相等(或在误差允许范围内)，则视为触发了单击事件。
                    if (Math.abs(xInScreen - xDownInScreen) <= error
                            && Math.abs(yInScreen - yDownInScreen) <= error) {
                        openBigWindow();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        /**
         * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
         *
         * @param params 小悬浮窗的参数
         */
        public void setParams(WindowManager.LayoutParams params) {
            mParams = params;
        }

        /**
         * 更新小悬浮窗在屏幕中的位置。
         */
        private void updateViewPosition() {
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            windowManager.updateViewLayout(this, mParams);
        }

        /**
         * 打开大悬浮窗，同时关闭小悬浮窗。
         */
        private void openBigWindow() {
            FloatWindowManager.createBigWindow(getContext());
            FloatWindowManager.removeSmallWindow(getContext());
        }

        /**
         * 用于获取状态栏的高度。
         *
         * @return 返回状态栏高度的像素值。
         */
        private int getStatusBarHeight() {
            if (statusBarHeight == 0) {
                try {
                    Class<?> c = Class.forName("com.android.internal.R$dimen");
                    Object o = c.newInstance();
                    Field field = c.getField("status_bar_height");
                    int x = (Integer) field.get(o);
                    statusBarHeight = getResources().getDimensionPixelSize(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return statusBarHeight;
        }

    }
}
