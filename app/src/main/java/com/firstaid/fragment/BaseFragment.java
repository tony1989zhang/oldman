package com.firstaid.fragment;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    ProgressDialog mLoadingDialog;
    protected void showLoading(){
        if(mLoadingDialog == null){
            mLoadingDialog = new ProgressDialog(this.getActivity());
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoading(){
        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }

    }

	
}
