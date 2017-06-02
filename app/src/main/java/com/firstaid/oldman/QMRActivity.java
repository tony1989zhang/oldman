package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firstaid.bean.MedicalRecordsBean;
import com.firstaid.bean.MedicalRecordsListBean;
import com.firstaid.bean.RelationItemBean;
import com.firstaid.dapter.ImageAdapter;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.InkPageIndicator;
import com.firstaid.view.TitleView;

import java.util.ArrayList;

public class QMRActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {
    ViewPager viewPager;
    InkPageIndicator inkPageIndicator;
    ArrayList<String> images = new ArrayList<>();
    TextView connectionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qmr);
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(titleView, Color.TRANSPARENT);
        titleView.setTitleBackVisibility(View.VISIBLE);
        titleView.setTitleBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setTitle("病例");
        connectionTV = (TextView) findViewById(R.id.tv_connection);

        Intent intent = getIntent();
        String relationBeanPhone = intent.getStringExtra("relationItemBeanPhone");
        String relationBean_id = (String) SPUtil.getInstant(this).get("relationBean_id", "");
        String relationBean_phone = (String) SPUtil.getInstant(this).get("relationBean_phone", "");
        String phone = null;
        if(BuildConfig.hasProduct){
            phone = relationBean_phone;
        }else{
            phone = "" + App.getInstance().getMoblieNo(this);
        }
        ConnectionManager.getInstance().queryMedicalRecords(this, phone, this);
        viewPager = (ViewPager) findViewById(R.id.image_view_pager);

        inkPageIndicator = (InkPageIndicator) findViewById(R.id.ink_pager_indicator);
//        images.add("http://a.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=dd50d45d47166d2238221d90731325c1/83025aafa40f4bfb7346cdc2074f78f0f6361895.jpg");
//        images.add("http://b.hiphotos.baidu.com/zhidao/pic/item/d8f9d72a6059252dcd7d1d36379b033b5bb5b99d.jpg");
        images.add("");
        viewPager.setAdapter(new ImageAdapter(images));
        inkPageIndicator.setViewPager(viewPager);

    }

    @Override
    public void OnLoadEnd(String ret) {
        LogUtil.d("ret", "ret:" + ret);
        MedicalRecordsListBean beanFromJson = App.getInstance().getBeanFromJson(ret, MedicalRecordsListBean.class);
        if (beanFromJson.success) {
            if (null == beanFromJson.data || ("").equals(beanFromJson) || beanFromJson.data.size() == 0) {
                viewPager.setVisibility(View.GONE);
                connectionTV.setText("暂时无病例");
            } else {
                images.clear();
                for (MedicalRecordsBean bean : beanFromJson.data
                        ) {
                    images.add(bean.medicalRecordImg);
                }
                if (images.size() > 0) {
                    LogUtil.d("images", "images:" + images);
                    viewPager.setVisibility(View.VISIBLE);
                    viewPager.setAdapter(new ImageAdapter(images));
                    inkPageIndicator.setViewPager(viewPager);
                }
                connectionTV.setText("暂无描述");
            }
        }
    }
}
