package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.MyMessageListFragment;

/**
 * Created by lenovo on 2016/1/13.
 */
public class MyMessageListActivity extends ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new MyMessageListFragment();
    }
}
