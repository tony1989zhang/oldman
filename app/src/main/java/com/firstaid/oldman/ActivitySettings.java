package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firstaid.uploadMedicalRecords.ChooseWayDialog;
import com.firstaid.uploadMedicalRecords.PhotoAlbumActivity;
import com.firstaid.uploadMedicalRecords.ToastManager;
import com.firstaid.view.TitleView;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ActivitySettings extends BaseActivity implements View.OnClickListener{

    private TitleView mTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_settings);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        super.initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitle(R.string.title_settings);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(this);
        findViewById(R.id.kan_zuji);//哪些人能看我的足迹
        TextView tvKanZJ = (TextView)findViewById(R.id.tv_kanzuji);
        findViewById(R.id.guanxiren);//管理关系人
        TextView tvGuanXiRen = (TextView) findViewById(R.id.tv_guanxiren);
        if(BuildConfig.hasProduct){
            tvKanZJ.setText("哪些人允许我看他的足迹");
            tvGuanXiRen.setText("管理关系人与个人信息");
            findViewById(R.id.baojing).setVisibility(View.GONE);
            findViewById(R.id.shangchuanzuji).setVisibility(View.GONE);//上传我的足迹
            findViewById(R.id.shiduan).setVisibility(View.GONE);
        }else{
            tvKanZJ.setText("哪些人能看见我足迹");
            findViewById(R.id.baojing).setVisibility(View.VISIBLE);
            findViewById(R.id.shiduan).setVisibility(View.GONE);
            findViewById(R.id.shangchuanzuji).setVisibility(View.VISIBLE);//上传我的足迹
        }


        findViewById(R.id.dianliang);//管理电量
        findViewById(R.id.weilan);//设置围栏GONE
        findViewById(R.id.baojing);//设置报警顺序
        findViewById(R.id.shiduan);//录音报警时间段
        findViewById(R.id.change_phone_no);//更改手机号码
        findViewById(R.id.be_relation);//申请加入列表



        findViewById(R.id.open_device).setVisibility(View.GONE);//开通设备


        if (BuildConfig.showAddress) {
           findViewById(R.id.insert_addr).setVisibility(View.GONE);
           findViewById(R.id.insert_addr).setOnClickListener(this);
           findViewById(R.id.manage_addr).setVisibility(View.GONE);
           findViewById(R.id.manage_addr).setOnClickListener(this);
//           findViewById(R.id.open_device_list).setVisibility(View.VISIBLE);
//           findViewById(R.id.open_device_list).setOnClickListener(this);
//           findViewById(R.id.help_record_list).setVisibility(View.VISIBLE);
//           findViewById(R.id.help_record_list).setOnClickListener(this);
//           findViewById(R.id.xianjin).setVisibility(View.VISIBLE);
//           findViewById(R.id.xianjin).setOnClickListener(this);
//           findViewById(R.id.tixian).setVisibility(View.VISIBLE);
//           findViewById(R.id.tixian).setOnClickListener(this);

        }

    }

    private ChooseWayDialog mDia;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:{
                this.finish();
                break;
            }
            case R.id.guanxiren:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(),RelationListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.change_phone_no:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(),ChangePhoneNumberActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.kan_zuji:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(),AllowZujiListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.open_device:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(),OpenDeviceActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.shiduan:{

                break;
            }
            case R.id.shangchuanzuji:{
                if (null == mDia) {
                    mDia = new ChooseWayDialog(this);
                    mDia.setWayBack(new RegistOnOperationListener());
                    mDia.settitle("添加或查看病例");
                    mDia.setData("添加病例", "查看病例",null);
                }
                mDia.show();
                break;
            }
            case R.id.baojing:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(), BaojingShunXuActivity.class);
                v.getContext().startActivity(intent);

                break;
            }
            case R.id.weilan:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MapWeiLanActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.dianliang:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(), DianLiangSettingsActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.be_relation:{
                Intent intent = new Intent();
                intent.setClass(v.getContext(), BeRelationListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.insert_addr: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), InsertAddressActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.manage_addr: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), AddressListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
//            case R.id.open_device_list: {
//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), OpenDeviceListActivity.class);
//                v.getContext().startActivity(intent);
//                break;
//            }
            case R.id.help_record_list: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), HelpRecordListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.xianjin: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ActivityXianJin.class);
                v.getContext().startActivity(intent);

                break;
            }
//            case R.id.tixian: {
//                Intent intent = new Intent();
//                intent.setClass(v.getContext(), AcountListActivity.class);
//                v.getContext().startActivity(intent);
//                break;
//            }
        }
    }
    class RegistOnOperationListener implements ChooseWayDialog.ChooseBack {

        @Override
        public void wayback(int i) {
            // TODO Auto-generated method stub
            switch (i) {
                case 0:
                    Intent intent = new Intent(ActivitySettings.this, PhotoAlbumActivity.class);
                    intent.putExtra("isFromOtherActivity", true);
                    startActivity(intent);
//                    mDia.show();
                    mDia.cancel();
                    break;
                case 1:
//                    ToastManager.getManager().show("查看病例");
                      startActivity(new Intent(ActivitySettings.this,QMRActivity.class));
//				photo();
                    mDia.cancel();
                    break;
                case 2:
                    mDia.cancel();
                    break;

            }
        }
    }

}
