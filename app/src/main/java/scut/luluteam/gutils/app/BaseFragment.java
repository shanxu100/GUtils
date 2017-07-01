package scut.luluteam.gutils.app;

import android.support.v4.app.Fragment;


/**
 * Created by guan on 6/20/17.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onStart() {
        super.onStart();
        //FragmentProxy.callonCreate(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //FragmentProxy.callonResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //FragmentProxy.callonPause(this);
    }
}
