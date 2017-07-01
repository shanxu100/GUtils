package scut.luluteam.gutils.activity.tab;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import java.util.ArrayList;
import java.util.List;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseActivity;
import scut.luluteam.gutils.fragment.BlankFragment;
import scut.luluteam.gutils.model.TabItemConfig.TabItemView;

/**
 * TabLayout 与 ViewPager 结合
 */
public class TabActivity extends BaseActivity {
    TabLayout content_tablayout;
    ViewPager content_viewpager;

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    List<TabItemView> tabItemViewList = new ArrayList<>();

    SimpleFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCustomBuilder().setNoTitle(true).build();

        setContentView(R.layout.activity_tab);

        initData();
        initUI();
    }

    private void initData() {
        fragmentList.add(BlankFragment.newInstance("a", true));
        fragmentList.add(BlankFragment.newInstance("b", true));
        fragmentList.add(BlankFragment.newInstance("c", true));
        fragmentList.add(BlankFragment.newInstance("d", true));

        titleList.add("1");
        titleList.add("2");
        titleList.add("3");
        titleList.add("4");

        tabItemViewList.add(new TabItemView(mContext, "1", 0, R.drawable.tab_item_sinaweibo_selector, 0));
        tabItemViewList.add(new TabItemView(mContext, "2", 0, R.drawable.tab_item_qq_selector, 0));
        tabItemViewList.add(new TabItemView(mContext, "3", 0, R.drawable.tab_item_shortmessage_selector, 0));
        tabItemViewList.add(new TabItemView(mContext, "4", 0, R.drawable.tab_item_wechat_selector, 0));
    }

    private void initUI() {
        content_tablayout = (TabLayout) findViewById(R.id.buttom_tablayout);
        content_viewpager = (ViewPager) findViewById(R.id.content_viewpager);
        //初始化
        fragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList,
                tabItemViewList, titleList);
        //为ViewPager设置Adapter
        content_viewpager.setAdapter(fragmentPagerAdapter);
        //tabLayOut和ViewPager关联
        content_tablayout.setupWithViewPager(content_viewpager, true);
        /**
         * MODE_FIXED:固定tabs，并同时显示所有的tabs。
         * MODE_SCROLLABLE：可滚动tabs，显示一部分tabs，在这个模式下能包含长标签和大量的tabs，最好用于用户不需要直接比较tabs。
         */
        content_tablayout.setTabMode(TabLayout.MODE_FIXED);
        //        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //手动指定TabLayout中Item的View
        for (int i = 0; i < content_tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = content_tablayout.getTabAt(i);
            tab.setCustomView(fragmentPagerAdapter.getTabItemView(i));
        }

    }


    public static class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> pagerList;
        List<TabItemView> tabItemViewList;
        List<String> titleList = new ArrayList<>();

        public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList,
                                          List<TabItemView> tabItemViewList,
                                          List<String> titleList) {
            super(fm);
            this.pagerList = fragmentList;
            this.tabItemViewList = tabItemViewList;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
            return pagerList.get(position);
        }

        @Override
        public int getCount() {
            return pagerList != null ? pagerList.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //通过自定义ItemView来显示
            //return titleList.get(position);
            return null;
        }

        //返回自定义ItemView
        public TabItemView getTabItemView(int position) {
            return tabItemViewList.get(position);
        }


    }
}
