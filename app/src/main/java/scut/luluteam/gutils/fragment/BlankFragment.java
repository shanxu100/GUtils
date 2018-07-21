package scut.luluteam.gutils.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.activity.test.FirstActivity;
import scut.luluteam.gutils.app.BaseFragment;
import scut.luluteam.gutils.utils.http.retrofit.RetrofitUtil;


/**
 * yangqiangyu on 2017/6/5 10:30
 * email:168553877@qq.com
 * blog:http://blog.csdn.net/yissan
 */

public class BlankFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected View rootView;
    private Toolbar content_toolbar;
    private TextView TBTitle_tv;
    protected TextView mTitle;
    private Button btn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private boolean showToolbar;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1      Parameter 1.
     * @param showToolbar Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, boolean showToolbar) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, showToolbar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            showToolbar = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("======BlankFragment onDestroyView======" + mParam1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("======BlankFragment onDestroy======" + mParam1);
    }

    private void initView(View rootView) {
        mTitle = (TextView) rootView.findViewById(R.id.title);
        btn = (Button) rootView.findViewById(R.id.btn);

        if (showToolbar) {

            content_toolbar = (Toolbar) rootView.findViewById(R.id.content_toolbar);
            TBTitle_tv = (TextView) rootView.findViewById(R.id.TBTitle_tv);
            //this.setHasOptionsMenu(true);
            ((AppCompatActivity) getActivity()).setSupportActionBar(content_toolbar);
            //显示返回键
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //取消标题
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar()
            //显示自定义标题
            TBTitle_tv.setText("Fragment Toolbar");
        }

        mTitle.setText(mParam1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRxAndRetrofit();
            }
        });
    }


    private void testRxAndRetrofit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", "luluteam");
        RetrofitUtil.commomPostAsyn("http://125.216.242.147:8080/netTest/log/delayInterface", params, RetrofitUtil.defaultContentType,
                new RetrofitUtil.Callback() {
                    @Override
                    public void onData(String data) {
                        mTitle.setText(data);
                        System.out.println("=============" + data + "  getActivity=" + (getActivity() == null));
                    }
                });
    }
}
