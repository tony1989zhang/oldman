package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.firstaid.bean.BaseBean;
import com.firstaid.fragment.XianJinListFragment;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.view.TitleView;

/**
 * Created by lenovo on 2016/1/21.
 */
public class ActivityXianJin extends ActivityForFragmentNormal implements View.OnClickListener{

    private TitleView mTitleView;
    private View mTopLoading;
    private TextView mTextXianJin;
    private TextView mTextBeiZhu;
    private TextView mTopRetry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xianjin);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitle(R.string.xianjin_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);

        mTopLoading = findViewById(R.id.top_loading);
        mTextXianJin = (TextView)findViewById(R.id.item_xianjin);
        findViewById(R.id.btn_tixian).setOnClickListener(this);
        mTextBeiZhu = (TextView) findViewById(R.id.text_beizhu);
        mTopRetry = (TextView) findViewById(R.id.top_retry);
        mTopRetry.setOnClickListener(this);
        requestBalance();
    }

    @Override
    public Fragment initFragment() {
        return new XianJinListFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:{
                finish();
                break;
            }
            case R.id.top_retry:{
                requestBalance();
                break;
            }
            case R.id.btn_tixian:
                startActivity(new Intent(ActivityXianJin.this,ActivityTiXianAccList.class));
                break;
        }

    }
    BalanceBean mBalanceBean;
    private void requestBalance(){
        mTopLoading.setVisibility(View.VISIBLE);
        mTopRetry.setVisibility(View.GONE);
        ConnectionManager.getInstance().queryBalance(this, "" + App.getInstance().getCustId(this), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx","ret "+ret);
                mTopLoading.setVisibility(View.GONE);
                mBalanceBean = App.getInstance().getBeanFromJson(ret, BalanceBean.class);
                if(mBalanceBean.success){
                    if(mBalanceBean.data != null){
                        mTextXianJin.setText(getString(R.string.xianjin_format,mBalanceBean.data.balance));
                        mTextBeiZhu.setText(mBalanceBean.data.remark);
                    }
                } else {
                    mTopRetry.setVisibility(View.VISIBLE);
                }
            }
        });
    }



    private class BalanceBean extends BaseBean{
        public BalanceDataBean data;
    }
    private class BalanceDataBean extends BaseBean{
        public String cus_id;
        public String balance;
        public String update_time;
        public String remark;
    }
}
