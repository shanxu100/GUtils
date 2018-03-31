package scut.luluteam.gutils.activity.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.app.BaseActivity;

public class SecondActivity extends BaseActivity {
    Button second1_btn;
    Button second3_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        TextView taskIdView = (TextView) findViewById(R.id.task_tv);
        taskIdView.setText("current task id: " + this.getTaskId());
    }


    private void init()
    {
        second1_btn=(Button)findViewById(R.id.second1_btn);
        second1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(App.getAppContext(),FirstActivity.class);
                startActivity(intent);
            }
        });
        second3_btn=(Button)findViewById(R.id.second3_btn);
        second3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(App.getAppContext(),ThirdActivity.class);
                startActivity(intent);
            }
        });


    }
}
