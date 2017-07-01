package scut.luluteam.gutils.activity.tab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.fragment.BlankFragment;
import scut.luluteam.gutils.fragment.TabFragment;
import scut.luluteam.gutils.model.TabItemConfig;
import scut.luluteam.gutils.model.TabItemConfig.ItemHolder;
import scut.luluteam.gutils.model.TabItemConfig.TabItemView;


public class OnlyTabActivity extends BaseActivity {

    Toolbar content_toolbar;
    TextView TBtitle_tv;
    LinearLayout content_ly;
    TabLayout buttom_tablayout;

    FragmentManager manager;
    FragmentTransaction transaction;

    List<ItemHolder> itemHolderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCustomBuilder()
                .setNoTitle(true)
                .allowScreenRoate(false)
                .build();
        setContentView(R.layout.activity_only_tab);

        initData();
        initUI();
    }

    private void initData() {

        manager = getSupportFragmentManager();

        /**
         * 初始化多组Item数据，供Tab使用：一个Tab Fragment和多个Blank Fragment
         */
        itemHolderList.add(new ItemHolder("Tag1", TabFragment.newInstance("a", "a"),
                new TabItemView(mContext, "1", 0, R.drawable.tab_item_sinaweibo_selector, 0)));
        itemHolderList.add(new ItemHolder("Tag2", BlankFragment.newInstance("b", true),
                new TabItemView(mContext, "2", 0, R.drawable.tab_item_qq_selector, 0)));
        itemHolderList.add(new ItemHolder("Tag3", BlankFragment.newInstance("c", true),
                new TabItemView(mContext, "3", 0, R.drawable.tab_item_shortmessage_selector, 0)));
        itemHolderList.add(new ItemHolder("Tag4", BlankFragment.newInstance("d", true),
                new TabItemView(mContext, "4", 0, R.drawable.tab_item_wechat_selector, 0)));

    }

    private void initUI() {
        content_toolbar = (Toolbar) this.findViewById(R.id.content_toolbar);
        TBtitle_tv = (TextView) this.findViewById(R.id.TBTitle_tv);
        content_ly = (LinearLayout) this.findViewById(R.id.content_ll);
        buttom_tablayout = (TabLayout) this.findViewById(R.id.buttom_tablayout);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //设置toolbar
//        this.setSupportActionBar(content_toolbar);
//        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        TBtitle_tv.setText("jdhfk");

        content_toolbar.setVisibility(View.GONE);


        /**
         * Tab 和 具体的数据ItemHolder 绑定
         */
        buttom_tablayout.addTab(buttom_tablayout.newTab().setCustomView(itemHolderList.get(0).tabItemView), 0, true);//默认第一个item被选中
        buttom_tablayout.addTab(buttom_tablayout.newTab().setCustomView(itemHolderList.get(1).tabItemView), 1, false);
        buttom_tablayout.addTab(buttom_tablayout.newTab().setCustomView(itemHolderList.get(2).tabItemView), 2, false);
        buttom_tablayout.addTab(buttom_tablayout.newTab().setCustomView(itemHolderList.get(3).tabItemView), 3, false);

        buttom_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private int needHidePosition;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.e(TAG, "onTabSelected:" + tab.getTag() + "\t" + tab.getPosition());
                changeFragment(tab.getPosition(), needHidePosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Log.e(TAG, "onTabUnselected:" + tab.getTag() + "\t" + tab.getPosition());
                needHidePosition = tab.getPosition();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Log.e(TAG, "onTabReselected:" + tab.getTag() + "\t" + tab.getPosition());
            }
        });

        //初始化第一个fragment
        this.getSupportFragmentManager().beginTransaction()
                .add(R.id.content_ll, itemHolderList.get(0).fragment, itemHolderList.get(0).tag)
                .show(itemHolderList.get(0).fragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    /**
     * fragment的切换
     *
     * @param showPosition
     * @param hidePosition
     */
    private void changeFragment(int showPosition, int hidePosition) {
        transaction = manager.beginTransaction();
        Fragment showFragment = manager.findFragmentByTag(itemHolderList.get(showPosition).tag);
        if (showFragment == null) {
            Log.e(TAG, "未找到fragment");
            transaction.add(R.id.content_ll,
                    itemHolderList.get(showPosition).fragment,
                    itemHolderList.get(showPosition).tag);
        }
        transaction.hide((itemHolderList.get(hidePosition).fragment))
                .show((itemHolderList.get(showPosition).fragment))
                .commit();
    }


}
