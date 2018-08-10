package scut.luluteam.gutils.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.app.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 * <p>
 * TabFragment是一个嵌套多个fragment的fragment
 */
public class TabFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //=======================

    TabLayout top_TabLayout;
    ViewPager content_viewPager;

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    //List<TabItemView> tabItemViewList=new ArrayList<>();
    //List<ItemHolder> itemHolderList = new ArrayList<>();

    ContentFragmentPagerAdapter pagerAdapter;

    //=======================

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        top_TabLayout = (TabLayout) rootView.findViewById(R.id.top_tablayout);
        content_viewPager = (ViewPager) rootView.findViewById(R.id.content_viewpager);

        top_TabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        content_viewPager.setAdapter(pagerAdapter);
        top_TabLayout.setupWithViewPager(content_viewPager);


        return rootView;
    }

    private void initData() {
//        itemHolderList.add(new ItemHolder(null,
//                BlankFragment.newInstance("a", "1"),
//                new TabItemView(getContext(), "1", 0, 0, 0)));
//

        fragmentList.add(BlankFragment.newInstance("a", false));
        fragmentList.add(BlankFragment.newInstance("b", false));
        fragmentList.add(BlankFragment.newInstance("c", false));
        fragmentList.add(BlankFragment.newInstance("d", false));
        fragmentList.add(BlankFragment.newInstance("e", false));
        fragmentList.add(BlankFragment.newInstance("f", false));
        fragmentList.add(BlankFragment.newInstance("g", false));
        fragmentList.add(BlankFragment.newInstance("h", false));

        titleList.add("1");
        titleList.add("2");
        titleList.add("3");
        titleList.add("4");
        titleList.add("5");
        titleList.add("6");
        titleList.add("7");
        titleList.add("8");

        pagerAdapter = new ContentFragmentPagerAdapter(getChildFragmentManager(), fragmentList, titleList);

    }


    public static class ContentFragmentPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragmentList;
        List<String> titleList;

        public ContentFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }


}
