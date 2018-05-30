package scut.luluteam.gutils.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.DisplayUtil;


/*
 * 使用示例

 final List<Boolean> results = new ArrayList<Boolean>();
 results.add(false);
 results.add(true);

 ////////////////////////////////////////////////

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
        switchDialog.showWithAnim();
*/


/**
 * <p>
 * Created by guan on 4/6/17.
 */

public class G_SwitchDialog extends Dialog {


    LinearLayout switch_ll;
    Button Accept_btn;
    TextView title_tv;

    final int switchNum;
    List<Switch> switches = new ArrayList<>();
    List<Boolean> checkedResult = new ArrayList<>();


    Context mContext;

    SwitchDialogCallback callback;


    /**
     * @param context
     * @param switchNames
     */
    public G_SwitchDialog(@NonNull Context context, @NonNull String[] switchNames) {
        super(context);
        this.mContext = context;
        this.switchNum = switchNames.length;
//        switches = new ArrayList<>(switchNum);
//        checkedResult = new ArrayList<>(switchNum);

        //设置自定义Dialog
        setCustomDialog(switchNames);

        //设置大小
        getWindow().setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    /**
     * @param callback
     */
    public void setCallback(SwitchDialogCallback callback) {
        this.callback = callback;
    }

    /**
     * @param states
     */
    public void setCheckedState(List<Boolean> states) {
        if (states.size() != switchNum) {
            //所指定的state的数量与switch的数量不符
            return;
        }
        for (int i = 0; i < switchNum; i++) {
            checkedResult.set(i, states.get(i));
            switches.get(i).setChecked(states.get(i));
        }
    }


    /**
     * @param title
     */
    public void setTitle(String title) {
        title_tv.setText(title);
    }


    public interface SwitchDialogCallback {
        void onClickAudioSwitch(List<Boolean> checkedResults);
    }

    //============================
    private void setCustomDialog(String[] switchNames) {

        if (switchNum == 0) {
            return;
        }
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_switchcontroller_g, null);
        switch_ll = (LinearLayout) mView.findViewById(R.id.switch_ll);
        title_tv = (TextView) mView.findViewById(R.id.title_tv);
        Accept_btn = (Button) mView.findViewById(R.id.AcceptAudio_btn);

        //创建switch的监听器
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int tag = Integer.valueOf(buttonView.getTag().toString());
                checkedResult.set(tag, isChecked);
            }
        };


        //创建添加分割线
        View top_line_view = new View(mContext);
        top_line_view.setBackgroundColor(mContext.getResources().getColor(R.color.lightgrey));
        switch_ll.addView(top_line_view);
        ViewGroup.LayoutParams line_view_params = top_line_view.getLayoutParams();
        line_view_params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        line_view_params.height = 3;
        top_line_view.setLayoutParams(line_view_params);

        for (int i = 0; i < switchNum; i++) {
            //创建switch控件
            Switch sw = new Switch(mContext);
            switches.add(sw);
            switch_ll.addView(sw);
            checkedResult.add(false);
            sw.setChecked(false);
            sw.setPadding(50, 10, 20, 10);
            sw.setVisibility(View.VISIBLE);
            sw.setText(switchNames[i]);
            sw.setTextSize(18);
            sw.setTag(i);
            sw.setOnCheckedChangeListener(listener);
            ViewGroup.LayoutParams sw_params = sw.getLayoutParams();
            sw_params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            sw_params.height = 135;
            sw.setLayoutParams(sw_params);

            //创建添加分割线
            View line_view = new View(mContext);
            line_view.setBackgroundColor(mContext.getResources().getColor(R.color.lightgrey));
            switch_ll.addView(line_view);
            ViewGroup.LayoutParams view_params = line_view.getLayoutParams();
            view_params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            view_params.height = 3;
            line_view.setLayoutParams(view_params);
        }


//        //switch的listener
//
//        new Switch(mContext).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });


        Accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickAudioSwitch(checkedResult);
                }
            }
        });


        //ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setContentView(mView);
    }


}
