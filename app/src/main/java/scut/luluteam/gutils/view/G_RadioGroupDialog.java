package scut.luluteam.gutils.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.DisplayUtil;


/*
 * 使用示例

    final G_RadioGroupDialog radioGroupDialog=new G_RadioGroupDialog(mContext,
        new String[]{"123","hahaha","hahahahaha"});
    radioGroupDialog.setTitle("This is my Title");
    adioGroupDialog.setDialogCallback(new G_RadioGroupDialog.DialogCallback(){
    @Override
    public void onClickRadioButton(String checkedString){
            Log.e(TAG,checkedString);
            radioGroupDialog.dismiss();
            }
        });

    radioGroupDialog.showWithAnim();
*/

/**
 * Created by guan on 3/13/17.
 */

public class G_RadioGroupDialog extends Dialog {

    RadioGroup radioGroup;
    TextView title_tv;
    Map<Integer, String> radioBtn_IdText_map = new HashMap<>();

    DialogCallback callback;
    int radioBtnNums;
    int bestWidth;

    final int textSize = 20;

    Context mContext;

    /**
     * @param context
     * @param radioText
     */
    public G_RadioGroupDialog(Context context, String[] radioText) {
        super(context, R.style.Dialog_Dim);

        this.mContext = context;
        radioBtnNums = radioText.length;

        setBestWidth(radioText);
        setCustomeDialog(radioText);

        getWindow().setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * @param callback
     */
    public void setDialogCallback(DialogCallback callback) {
        this.callback = callback;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        title_tv.setText(title);
    }


    public interface DialogCallback {

        void onClickRadioButton(String checkedString);

    }

    //=====================================

    private void setCustomeDialog(String[] radioText) {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_radiogroup_g, null);
        radioGroup = (RadioGroup) mView.findViewById(R.id.selectserver_radiogrp);
        title_tv = (TextView) mView.findViewById(R.id.title_tv);

        for (int i = 0; i < radioBtnNums; i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioGroup.addView(radioButton);
            ViewGroup.LayoutParams rb_params = radioButton.getLayoutParams();
            rb_params.width = bestWidth;//对于宽度的设置，以后可以优化
            rb_params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            radioButton.setLayoutParams(rb_params);
            radioButton.setTextSize(textSize);
            radioButton.setText(radioText[i]);
            radioButton.setPadding(20, 20, 20, 20);

            radioBtn_IdText_map.put(radioButton.getId(), radioText[i]);

        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (callback != null) {
                    callback.onClickRadioButton(radioBtn_IdText_map.get(checkedId));
                }
                //G_RadioGroupDialog.this.dismiss();
            }
        });

        super.setContentView(mView);

    }

    private void setBestWidth(String[] radioText) {
        int length = 0;

        for (String s : radioText) {
            length = s.length() > length ? s.length() : length;
        }
        bestWidth = length * (textSize + 20) + 100;

    }
}
