package com.firstaid.oldman;

import android.support.v4.app.Fragment;

import com.firstaid.fragment.BaojingShunXuFragment;


/**
 * Created by lenovo on 2016/1/6.
 */
public class BaojingShunXuActivity extends  ActivityForFragmentNormal {

    @Override
    public Fragment initFragment() {
        return new BaojingShunXuFragment();
    }
}
