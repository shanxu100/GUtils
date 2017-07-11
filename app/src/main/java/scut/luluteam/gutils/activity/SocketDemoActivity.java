package scut.luluteam.gutils.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.utils.ShowUtil;

public class SocketDemoActivity extends BaseActivity implements Runnable {
    private TextView ReceivedMsg_tv = null;
    private EditText Msg_et = null;
    private Button SendMsg_btn = null;
    //    private Button btn_login = null;
    private static final String HOST = "125.216.242.147";
    private static final int PORT = 9999;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String content = "";


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_demo);

        ReceivedMsg_tv = (TextView) findViewById(R.id.ReceivedMsg_tv);
        Msg_et = (EditText) findViewById(R.id.Msg_et);
//        btn_login = (Button) findViewById(R.id.Button01);
        SendMsg_btn = (Button) findViewById(R.id.SendMsg_btn);

        SendMsg_btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String msg = Msg_et.getText().toString();
                if (socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        out.println(msg);
                    }
                }
            }
        });
        new Thread(SocketDemoActivity.this).start();
    }


    @Override
    public void run() {

        try {
            socket = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);

            while (true) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((content = in.readLine()) != null) {
                            content += "\n";
                            Message msg=mHandler.obtainMessage();
                            msg.obj=content;
                            mHandler.sendMessage(msg);
                        } else {

                        }
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            ShowUtil.LogAndToast(mContext,"login exception" + ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ReceivedMsg_tv.setText(ReceivedMsg_tv.getText().toString() + msg.obj);
        }
    };
}
