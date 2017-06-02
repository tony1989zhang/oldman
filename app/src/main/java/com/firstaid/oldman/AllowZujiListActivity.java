package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.AllowZujiListFragment;

/**
 * Created by Administrator on 2016/1/7.
 */
public class AllowZujiListActivity extends ActivityForFragmentNormal {
    @Override
    public Fragment initFragment() {
        return new AllowZujiListFragment();
    }
}
