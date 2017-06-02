package com.firstaid.oldman;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class AddRelationActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXT_ADD_RELATIVES = "ext_add_relatives";
    TitleView mTitleView;
    private EditText mPhone;
    private boolean mAddRelatives = false;
    private boolean isCheckAlarmOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_relation);

        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitle(R.string.add_relation_title);
        super.initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mPhone = (EditText) findViewById(R.id.edit_phone);
        this.findViewById(R.id.btn_submit).setOnClickListener(this);
        if(this.getIntent() != null && getIntent().hasExtra(EXT_ADD_RELATIVES)){
            mAddRelatives = getIntent().getBooleanExtra(EXT_ADD_RELATIVES,false);
        }

        CheckBox isAlarmOrder = (CheckBox) findViewById(R.id.isAlarmOrder);
        isAlarmOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckAlarmOrder = isChecked;
            }
        });
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
                    Toast.makeText(AddRelationActivity.this, R.string.input_phone, Toast.LENGTH_SHORT).show();
                } else {
                    addRelation(phone);
                }
                break;
            }
        }
    }

    private void addRelation(String phone){
        showLoading();
        String id = ""+App.getInstance().getCustId(this);
        if(mAddRelatives){
            ConnectionManager.getInstance().addRelatives(mTitleView.getContext(), id, phone, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d("cx", "ret --- " + ret);
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if (baseBean.success) {
                        setResult(Activity.RESULT_OK);
                        AddRelationActivity.this.finish();
                    }
                }
            });
        } else {
            ConnectionManager.getInstance().addRelation(mTitleView.getContext(),id ,App.getInstance().getMoblieNo(mTitleView.getContext()),phone,isCheckAlarmOrder, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    LogUtil.d("cx","ret --- "+ret);
                    dismissLoading();
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    Toast.makeText(mTitleView.getContext(), baseBean.msg, Toast.LENGTH_SHORT).show();
                    if(baseBean.success){
                        setResult(Activity.RESULT_OK);
                        AddRelationActivity.this.finish();
                    }
                }
            });
        }

    }
}
