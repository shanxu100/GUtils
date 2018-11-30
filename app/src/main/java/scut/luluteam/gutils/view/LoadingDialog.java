package scut.luluteam.gutils.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.utils.ClickUtil;
import scut.luluteam.gutils.utils.ShowUtil;


/**
 * Created by guan on 5/19/17.
 */

public class LoadingDialog extends Dialog {
    private TextView loading_tv;
    private Context mContext;
    private CancelLoadingCallback callback;


    public static LoadingDialog newInstance(@NonNull Context mContext, String loadingText) {
        LoadingDialog loadingDialog = new LoadingDialog(mContext);
        loadingDialog.setLoadingText(loadingText);
        return loadingDialog;
    }

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Dialog_Dim);
        mContext = context;
        setCustomeDialog();
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setCustomeDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading_g, null);
        loading_tv = (TextView) view.findViewById(R.id.loading_tv);
        this.setCanceledOnTouchOutside(false);
        super.setContentView(view);
    }

    public void setLoadingText(String loadingText) {
        loading_tv.setText(loadingText);
    }

    public void setCancelLoadingCallback(CancelLoadingCallback callback) {
        this.callback = callback;
    }


    /**
     * 双击取消加载
     */
    public interface CancelLoadingCallback {
        void onCancelLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //双击返回键取消加载
            if (ClickUtil.isFastDoubleClick()) {
                if (callback != null) {
                    callback.onCancelLoading();
                    this.dismiss();
                }
                ShowUtil.Toast(mContext, "正在加载，请勿取消……");
            } else {
                ShowUtil.Toast(mContext, "再按一次取消加载……");
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
