package scut.luluteam.gutils.service.floatwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


import java.util.Timer;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.App;

/**
 * Created by guan on 6/22/17.
 */

public class FloatWinForMessage {

    String message;
    WindowManager windowManager;
    View floatWinView_view;
    TextView floatMessage_tv;
    Timer timer;

    private FloatWinForMessage(String message) {
        this.message = message;
        createFloatWindow();
    }

    public static void newInstance(String message) {
        new FloatWinForMessage(message).show();
    }

    private void createFloatWindow() {
        windowManager = (WindowManager) App.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        floatWinView_view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.view_floatwindow_alert, null);
        floatMessage_tv = (TextView) floatWinView_view.findViewById(R.id.floatMessage_tv);
    }

    private void show() {

    }

}
