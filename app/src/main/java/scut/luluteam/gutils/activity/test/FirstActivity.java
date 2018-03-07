package scut.luluteam.gutils.activity.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.App;

public class FirstActivity extends AppCompatActivity {

    Button first2_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
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
        first2_btn=(Button)findViewById(R.id.first2_btn);
        first2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(App.getAppContext(),SecondActivity.class);
                startActivity(intent);
            }
        });

    }

}
