package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.AddressListFragment;

/**
 * Created by lenovo on 2016/1/19.
 */
public class AddressListActivity extends ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new AddressListFragment();
    }
}
