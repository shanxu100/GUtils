package scut.luluteam.gutils.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by guan on 6/24/17.
 */

public class CountTime {

    //private static CountTimeUtil countTime;
    private Timer timer;
    private int id;
    private int second;
    private CountTimeEvent event;

    public static CountTime newInstance(int second, CountTimeEvent event) {
        return new CountTime(second, event);
    }

    private CountTime(int second, CountTimeEvent event) {
        this.second = second;
        this.event = event;
        System.out.println("Time remians " + second + " s");
    }

    public void start() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (second <= 0) {
                    System.out.println("Time is out!");
                    stop();
                    event.timeOut();
                }
                second--;
            }
        }, 0, 1000);

    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 负责通知倒计时结束、中断的回调
     */
    public interface CountTimeEvent {
        void timeOut();
    }


}
