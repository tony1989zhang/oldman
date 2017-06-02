package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.HelpRecordListFragment;

/**
 * Created by lenovo on 2016/1/20.
 */
public class HelpRecordListActivity extends ActivityForFragmentNormal {
    @Override
    public Fragment initFragment() {
        return new HelpRecordListFragment();
    }
}
