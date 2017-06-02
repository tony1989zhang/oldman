package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.RelationListFragment;

/**
 * Created by lenovo on 2016/1/6.
 */
public class RelationListActivity extends  ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new RelationListFragment();
    }
}
