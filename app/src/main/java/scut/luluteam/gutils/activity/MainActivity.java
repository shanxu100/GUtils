package scut.luluteam.gutils.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kyleduo.switchbutton.SwitchButton;

import scut.luluteam.gutils.activity.tab.OnlyTabActivity;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.model.EventBusMessage;

import scut.luluteam.gutils.service.DownUploadService;
import scut.luluteam.gutils.service.UpdateService;
import scut.luluteam.gutils.service.floatwindow.FloatWinService;
import scut.luluteam.gutils.service.mqtt.MQTTService;
import scut.luluteam.gutils.service.mqtt.MessageSender;
import scut.luluteam.gutils.service.socket.SocketService;
import scut.luluteam.gutils.service.websocket.WebSocketService;
import scut.luluteam.gutils.service.websocket.WsService;
import scut.luluteam.gutils.utils.MqttClientManager;
import scut.luluteam.gutils.utils.ShowUtil;
import scut.luluteam.gutils.utils.UriUtil;
import scut.luluteam.gutils.utils.headmsg.HeadMsgManager;
import scut.luluteam.gutils.utils.headmsg.HeadsMsg;
import scut.luluteam.gutils.utils.http.AsyncHttpURLConnection;
import scut.luluteam.gutils.utils.lock_screen.DeviceManager;
import scut.luluteam.gutils.utils.screen_shot.ScreenShotActivity;
import scut.luluteam.gutils.view.G_AlertDialog;
import scut.luluteam.gutils.view.G_InputDialog;
import scut.luluteam.gutils.view.LoadingDialog;
import scut.luluteam.gutils.view.G_RadioGroupDialog;
import scut.luluteam.gutils.view.G_SwitchDialog;
import scut.luluteam.gutils.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    Context mContext;
    String TAG = "MainActivity";
    //private NetworkStateReceiver networkStateReceiver;

    final List<Boolean> results = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 必须放在setContentView()之前，must be called before adding content
         *
         */
        getCustomBuilder().setNoTitle(false)
                .setSteepStatusBar(false)
                .allowScreenRoate(true);
        //.build();

        setContentView(R.layout.activity_main);
        mContext = this;


        Button test1_btn = (Button) this.findViewById(R.id.test1_btn);
        Button test2_btn = (Button) this.findViewById(R.id.test2_btn);
        Button test3_btn = (Button) this.findViewById(R.id.test3_btn);
        Button test4_btn = (Button) this.findViewById(R.id.test4_btn);
        Button test5_btn = (Button) this.findViewById(R.id.test5_btn);
        final Button show_btn = (Button) this.findViewById(R.id.show_btn);

        final SwitchButton switchButton = (SwitchButton) this.findViewById(R.id.switchButton);

        results.add(false);
        results.add(true);

//        ViewGroup
//WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY

        test1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testActivity();
            }
        });

        test2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testMqttPublish_true();
            }
        });

        test3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSendWSMsg();
            }
        });

        test4_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testUpdateService();
            }
        });

        test5_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSendSocketMsg();
            }
        });

        show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchButton.setChecked(true);
            }
        });

//        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                switchButton.setChecked(!isChecked);
//                System.out.println("balabal");
//            }
//        });

        switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println(event.getAction());
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    System.out.println("onTouch");
                }
                //一定要true
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        //注册监听网络的广播
//        networkStateReceiver=new NetworkStateReceiver();
//        IntentFilter networkFilter=new IntentFilter();
//        networkFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        networkFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
//        registerReceiver(networkStateReceiver,networkFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        //注销广播
        //unregisterReceiver(networkStateReceiver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

//===========================================================================================

    private void testSwitchDialog() {
        final G_SwitchDialog switchDialog = new G_SwitchDialog(mContext,
                new String[]{"aaa", "bbb"});
        switchDialog.setTitle("This is my Title");
        switchDialog.setCheckedState(results);
        switchDialog.setCallback(new G_SwitchDialog.SwitchDialogCallback() {
            @Override
            public void onClickAudioSwitch(List<Boolean> checkedResults) {
                for (int i = 0; i < checkedResults.size(); i++) {
                    Log.e(TAG, checkedResults.get(i) + "");
                    //保存新的结果
                    results.clear();
                    results.addAll(checkedResults);
                }
                switchDialog.dismiss();
            }
        });
        switchDialog.show();
    }

    private void testRadioGroupDialog() {
        final G_RadioGroupDialog radioGroupDialog = new G_RadioGroupDialog(mContext,
                new String[]{"123", "hahaha", "hahahahaha"});
        radioGroupDialog.setTitle("This is my Title");

        radioGroupDialog.setDialogCallback(new G_RadioGroupDialog.DialogCallback() {

            @Override
            public void onClickRadioButton(String checkedString) {
                Log.e(TAG, checkedString);
                radioGroupDialog.dismiss();
            }
        });

        radioGroupDialog.show();

    }

    private void testAlertDialog() {
        G_AlertDialog alertDialog = new G_AlertDialog(this);
//        alertDialog.ty
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setMeesage("nihaodgk");
        alertDialog.show();
    }

    private void testInputDialog() {
        final G_InputDialog inputDialog = new G_InputDialog(mContext);
        inputDialog.setCallback(new G_InputDialog.InputDialogCallback() {
            @Override
            public void onPositive(String s) {
                ShowUtil.UIToast(mContext, s);
                inputDialog.dismiss();
            }

            @Override
            public void onNegative(String message) {
                inputDialog.dismiss();
            }
        });
        inputDialog.show();
    }

    private void testLoadingDialog() {
        LoadingDialog loadingDialog = LoadingDialog.newInstance(mContext, "Loading");
        loadingDialog.show();
    }

    private void testDownUploadService() {
        String url = "https://sz-ctfs.ftn.qq.com/ftn_handler/b28427154f58175f6f5e64d950052b7e02aee7843870a27bc5c34a29e0b33582e3669079a44900890468318bf71cee9cb1d38e32db7eabc4c1d0fb0dfd42b01c/?fname=_M9A5457.JPG&k=253330619d90fe9a0bbf0e4116340b4a0504555709020f03490b565153190d5550041d0002500c485606565151075d52570755043038393a290a715404010e4b2e6377610d&fr=00&&txf_fid=c7bfe6b5e531c8199bb30dd1563b39d22d735217&xffz=3693395";
        String localName = "balabala";
        String localPath = Environment.getExternalStorageDirectory().getPath();

        Intent intent = new Intent(mContext, DownUploadService.class);
        intent.putExtra("operation", DownUploadService.ServiceOperation.DOWNLOAD);
        DownUploadService.DownloadParams downloadParams = new DownUploadService.DownloadParams(
                localName,
                url,
                localPath,
                null);
        intent.putExtra("DownloadParams", downloadParams);
        startService(intent);
    }

    private void testFloatWindow() {
        Intent intent = new Intent(mContext, FloatWinService.class);

        startService(intent);

    }

    private void testheadMsg() {
        HeadsMsg headsMsg = new HeadsMsg.HeadMsgBuilder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("HeadMsg Title")
                .setContentText("这个是自定义通知")
                .setSticky(true)
                .buildHeadMsg();
        View view = getLayoutInflater().inflate(R.layout.view_headmsg_custome, null);
        headsMsg.setCustomView(view);

        HeadMsgManager.getInstant().show(this, headsMsg);

    }

    private void testStopFloatWindow() {
        Intent intent = new Intent(mContext, FloatWinService.class);
        stopService(intent);
    }

    private void testStartMqttService() {
//        Intent intent = new Intent(mContext, MQTTService.class);
//        startService(intent);
        MqttClientManager.getInstance().start(getApplicationContext());
    }

    private void testMqttPublish_true() {
        MessageSender.sendActionLockScreen("TOPIC_TEST", true);
        //MessageSender.sendString("TOPIC_TEST","balabala");
    }

    private void testMqttPublish_false() {
        MessageSender.sendActionLockScreen("TOPIC_TEST", false);
    }

    private void testStopMqttService() {
        Intent intent = new Intent(mContext, MQTTService.class);
        stopService(intent);
    }

    private void testSendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "choose file"), 9000);
    }

    /**
     * 请求截屏权限
     */
    private void testScreenShot() {
        Intent requsetScreenShotIntent = new Intent(mContext, ScreenShotActivity.class);
        startActivity(requsetScreenShotIntent);
    }

    private void testApp() {
        /**
         * http://blog.csdn.net/cbbbc/article/details/60148864
         */
        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            //intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    public void testPermission() {
        int hasPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ShowUtil.UIToast("权限未赋予");
            /**
             * 弹出在其他应用上显示的设置页面
             */
//            if(Build.VERSION.SDK_INT >= 23) {
//                Intent intent = new Intent();
//                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                intent.setData(Uri.fromParts("package", getPackageName(), null));
//                startActivityForResult(intent,123);
//            }
        }
    }

    private void testFloatPermission() {
        //Intent intent = new Intent(this, FloatWinPermissionActivity.class);
        //startActivity(intent);
    }


    private void testIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);

    }


    private void testActiveAdmin() {
        DeviceManager.getInstance().activeAdmin(mContext);
    }

    private void testActivity() {
        //GLSurfaceView.EGLWindowSurfaceFactory factory=new fa
        Intent intent = new Intent(this, OnlyTabActivity.class);
        startActivity(intent);
    }

    private void testIOSocket() {
        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
    }

    private void testSendSocketMsg() {
        SocketService.SocketManager.send("testSend");
    }

    private void testWebSocket() {
        Intent intent = new Intent(this, WsService.class);
        startService(intent);
    }

    private void testSendWSMsg() {
        WebSocketService.sendMsg("test WS Msg");
    }

    private void testHttp() {
        String url = "http://www.baidu.com";
        AsyncHttpURLConnection connection = new AsyncHttpURLConnection("POST", url, "", new AsyncHttpURLConnection.AsyncHttpEvents() {
            @Override
            public void onHttpError(String errorMessage) {
                System.out.println("ERROR:" + errorMessage);
            }

            @Override
            public void onHttpComplete(String response) {
                System.out.println(response);
            }
        });
        connection.send();
    }

    private void testWebView() {
        Intent intent = new Intent(this, WebViewActivity.class);
        startActivity(intent);
    }

    private void testUpdateService() {
        Intent intent = new Intent(this, UpdateService.class);
        startService(intent);
    }


    /**
     * 触发屏幕点击事件
     *
     * @param x 点击位置的x值
     * @param y 点击位置的y值
     */
    public void setMouseClick(int x, int y) {
        MotionEvent evenDown = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
        dispatchTouchEvent(evenDown);
        MotionEvent eventUp = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
        dispatchTouchEvent(eventUp);
        evenDown.recycle();
        eventUp.recycle();
    }


    //============================================================================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 9000) {
            Uri uri = data.getData();
            String filePath = UriUtil.getFilePathFromUri(getApplicationContext(), uri);
            try {
                //LoadingDialog.getInstance(mContext, "正在发送文件……").showWithAnim();

                //MQTTService.MQTTManager.sendFile("TOPIC_TEST", filePath, mContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 123) {
            //Log.e(TAG,data.getData().toString());
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //PermissionUtil.resultCallback(requestCode,permissions,grantResults,mContext);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(EventBusMessage eventBusMessage) {
        if (eventBusMessage.type == EventBusMessage.MessageType.Loading) {
            if (eventBusMessage.isDone) {
                //LoadingDialog.getInstance(mContext, "正在处理，请稍后……").dismiss();
            } else {
                //LoadingDialog.getInstance(mContext, "正在处理，请稍后……").showWithAnim();
            }

        }
    }


}