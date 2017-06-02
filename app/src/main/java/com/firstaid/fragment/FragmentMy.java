package com.firstaid.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.bean.BaseBean;
import com.firstaid.bean.InviteInfoBean;
import com.firstaid.bean.MyIndexBean;
import com.firstaid.oldman.AbountActivity;
import com.firstaid.oldman.AcountListActivity;
import com.firstaid.oldman.ActivitySettings;
import com.firstaid.oldman.ActivityXianJin;
import com.firstaid.oldman.AddressListActivity;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BaseFragmentActivity;
import com.firstaid.oldman.BeRelationListActivity;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.DeliverListActivity;
import com.firstaid.oldman.EditMyInfoActivity;
import com.firstaid.oldman.HelpRecordListActivity;
import com.firstaid.oldman.InsertAddressActivity;
import com.firstaid.oldman.MyMessageListActivity;
import com.firstaid.oldman.OpenDeviceListActivity;
import com.firstaid.oldman.R;
import com.firstaid.oldman.ReservePlanActivity;
import com.firstaid.oldman.WebActivity;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.CircleNetworkImageView;
import com.firstaid.view.TitleView;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FragmentMy extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {

    private TitleView mTitleView;
    private CircleNetworkImageView mCircleNetworkImageView;
    private TextView mName;
    private TextView mRetry;
    private TextView mMyMsgCount;
    private TextView mInviteTv;
    private TextView mInviteShare;
    private TextView mInviteRetry;
    private MyIndexBean mBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        ;
        initViews(root);
        return root;
    }

    private void initViews(View root) {
        mTitleView = (TitleView) root.findViewById(R.id.title_view);
        if (this.getActivity() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) this.getActivity()).initSystemBarTint(mTitleView, Color.TRANSPARENT);
        }
        mTitleView.setTitleRightImg(R.mipmap.my_settings);
        mTitleView.setTitleRightOnClickListener(this);
        mTitleView.setTitle(R.string.fragment_my_title);
        mCircleNetworkImageView = (CircleNetworkImageView) root.findViewById(R.id.item_icon);
        mCircleNetworkImageView.setDefaultImageResId(R.mipmap.my_tou);
        mName = (TextView) root.findViewById(R.id.item_name);
        mRetry = (TextView) root.findViewById(R.id.retry);
        mRetry.setOnClickListener(this);
        mMyMsgCount = (TextView) root.findViewById(R.id.my_msg_count);
        mCircleNetworkImageView.setOnLongClickListener(this);
        mName.setOnLongClickListener(this);
        mInviteTv = (TextView) root.findViewById(R.id.invite_code_tv);
        mInviteTv.setVisibility(View.GONE);
        mInviteShare = (TextView) root.findViewById(R.id.invite_code_share);
        mInviteRetry = (TextView) root.findViewById(R.id.invite_retry);
        mInviteRetry.setOnClickListener(this);
        mInviteShare.setOnClickListener(this);

        root.findViewById(R.id.my_setting).setOnClickListener(this);
        root.findViewById(R.id.add_relation).setOnClickListener(this);
        root.findViewById(R.id.change_phone_no).setOnClickListener(this);
        root.findViewById(R.id.my_msg).setOnClickListener(this);
//        if(!BuildConfig.hasProduct) {
            root.findViewById(R.id.my_pengy).setOnClickListener(this);
//        }
        root.findViewById(R.id.my_jijiushouche).setOnClickListener(this);
        root.findViewById(R.id.my_wuliu).setOnClickListener(this);
        root.findViewById(R.id.my_help).setOnClickListener(this);
        root.findViewById(R.id.my_update).setOnClickListener(this);
        root.findViewById(R.id.my_about).setOnClickListener(this);
        root.findViewById(R.id.my_pingjia).setOnClickListener(this);
        root.findViewById(R.id.my_fankui).setOnClickListener(this);
        root.findViewById(R.id.my_setting).setOnClickListener(this);
        if (App.getInstance().getCustId(mTitleView.getContext()) == 0)
        {
            root.findViewById(R.id.my_delete_account).setVisibility(View.GONE);
        }
        else{
            root.findViewById(R.id.my_delete_account).setVisibility(View.GONE);//
           // root.findViewById(R.id.my_delete_account).setVisibility(View.VISIBLE);//暂时不现实
            root.findViewById(R.id.my_delete_account).setOnClickListener(this);
        }
//        if (BuildConfig.showAddress) {
//            root.findViewById(R.id.insert_addr).setVisibility(View.VISIBLE);
//            root.findViewById(R.id.insert_addr).setOnClickListener(this);
//            root.findViewById(R.id.manage_addr).setVisibility(View.VISIBLE);
//            root.findViewById(R.id.manage_addr).setOnClickListener(this);
//            root.findViewById(R.id.open_device_list).setVisibility(View.VISIBLE);
//            root.findViewById(R.id.open_device_list).setOnClickListener(this);
//            root.findViewById(R.id.help_record_list).setVisibility(View.VISIBLE);
        if(BuildConfig.hasProduct) {
            root.findViewById(R.id.xianjin).setVisibility(View.VISIBLE);
            root.findViewById(R.id.xianjin).setOnClickListener(this);
        }else{
            root.findViewById(R.id.xianjin).setVisibility(View.GONE);
        }

//            root.findViewById(R.id.help_record_list).setOnClickListener(this);
//            root.findViewById(R.id.tixian).setVisibility(View.VISIBLE);
//            root.findViewById(R.id.tixian).setOnClickListener(this);
//
//        }

        queryMyIndexInfo();
        setUpUmengFeedback();//用户反馈
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.title_right_img: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ActivitySettings.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.my_msg: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MyMessageListActivity.class);
                v.getContext().startActivity(intent);

                break;
            }
            case R.id.my_pengy: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), WebActivity.class);
                intent.putExtra("ext_title", getString(R.string.pengyou_shuohua));
                intent.putExtra("ext_url", "http://7.77-7.com/gz/gz-qs.htm");
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.my_jijiushouche: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), ReservePlanActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.my_wuliu: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), DeliverListActivity.class);
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.my_help: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), WebActivity.class);
                intent.putExtra("ext_title", getString(R.string.help_center));
                if (BuildConfig.hasProduct)
                    intent.putExtra("ext_url", "http://7.77-7.com/bz/bz-qs.htm");
                else
                    intent.putExtra("ext_url", "http://7.77-7.com/bz/bz-lr.htm");

                v.getContext().startActivity(intent);
                break;
            }
            case R.id.my_update: {
                //umeng update
                UmengUpdateAgent.setUpdateOnlyWifi(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        if (updateResponse == null) {
                            Toast.makeText(v.getContext(), R.string.check_update_failed, Toast.LENGTH_SHORT).show();
                        } else if (!updateResponse.hasUpdate) {
                            Toast.makeText(v.getContext(), R.string.check_update_newest, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                UmengUpdateAgent.update(v.getContext());
                break;
            }
            case R.id.my_about: {
                //goto about
                Intent intent = new Intent();
                intent.setClass(v.getContext(), AbountActivity.class);
                v.getContext().startActivity(intent);

                break;
            }
            case R.id.my_pingjia: {
                //go to 360
                if (is360Avilible(v.getContext(), "com.qihoo.appstore")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    ComponentName cn = new ComponentName("com.qihoo.appstore",
                            "com.qihoo.appstore.activities.SearchDistributionActivity");
                    intent.setComponent(cn);
                    intent.setData(Uri
                            .parse("market://details?id=" + App.getInstance().getPackageName()));
                    try {
                        v.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), R.string.goto_store_failed, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), R.string.store_not_installed, Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.my_fankui: {
                //feedback
//                if(BuildConfig.hasProduct){
//                 Toast.makeText(getActivity(),"正在修复",Toast.LENGTH_SHORT).show();
//                }else {
                    fb.startFeedbackActivity();
//                }
                break;
            }
            case R.id.my_delete_account: {
                SPUtil.getInstant(v.getContext()).save("custId", 0);
                SPUtil.getInstant(v.getContext()).save("mobileNo", "");
                SPUtil.getInstant(v.getContext()).save("custName", "");
                System.exit(0);
                showLoading();
                ConnectionManager.getInstance().deleteAccount(v.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                        dismissLoading();
                        BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                        if (bean.success) {


                        } else {
                        }
                        Toast.makeText(v.getContext(), bean.msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.retry: {
                mRetry.setVisibility(View.INVISIBLE);
                queryMyIndexInfo();
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
            case R.id.invite_retry: {
                queryInvitationInfo();
                break;
            }
            case R.id.invite_code_share: {
                App.getInstance().doShare(getActivity(), "分享", mTitleView.getResources().getString(R.string.invite_share_content, mInviteCode), "", "http://7.77-7.com/x.htm");
                break;
            }

            case R.id.my_setting:
            {
               startActivity(new Intent(getActivity(),ActivitySettings.class));
                break;
            }

        }
    }

    FeedbackAgent fb;

    private void setUpUmengFeedback() {
        fb = new FeedbackAgent(mTitleView.getContext());
        // check if the app developer has replied to the feedback or not.
        fb.sync();
        fb.openAudioFeedback();
        fb.openFeedbackPush();
        //PushAgent.getInstance(this).setDebugMode(true);
        PushAgent.getInstance(mTitleView.getContext()).enable();

        //fb.setWelcomeInfo();
        //  fb.setWelcomeInfo("Welcome to use umeng feedback app");
//        FeedbackPush.getInstance(this).init(true);
//        PushAgent.getInstance(this).setPushIntentServiceClass(MyPushIntentService.class);


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean result = fb.updateUserInfo();
            }
        }).start();
    }

    public static boolean is360Avilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    private void queryMyIndexInfo() {
        ConnectionManager.getInstance().queryMyIndexInfo(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                MyIndexBean bean = App.getInstance().getBeanFromJson(ret, MyIndexBean.class);
                LogUtil.d("cx", "ret " + ret);
                mBean = bean;
                if (bean.success) {
                    mRetry.setVisibility(View.GONE);
                    if (bean.data != null) {
                        ConnectionUtil.getInstance().loadImgae(mCircleNetworkImageView, bean.data.headImg);
                        mName.setText(bean.data.nickName);
                        if (bean.data.countMessage > 0) {
                            mMyMsgCount.setVisibility(View.VISIBLE);
                            if (bean.data.countMessage > 99) {
                                mMyMsgCount.setText("99+");
                            } else {
                                mMyMsgCount.setText("" + bean.data.countMessage);
                            }

                        } else {
                            mMyMsgCount.setVisibility(View.GONE);
                        }

                    } else {

                    }
                } else {
                    Toast.makeText(mTitleView.getContext(), bean.msg, Toast.LENGTH_SHORT).show();
                    mRetry.setVisibility(View.VISIBLE);
                    mMyMsgCount.setVisibility(View.GONE);
                }
            }
        });
        queryInvitationInfo();
    }


    public String mInviteCode = "";

    private void queryInvitationInfo() {
        mInviteRetry.setVisibility(View.GONE);
        showLoading();
        ConnectionManager.getInstance().queryInvitationInfo(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                dismissLoading();
                InviteInfoBean bean = App.getInstance().getBeanFromJson(ret, InviteInfoBean.class);
                if (bean.success && bean.data != null) {
                    mInviteCode = bean.data.invitationCode;
                    mInviteRetry.setVisibility(View.GONE);
                    mInviteTv.setVisibility(View.VISIBLE);
                    mInviteShare.setVisibility(View.VISIBLE);
                    mInviteTv.setText(mInviteTv.getResources().getString(R.string.invite_code_format, bean.data.invitationCode));
                } else {
                    if (mRetry.getVisibility() == View.VISIBLE) {
                        mInviteRetry.setVisibility(View.GONE);
                    } else {
                        mInviteRetry.setVisibility(View.VISIBLE);
                    }
                    mInviteTv.setVisibility(View.GONE);
                    mInviteShare.setVisibility(View.GONE);
                }
            }
        });
    }


    private static final int REQUEST_EIDT_MY_INFO = 102;

    @Override
    public boolean onLongClick(View v) {
        if (mBean == null || !mBean.success) {
            return false;
        }
        Intent intent = new Intent();
        intent.setClass(v.getContext(), EditMyInfoActivity.class);
        intent.putExtra("ext_data", mBean);
        startActivityForResult(intent, REQUEST_EIDT_MY_INFO);
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("cx", "requestCode " + requestCode + " = ===  " + requestCode);
        if (requestCode == REQUEST_EIDT_MY_INFO && resultCode == Activity.RESULT_OK) {
            queryMyIndexInfo();
        }
    }
}