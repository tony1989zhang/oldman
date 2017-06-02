package com.firstaid.oldman;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.firstaid.fragment.AcountListFragment;

public class ActivityTiXianAccList extends ActivityForFragmentNormal
{

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_activity_qu_xian);
    }

    @Override
    public Fragment initFragment() {
        return new AcountListFragment();// new TiXianAccListFragment();
    }
}
