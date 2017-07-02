package scut.luluteam.gutils.utils.headsmsg;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责创建并保存关于HeadMsg的
 * 所有数据信息
 * <p>
 * Created by guan on 6/23/17.
 */

public class HeadsMsg {

    public boolean flag_hasDismissed=false;

    private Context context;
    /**
     * 出现时间  单位是 second
     */
    private long duration = 3;
    /**
     *
     */
    private Notification notification;
    private HeadMsgBuilder headMsgBuilder;


    private boolean isSticky = false;
    /**
     * 是否声音等提示
     */
    private boolean activateStatusBar = true;

    /**
     * 间隔时间
     */
    private long interval = 600;
    private int notificationId = 10086;
    private List<NotificationCompat.Action> actions;
    private CharSequence titleStr;
    private CharSequence msgStr;
    private int smallIcon;
    private View customView;
    private boolean isExpand;

    private HeadsMsg(Context context) {
        this.context = context;
    }


    public static class HeadMsgBuilder {

        private List<NotificationCompat.Action> actions;

        private HeadsMsg headsMsg;
        private Notification.Builder notificationBuilder;

        private CharSequence contentTitle;
        private CharSequence contentText;
        private int smallIcon;
        private boolean isSticky;

        private Context mContext;


        public HeadMsgBuilder(Context context) {
            actions = new ArrayList<>();
            this.mContext = context;
        }

        /**
         * 显示全部界面
         *
         * @param isExpand
         */
        public HeadMsgBuilder setUsesChronometer(boolean isExpand) {
            //headsMsg.setExpand(isExpand);
            return this;
        }

        /**
         * Set the first line of text in the platform notification template.
         */
        public HeadMsgBuilder setContentTitle(CharSequence title) {
            this.contentTitle = title;
            return this;
        }

        /**
         * Set the second line of text in the platform notification template.
         */
        public HeadMsgBuilder setContentText(CharSequence text) {
            this.contentText = text;
            return this;
        }

        public HeadMsgBuilder setSmallIcon(int icon) {
            this.smallIcon = icon;
            return this;
        }

        public HeadMsgBuilder setSticky(boolean isSticky) {
            this.isSticky = isSticky;
            return this;
        }


//        @Override
//        public NotificationBuilder addAction(int icon, CharSequence title, PendingIntent intent) {
//            NotificationCompat.Action action = new NotificationCompat.Action(icon, title, intent);
//            actions.add(action);
//            super.addAction(icon, title, intent);
//            return this;
//        }


        /**
         * Build
         *
         * @return
         */
        public HeadsMsg buildHeadMsg() {

            notificationBuilder = new Notification.Builder(mContext);
            headsMsg = new HeadsMsg(mContext);

            headsMsg.setTitleStr(contentTitle);
            headsMsg.setMsgStr(contentText);
            headsMsg.setSmallIcon(smallIcon);
            notificationBuilder.setContentTitle(contentTitle);
            notificationBuilder.setContentText(contentText);
            notificationBuilder.setSmallIcon(smallIcon);

            headsMsg.setSticky(isSticky);
            headsMsg.setNotification(notificationBuilder.build());
            headsMsg.setActions(actions);
            headsMsg.setHeadMsgBuilder(this);
            return headsMsg;
        }

//        private Notification silencerNotification() {
//            super.setSmallIcon(headsMsg.getSmallIcon());
//            //设置声音、呼吸灯等
//            setDefaults(Notification.DEFAULT_ALL);
//            return this.build();
//        }

//        public HeadMsgBuilder addAction(NotificationCompat.Action action) {
//            actions.add(action);
//            notificationBuilder.addAction(action);
//            return this;
//        }

    }//end headMsgBuilder

    //getter and setter

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public HeadMsgBuilder getHeadMsgBuilder() {
        return headMsgBuilder;
    }

    public void setHeadMsgBuilder(HeadMsgBuilder headMsgBuilder) {
        this.headMsgBuilder = headMsgBuilder;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public boolean isActivateStatusBar() {
        return activateStatusBar;
    }

    public void setActivateStatusBar(boolean activateStatusBar) {
        this.activateStatusBar = activateStatusBar;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public List<NotificationCompat.Action> getActions() {
        return actions;
    }

    public void setActions(List<NotificationCompat.Action> actions) {
        this.actions = actions;
    }

    public CharSequence getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(CharSequence titleStr) {
        this.titleStr = titleStr;
    }

    public CharSequence getMsgStr() {
        return msgStr;
    }

    public void setMsgStr(CharSequence msgStr) {
        this.msgStr = msgStr;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public View getCustomView() {
        return customView;
    }

    public void setCustomView(View customView) {
        this.customView = customView;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
