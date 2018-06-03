package scut.luluteam.gutils.activity.video.other;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

import scut.luluteam.gutils.activity.video.model.AccessTokenResult;
import scut.luluteam.gutils.utils.SharedPreferencesUtil;
import scut.luluteam.gutils.utils.ToastUtil;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;

/**
 * @author Guan
 * @date Created on 2018/6/3
 */
public class AccessTokenUtil {

    private static final String TAG = "EZOPENUtil";

    public static void initAccessToken(final Context context, final AccessTokenCallback callback) {
        String savedAccessToken = getSavedAccessToken(context);
        if (StringUtils.isEmpty(savedAccessToken)) {
            Log.e(TAG, "loadAccessToken=========");
            getAccessTokenByNetwork(new OkHttpManager.ResultCallback() {
                @Override
                public void onCallBack(final OkHttpManager.State state, final String result) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (state == OkHttpManager.State.SUCCESS) {
                                AccessTokenResult accessTokenResult = new Gson().fromJson(result, AccessTokenResult.class);
                                if (accessTokenResult.getData() != null) {
                                    //保存
                                    SharedPreferencesUtil.putString(context, AccessTokenResult.SAVED_ACCESSTOKEN, result);
                                    callback.onSuccess();
                                    //TODO 获取accessToken成功
                                    Log.i(TAG, "获取accessToken成功:" + result);
                                }
                            } else {
                                ToastUtil.logAndToast(context, "获取AccessToken失败:" + result);
                            }
                        }
                    });

                }
            });
        } else {
            callback.onSuccess();
        }
    }

    /**
     * 获取存储的accessToken
     * 如果accessToken
     *
     * @return
     */
    public static String getSavedAccessToken(Context context) {
        String savedJson = SharedPreferencesUtil.getString(context, AccessTokenResult.SAVED_ACCESSTOKEN);
        if (StringUtils.isNotEmpty(savedJson)) {
            AccessTokenResult accessTokenResult = new Gson().fromJson(savedJson, AccessTokenResult.class);
            long now = System.currentTimeMillis();
            if (accessTokenResult.getData().getExpireTime() - now > 3 * 60 * 60 * 1000) {
                return accessTokenResult.getData().getAccessToken();
            } else {
                //TODO toast
                Log.i(TAG, "accessToken距离过期时间不足3小时，请重新获取最新accessToken");
            }
        }
        return "";
    }

    public static void getAccessTokenByNetwork(OkHttpManager.ResultCallback callback) {
        //通过网络获取AccessToken
        Log.i(TAG, "通过网络获取最新accessToken====getAccessTokenByNetwork");
        HashMap<String, String> params = new HashMap<>();
        params.put("appKey", VideoConstant.Config.APPKEY);
        params.put("appSecret", VideoConstant.Config.SECRET);
        OkHttpManager.CommonPostAsyn(VideoConstant.GET_ACCESS_TOKEN, params, callback);

    }

    public interface AccessTokenCallback {
        void onSuccess();
    }
}
