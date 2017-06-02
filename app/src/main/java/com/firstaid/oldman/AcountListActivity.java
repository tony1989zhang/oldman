package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.AcountListFragment;

/**
 * Created by lenovo on 2016/1/22.
 */
public class AcountListActivity extends ActivityForFragmentNormal {
    @Override
    public Fragment initFragment() {
        return new AcountListFragment();
    }
}
