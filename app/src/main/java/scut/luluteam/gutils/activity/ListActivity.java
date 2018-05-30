package scut.luluteam.gutils.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import scut.luluteam.gutils.R;

public class ListActivity extends AppCompatActivity {
    private ListView test_lv;
    private Button refresh_btn;
    private List<String> list;
    private MyAdapter myAdapter;

    private HandlerThread handlerThread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        test_lv = (ListView) findViewById(R.id.test_lv);
        refresh_btn = (Button) findViewById(R.id.refresh_btn);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(System.currentTimeMillis() + "");
        }
        myAdapter = new MyAdapter(list);
        test_lv.setAdapter(myAdapter);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("ThreadName==="+Thread.currentThread().getName());
//                        doRefreshList();
//                    }
//                });
                doRefreshList();
            }
        });

        handlerThread = new HandlerThread("listTest");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

    }

    private void doRefreshList() {
        list.clear();
        for (int i = 0; i < 20; i++) {
            list.add(System.currentTimeMillis() + "");
        }
        myAdapter.notifyDataSetChanged();
    }


    private static class MyAdapter extends BaseAdapter {

        private List<String> list;
        private String TAG = "MyAdapter";



        public MyAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_tab_item, null);
                holder.textView = (TextView) convertView.findViewById(R.id.tabItemTitle_tv);
                convertView.setTag(holder);
                Log.e(TAG, "getView===" + position + "convertView==null");

            } else {
                holder = (ViewHolder) convertView.getTag();
                Log.e(TAG, "getView===" + position + "convertView!=null");

            }
            holder.textView.setText(list.get(position));
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView textView;
    }
}
