package scut.luluteam.gutils.utils.headsmsg;

import android.app.NotificationManager;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import scut.luluteam.gutils.app.App;


/**
 * Created by guan on 6/23/17.
 */

public class HeadMsgManager {

    private static HeadMsgManager manager;
    private Context mContext;

    private WindowManager windowManager;
    private NotificationManager notificationManager = null;
    private HeadMsgView2 msgView;
    public static WindowManager.LayoutParams winParams;

    /**
     * 改为单例模式
     *
     * @return
     */
    public static HeadMsgManager getInstant() {
        if (manager == null) {
            synchronized (HeadMsgManager.class) {
                if (manager == null) {
                    manager = new HeadMsgManager();
                }
            }
        }
        return manager;
    }


    /**
     * 构造函数
     */
    private HeadMsgManager() {
        windowManager = (WindowManager) App.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        notificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        winParams = new WindowManager.LayoutParams();
    }

    /**
     * 显示
     */
    public void show(Context mContext, final HeadsMsg headsMsg) {

        this.mContext = mContext;
        msgView = new HeadMsgView2(mContext);
        msgView.setUpWithHeadMsg(headsMsg);

        /**
         * 显示头顶悬浮窗
         */
        final WindowManager.LayoutParams params = winParams;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = -3;
        params.gravity = Gravity.CENTER | Gravity.TOP;
        params.x = msgView.originalLeft;
        params.y = 0;
        params.alpha = 1f;

        windowManager.addView(msgView, params);
        msgView.showWithAnim();

        /**
         * 在状态栏显示通知
         */
        if (headsMsg.getNotification() != null) {
            notificationManager.notify(headsMsg.getNotificationId(), headsMsg.getNotification());
        }


    }


    /**
     * 移除
     */
    public void dismiss() {
        if (msgView.getParent() != null) {
            windowManager.removeView(msgView);
        }

    }


}
