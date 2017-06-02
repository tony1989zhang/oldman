package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.OpenDeviceListFragment;

/**
 * Created by lenovo on 2016/1/19.
 */
public class OpenDeviceListActivity extends ActivityForFragmentNormal {
    @Override
    public Fragment initFragment() {
        return new OpenDeviceListFragment();
    }
}
