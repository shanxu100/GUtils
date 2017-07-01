package scut.luluteam.gutils.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.DisplayUtil;


/**
 * 未完待续……
 */
public class G_AlertDialog extends Dialog {
    private Button positiveButton, negativeButton;
    private TextView message;
    private YesOrNoDialogCallback callback;

    public enum ClickedButton {POSITIVE, NEGATIVE}

    /**
     * @param context
     */
    public G_AlertDialog(Context context) {
        super(context, R.style.Dialog_Dim);
        setCustomDialog();
        getWindow().setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        /**
         * 重要：保证该AlertDialog能通过ApplicationContext弹出
         */
        //getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    /**
     * @param callback
     */
    public void setCallback(YesOrNoDialogCallback callback) {
        this.callback = callback;
    }

    public void setMeesage(String mes) {
        message.setText(mes);
    }


    public interface YesOrNoDialogCallback {
        void onClickButton(ClickedButton button, String message);
    }

    //====================================
    private void setCustomDialog() {

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alert_g, null);
        message = (TextView) mView.findViewById(R.id.title);
        positiveButton = (Button) mView.findViewById(R.id.accept_btn);
        negativeButton = (Button) mView.findViewById(R.id.refuse_btn);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickButton(ClickedButton.POSITIVE, "");
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onClickButton(ClickedButton.NEGATIVE, "");
                }
            }
        });

        //ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setContentView(mView);
    }


}
