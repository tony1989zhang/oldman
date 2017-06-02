package com.firstaid.oldman;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.bean.ReservePlanBean;
import com.firstaid.fragment.ReservePlanFragment;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.view.TitleView;

/**
 * Created by lenovo on 2016/1/13.
 */
public class ReservePlanActivity extends ActivityForFragmentNormal {
    @Override
    public Fragment initFragment() {
        return new ReservePlanFragment();
    }


    //    private TitleView mTitleView;
//    private TextView mTitle;
//    private TextView mTime;
//    private TextView mContent;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_reserve_plan);
//        mTitleView = (TitleView) findViewById(R.id.title_view);
//        initSystemBarTint(mTitleView, Color.TRANSPARENT);
//        mTitle = (TextView) findViewById(R.id.item_title);
//        mTime = (TextView) findViewById(R.id.item_time);
//        mContent = (TextView) findViewById(R.id.item_content);
//        mTitleView.setTitle(R.string.reserve_plan_title);
//        mTitleView.setTitle(R.string.reserve_plan_title);
//        mTitleView.setTitleBackVisibility(View.VISIBLE);
//        mTitleView.setTitleBackOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        requestData();
//    }
//
//    private void requestData(){
//        showLoading();
//        ConnectionManager.getInstance().queryReservePlan(this, null, new ConnectionUtil.OnDataLoadEndListener() {
//            @Override
//            public void OnLoadEnd(String ret) {
//                LogUtil.d("ret","ret:" + ret);
//                dismissLoading();
//                ReservePlanBean bean = App.getInstance().getBeanFromJson(ret,ReservePlanBean.class);
//                if(bean.success && bean.data != null&& !bean.data.equals("")){
//                    mTitle.setText(bean.data.get(0).title);
//                    mTime.setText(ReservePlanActivity.this.getString(R.string.reserve_plan_time_format,bean.data.get(0).editor,bean.data.get(0).editTime));
//                    mContent.setText(bean.data.get(0).content);
//                } else {
//                    Toast.makeText(ReservePlanActivity.this, bean.msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
