package scut.luluteam.gutils.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.DisplayUtil;


/**
 * Created by guan on 6/8/17.
 */

public class G_InputDialog extends Dialog {

    EditText input_et;
    Button accept_btn;
    Button refuse_btn;

    InputDialogCallback callback;

    public G_InputDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Dim);
        setCustumDialog();
        getWindow().setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setCallback(InputDialogCallback callback) {
        this.callback = callback;
    }

    private void setCustumDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_g, null);
        input_et = (EditText) mView.findViewById(R.id.input_et);
        accept_btn = (Button) mView.findViewById(R.id.accept_btn);
        refuse_btn = (Button) mView.findViewById(R.id.refuse_btn);
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onPositive(input_et.getText().toString().trim());
            }
        });

        refuse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onNegative("");
            }
        });
        setCanceledOnTouchOutside(false);
        super.setContentView(mView);
    }

    public interface InputDialogCallback {
        void onPositive(String message);

        void onNegative(String message);
    }
}
