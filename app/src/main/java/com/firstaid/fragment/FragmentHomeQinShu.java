package com.firstaid.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.firstaid.bean.RelativeItemBean;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.QMRActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.ImageFileCache;
import com.firstaid.util.ImageFileCacheUtils;
import com.firstaid.util.LogUtil;
import com.firstaid.util.LruImageCache;
import com.firstaid.util.Util;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2016/1/5.
 */
public class FragmentHomeQinShu extends BaseFragment {

    RelativeItemBean mRelativeItemBean;
    private NetworkImageView mIcon;
    private TextView mName;
    private TextView mPhoneNum;
    private View mBtnCall;


    public void setData(RelativeItemBean bean){
        mRelativeItemBean = bean;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_qinshu, container, false);

        mIcon = (NetworkImageView) root.findViewById(R.id.fks_iv_touxiang);
        mName = (TextView) root.findViewById(R.id.home_qinshu_name);
        mPhoneNum = (TextView) root.findViewById(R.id.home_qinshu_phone);
        if(BuildConfig.hasProduct){
            root.findViewById(R.id.qinshu_status).setVisibility(View.VISIBLE);
            mBtnCall = root.findViewById(R.id.tv_call);
            root.findViewById(R.id.queryMedicalRecords).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"查看情况",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), QMRActivity.class);
                    intent.putExtra("relationItemBeanPhone","" + mRelativeItemBean.phone);
                    startActivity(intent);
                }
            });

        }else{
            mBtnCall =  root.findViewById(R.id.btn_call);
            mBtnCall.setVisibility(View.VISIBLE);
        }
        mName.setText(mRelativeItemBean.relativesName);
        mPhoneNum.setText(root.getResources().getString(R.string.phone_format,mRelativeItemBean.phone));
        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BuildConfig.hasProduct) {
                    Util.call(root.getContext(), mRelativeItemBean.phone);
                }else{
                    //获取老人端区号
                    Util.call(root.getContext(),"120");
                }
            }
        });
        loadImgae();
        return root;
    }

    private  void loadImgae(){
        ImageLoader imageLoader = new ImageLoader(ConnectionUtil.getInstance().getRequestQueue(mIcon.getContext()), ImageFileCache.getInstance());
        mIcon.setImageUrl(mRelativeItemBean.smallImageUrl,imageLoader);
    }

}
