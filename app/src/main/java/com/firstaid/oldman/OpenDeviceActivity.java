package com.firstaid.oldman;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firstaid.bean.BaseBean;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.view.TitleView;

/**
 * Created by lenovo on 2016/1/6.
 */
public class OpenDeviceActivity extends BaseActivity implements View.OnClickListener {

    TitleView mTitleView;
    private EditText mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_open_device);

        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitle(R.string.open_device_title);
        super.initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        this.findViewById(R.id.btn_submit).setOnClickListener(this);
        openDevice("");

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_back:{
                finish();
                break;
            }
            case R.id.btn_submit: {
                final String phone = mPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(OpenDeviceActivity.this, R.string.input_device_code, Toast.LENGTH_SHORT).show();
                } else {
                    openDevice(phone);
                }
                break;
            }
        }
    }

    private void openDevice(String phone){
        showLoading();
        String id = ""+App.getInstance().getCustId(this);
        ConnectionManager.getInstance().openDevice(mTitleView.getContext(),id ,phone, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx","ret --- "+ret);
                dismissLoading();
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                if(baseBean.success){
                    setResult(RESULT_OK);
                    OpenDeviceActivity.this.finish();
                }
            }
        });
    }
}
