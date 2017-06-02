package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.RelationListFragment;
import com.firstaid.fragment.RelativesListFragment;

/**
 * Created by lenovo on 2016/1/6.
 */
public class RelativesListActivity extends  ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new RelativesListFragment();
    }
}
