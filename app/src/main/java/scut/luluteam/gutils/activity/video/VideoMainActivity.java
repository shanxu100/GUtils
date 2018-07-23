package scut.luluteam.gutils.activity.video;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.activity.video.model.CameraListResult;
import scut.luluteam.gutils.activity.video.model.SourceListResult;
import scut.luluteam.gutils.activity.video.other.AccessTokenUtil;
import scut.luluteam.gutils.activity.video.other.EZOPENUtil;
import scut.luluteam.gutils.activity.video.other.VideoConstant;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.utils.ShowUtil;
import scut.luluteam.gutils.utils.ToastUtil;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;

public class VideoMainActivity extends BaseActivity {

    private ListView lv_videoList;
    private ArrayAdapter<String> videListAdapter;
    private CameraListResult cameraListResult;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);
        initUI();
        initData();
    }

    private void initUI() {
        lv_videoList = (ListView) findViewById(R.id.lv_videoList);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载并刷新accessToken
                AccessTokenUtil.initAccessToken(mContext, new AccessTokenUtil.AccessTokenCallback() {
                    @Override
                    public void onSuccess(String accessTokenJson) {
                        loadVideoList(accessTokenJson);
                    }
                });
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void initData() {
        videListAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
        lv_videoList.setAdapter(videListAdapter);
        lv_videoList.setOnItemClickListener(new MyOnItemClickListener());
        //加载并刷新accessToken
        AccessTokenUtil.initAccessToken(mContext, new AccessTokenUtil.AccessTokenCallback() {
            @Override
            public void onSuccess(String accessTokenJson) {
                loadVideoList(accessTokenJson);
            }
        });

    }


    /**
     * 加载摄像头列表
     */
    private void loadVideoList(String accessToken) {
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", accessToken);
        OkHttpManager.CommonPostAsyn(VideoConstant.GET_CAMERA_LIST, params, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(final OkHttpManager.State state, final String result) {
                Log.i(TAG, "获取Camera列表： " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (state == OkHttpManager.State.SUCCESS) {
                            //TODO tmpRes --> result
                            cameraListResult = new Gson().fromJson(result, CameraListResult.class);
                            refreshVideoList(cameraListResult);
                        } else {
                            ShowUtil.UIToast(mContext, "获取设备列表失败：" + result);
                        }
                    }
                });

            }
        });
    }


    /**
     * 更新列表的显示
     *
     * @param cameraListResult
     */
    private void refreshVideoList(CameraListResult cameraListResult) {
        videListAdapter.clear();
        videListAdapter.addAll(cameraListResult.getAllInfoList());
        videListAdapter.notifyDataSetChanged();
    }

    /**
     * 获取RTMP、HLS播放源，
     * 用于H5页面的播放
     *
     * @param item
     */
    private void loadPlayUrl(CameraListResult.Item item) {
        String GET_LIVE_ADDRESS = "https://open.ys7.com/api/lapp/live/address/get";
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", "");
        params.put("source", item.getDeviceSerial() + ":" + item.getChannelNo());
        OkHttpManager.CommonPostAsyn(GET_LIVE_ADDRESS, params, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(final OkHttpManager.State state, final String result) {
                Log.i(TAG, "获取RTMP、HLS播放源: " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (state == OkHttpManager.State.SUCCESS) {

                            SourceListResult sourceListResult = new Gson().fromJson(result, SourceListResult.class);
                            if (sourceListResult.getData() != null && sourceListResult.getData().size() > 0) {
                                showPrePlayDialog(sourceListResult.getData().get(0));
                            } else {
                                //toast 获取播放源失败
                            }

                        } else {
                            //toast 获取播放源失败
                        }
                    }
                });
            }
        });


    }

    /**
     * 弹出dialog，提示是否播放
     * RTMP\HLS 播放源
     */
    private void showPrePlayDialog(SourceListResult.Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        if (item.getStatus() == 1) {
            builder.setTitle("观看直播")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        } else {
            builder.setTitle("设备异常，请检查:" + item.getStatus());
        }

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();


    }


    /**
     * 弹出提示框
     *
     * @param msg
     */
    private void showTipDialog(String msg) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(msg + "\n")
                .show();
    }


    /**
     * 点击ListView，弹出Dialog
     */
    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        private static final String offlineMsg = "设备已离线，请检查摄像头网络连接。\n\n刷新后重试。";
        private static final String isEncryptMsg = "设备已加密，请通知管理员关闭加密选项再观看。";

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (cameraListResult == null) {
                return;
            }
            CameraListResult.Item item = cameraListResult.getData().get(i);
            if (item.getStatus() != 1) {
                showTipDialog(offlineMsg);
                return;
            }
            if (item.getIsEncrypt() != 0) {
                showTipDialog(isEncryptMsg);
                return;
            }

            //检查完毕，可以播放视频。
            // 必须使用EZOPEN协议
            PlayActivity.startPlayActivity(mContext, VideoConstant.Config.APPKEY, AccessTokenUtil.getSavedAccessToken(mContext),
                    EZOPENUtil.getLiveUrl(item.getDeviceSerial(), item.getChannelNo()));
        }
    }


}
