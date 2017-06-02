package com.firstaid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firstaid.oldman.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FragmentKinsfolkErxi extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kinsfolk_erxi, container, false);
    }
}