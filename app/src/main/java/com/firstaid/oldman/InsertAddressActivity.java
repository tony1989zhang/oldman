package com.firstaid.oldman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.bean.AddressDataBean;
import com.firstaid.bean.BaseBean;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.view.TitleView;

/**
 * Created by lenovo on 2016/1/19.
 */
public class InsertAddressActivity extends BaseActivity implements View.OnClickListener {

    private TitleView mTitleView;
    private TextView mName;
    private TextView mPhone;
    private TextView mAddr;
    private TextView mYouBian;

    private AddressDataBean mAddressDataBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_address);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitle(R.string.insert_addr_title);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        initSystemBarTint(mTitleView, Color.TRANSPARENT);

        mName = (TextView) findViewById(R.id.item_name);
        mPhone = (TextView) findViewById(R.id.item_phone);
        mAddr = (TextView) findViewById(R.id.item_addr);
        mYouBian = (TextView) findViewById(R.id.item_youbian);

        findViewById(R.id.my_name).setOnClickListener(this);
        findViewById(R.id.my_phone).setOnClickListener(this);
        findViewById(R.id.my_addr).setOnClickListener(this);
        findViewById(R.id.my_youbian).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        if(this.getIntent() != null && this.getIntent().hasExtra("ext_item")){
            mAddressDataBean = (AddressDataBean) this.getIntent().getSerializableExtra("ext_item");
        }
        if(mAddressDataBean != null){
            //means update
            mTitleView.setTitle("修改地址");
            mName.setText(mAddressDataBean.receiver);
            mPhone.setText(mAddressDataBean.phone);
            mAddr.setText(mAddressDataBean.address);
            mYouBian.setText(mAddressDataBean.zip_code);
        }
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.my_name:{
                if (null == mAddressDataBean)
                    showEditDialog(0);
                    else
                showEditDialog(0,mAddressDataBean.receiver);
                break;
            }
            case R.id.my_phone:{
                if (null == mAddressDataBean)
                    showEditDialog(2);
                else
                showEditDialog(2,mAddressDataBean.phone);
                break;
            }
            case R.id.my_addr:{
                if (null == mAddressDataBean)
                    showEditDialog(1);
                else
                showEditDialog(1,mAddressDataBean.address);
                break;
            }
            case R.id.my_youbian:{
                if (null == mAddressDataBean)
                    showEditDialog(3);
                else
                showEditDialog(3,mAddressDataBean.zip_code);
                break;
            }
            case R.id.btn_ok:{
                showLoading();
                if(mAddressDataBean == null){
                    //insert
                    ConnectionManager.getInstance().insertAddress(this, "" + App.getInstance().getCustId(this), mName.getText().toString(),
                            mPhone.getText().toString(), mYouBian.getText().toString(), mAddr.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
                                @Override
                                public void OnLoadEnd(String ret) {
                                    dismissLoading();
                                    BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                                    Toast.makeText(v.getContext(),bean.msg,Toast.LENGTH_SHORT).show();
                                    if (bean.success) {
                                        finish();
                                    }
                                }
                            });
                } else {
                    //update
                    ConnectionManager.getInstance().updateAddress(this, mAddressDataBean.id, mName.getText().toString(),
                            mPhone.getText().toString(), mYouBian.getText().toString(), mAddr.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
                                @Override
                                public void OnLoadEnd(String ret) {
                                    dismissLoading();
                                    BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                                    Toast.makeText(v.getContext(),bean.msg,Toast.LENGTH_SHORT).show();
                                    if (bean.success) {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                }
                            });
                }

                break;
            }
            case R.id.title_back:{
                finish();
                break;
            }
        }
    }


    private void showEditDialog(final int typeIndex){
        showEditDialog(typeIndex,null);
    }
    private void showEditDialog(final int typeIndex,String et){
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_text,null,false);
        final EditText editText = (EditText)view.findViewById(R.id.edit_text);
        if(null != et && !TextUtils.isEmpty(et)){
            editText.setText(et);
        }

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(typeIndex == 0){
                            mName.setText(editText.getText().toString());
                        } else if(typeIndex == 1){
                            mAddr.setText(editText.getText().toString());
                        } else if(typeIndex == 2){
                            mPhone.setText(editText.getText().toString());
                        } else if(typeIndex == 3){
                            mYouBian.setText(editText.getText().toString());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel,null)
                .show();

    }
}
