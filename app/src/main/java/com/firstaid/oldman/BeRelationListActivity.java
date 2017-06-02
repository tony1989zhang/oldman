package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.BeRelationListFragment;

/**
 * Created by lenovo on 2016/1/6.
 */
public class BeRelationListActivity extends  ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new BeRelationListFragment();
    }
}
