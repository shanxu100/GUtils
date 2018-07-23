package scut.luluteam.gutils.activity.test;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.model.UserResult;
import scut.luluteam.gutils.utils.http.retrofit.RetrofitUtil;

public class FirstActivity extends AppCompatActivity {

    Button first2_btn;
    TextView taskIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        try {
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskIdView = (TextView) findViewById(R.id.task_tv);
        taskIdView.setText("current task id: " + this.getTaskId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy===="+FirstActivity.class.getSimpleName());
    }

    private void init() throws InterruptedException {
        first2_btn = (Button) findViewById(R.id.first2_btn);
        first2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                testRxAndRetrofit();
//                Intent intent = new Intent(App.getAppContext(), SecondActivity.class);
//                startActivity(intent);
//                startService(new Intent(mContext,TestIntentService.class));

            }
        });

        System.out.println("主线程：" + Thread.currentThread().getName());


//        Looper.prepare();
//        Looper.loop();


//        HandlerThread handlerThread = new HandlerThread("testHandThread");
//        handlerThread.start();
//        @SuppressLint("HandlerLeak")
//        Handler handler = new Handler(handlerThread.getLooper()) {
//
//            @Override
//            public void handleMessage(Message msg) {
//                System.out.println("handler 运行耗时任务：" + Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        handler.sendEmptyMessage(0);
//        task.executeOnExecutor();
//        TestIntentService service=new TestIntentService("GuanService");
//        startService(new Intent(mContext, TestIntentService.class));
//        Thread.sleep(4000);
//        startService(new Intent(mContext,TestIntentService.class));


    }






    public static class TestIntentService extends IntentService {
        public String TAG = "";

        @Override
        public void onCreate() {
            super.onCreate();
            Log.e("testIntentService", "onCreate");
            TAG = "onCreate currentTimeMillis=" + System.currentTimeMillis();
        }

        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         */
        public TestIntentService() {
            super("TestIntentService");
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            try {
                Log.e("testIntentService", "TAG变量==" + TAG + "  ThreadName=" + Thread.currentThread().getName());
                Log.e("testIntentService", "执行到了onHandleIntent,开始sleeping 2000");
                Thread.sleep(2000);
                Log.e("testIntentService", "结束sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Log.e("testIntentService", "onDestroy===");

        }
    }

}
