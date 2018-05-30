package scut.luluteam.gutils.activity.video;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;

public class VideoMainActivity extends BaseActivity {

    private ListView lv_videoList;
    private ArrayAdapter<String> videListAdapter;
    private CameraListResult cameraListResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);
        initUI();
        initData();
    }

    private void initUI() {
        lv_videoList = (ListView) findViewById(R.id.lv_videoList);
    }

    private void initData() {
        videListAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
        lv_videoList.setAdapter(videListAdapter);
        lv_videoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cameraListResult == null) {
                    return;
                }
                CameraListResult.Item item = cameraListResult.getData().get(position);
                //1、必须使用EZOPEN协议
//                PlayActivity.startPlayActivity(mContext, "", "",
//                        EZOPENUtil.getLiveUrl(item.getDeviceSerial(), item.getChannelNo()));

                //2、实现H5播放
//                loadPlayUrl(item);

                startActivity(new Intent(mContext, WebVideoActivity.class));


            }
        });
        loadVideoList();

    }

    private void loadVideoList() {
        String GET_CAMERA_LIST = "https://open.ys7.com/api/lapp/camera/list";
//        String String
        HashMap<String, String> params = new HashMap<>();
        params.put("accessToken", "");
        OkHttpManager.CommonPostAsyn(GET_CAMERA_LIST, params, new OkHttpManager.ResultCallback() {
            @Override
            public void onCallBack(final OkHttpManager.State state, final String result) {
                Log.i(TAG, "获取Camera列表： " + result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (state == OkHttpManager.State.SUCCESS) {
                            String tmpRes = "{\n" +
                                    "    \"page\": {\n" +
                                    "        \"total\": 2,\n" +
                                    "        \"page\": 0,\n" +
                                    "        \"size\": 10\n" +
                                    "    },\n" +
                                    "    \"data\": [\n" +
                                    "        {\n" +
                                    "            \"deviceSerial\": \"427734444\",\n" +
                                    "            \"channelNo\": 1,\n" +
                                    "            \"channelName\": \"C1(427734444)\",\n" +
                                    "            \"status\": 1,\n" +
                                    "            \"isShared\": \"1\",\n" +
                                    "            \"picUrl\": \"http://img.ys7.com/group1/M00/02/B4/CmGCA1dRGyuAdJ_RAABJBCB_Re4796.jpg\",\n" +
                                    "            \"isEncrypt\": 1,\n" +
                                    "            \"videoLevel\": 2\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"deviceSerial\": \"519544444\",\n" +
                                    "            \"channelNo\": 1,\n" +
                                    "            \"channelName\": \"C2C(519544444)\",\n" +
                                    "            \"status\": 0,\n" +
                                    "            \"isShared\": \"2\",\n" +
                                    "            \"picUrl\": \"https://i.ys7.com/assets/imgs/public/homeDevice.jpeg\",\n" +
                                    "            \"isEncrypt\": 0,\n" +
                                    "            \"videoLevel\": 2\n" +
                                    "        }\n" +
                                    "    ],\n" +
                                    "    \"code\": \"200\",\n" +
                                    "    \"msg\": \"操作成功!\"\n" +
                                    "}";
                            cameraListResult = new Gson().fromJson(tmpRes, CameraListResult.class);
                            refreshVideoList(cameraListResult);
                        } else {
                            //toast 获取设备列表失败
                        }
                    }
                });


            }
        });
    }

    /**
     * 获取RTMP、HLS播放源
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


}
