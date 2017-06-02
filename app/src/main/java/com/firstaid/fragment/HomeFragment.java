package com.firstaid.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AutomaticGainControl;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.firstaid.bean.InviteInfoBean;
import com.firstaid.bean.NewsItemBean;
import com.firstaid.bean.NewsListBean;
import com.firstaid.bean.Query120CenterBean;
import com.firstaid.bean.RelativeIndexBean;
import com.firstaid.bean.RelativeItemBean;
import com.firstaid.bean.RelativeListBean;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BaseFragmentActivity;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.CodeCreatorActivity;
import com.firstaid.oldman.DianLiangSettingsActivity;
import com.firstaid.oldman.MainActivity;
import com.firstaid.oldman.NewsListActivity;
import com.firstaid.oldman.OpenDeviceActivity;
import com.firstaid.oldman.OpenDeviceListActivity;
import com.firstaid.oldman.R;
import com.firstaid.oldman.RelationListActivity;
import com.firstaid.oldman.RelativesListActivity;
import com.firstaid.oldman.WebActivity;
import com.firstaid.service.HeadSetService;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.HomeNewsListView;
import com.firstaid.view.SmartIndicator;
import com.firstaid.view.TitleView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/2.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final int REQUEST_OPEN_DEVICE = 101;
    private static final int REQUEST_VIEW_RELATIVES = 102;
    private TextView mShebeiState;
    private TextView m120State;
    private TextView mGpsState;
    private TextView mCreditState;
    private TextView mBatteryState;
    private TextView mNetworkState;
    private TitleView mTitleView;

    private TextView mQinShuTitle;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private SmartIndicator mQinshuIndicator;
    private View mQinshuRetry;
    private View mQinShuVpRoot;
    private View mQinShuLoading;
    private View mNewsRetry;
    private View mNewsMore;
    private View mNewsLoading;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private HomeNewsListView mHomeNewsListView;
    private Button mJiJiuCheState;
    TextView mInvitationCode;
    TextView mParamValue;
    View mInviteShare;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View root = inflater.inflate(getLayoutRes(), container, false);
        initViews(root);
        return root;
    }

    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    protected void initViews(View root) {
        mTitleView = (TitleView) root.findViewById(R.id.title_view);
        if (this.getActivity() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) this.getActivity()).initSystemBarTint(mTitleView, Color.TRANSPARENT);
        }
        mTitleView.setTitle(R.string.app_name);
//        mTitleView.setTitle(R.string.title_home);

        root.findViewById(R.id.top_panel).setOnClickListener(this);


        mShebeiState = (TextView) root.findViewById(R.id.tv_shebei_state);
        m120State = (TextView) root.findViewById(R.id.tv_120_state);
        mGpsState = (TextView) root.findViewById(R.id.tv_gps_state);
        mCreditState = (TextView) root.findViewById(R.id.tv_credit_state);
        mBatteryState = (TextView) root.findViewById(R.id.tv_battery_state);
        mNetworkState = (TextView) root.findViewById(R.id.tv_network_state);
        mViewPager = (ViewPager) root.findViewById(R.id.mykinsfolk_vp);
        mQinshuRetry = root.findViewById(R.id.qinshu_retry);
        mQinShuLoading = root.findViewById(R.id.qinshu_loading);
        mQinShuTitle = (TextView) root.findViewById(R.id.qinshu_title);
        TextView baojingStaus = (TextView) root.findViewById(R.id.baojing_staus);
        baojingStaus.setOnClickListener(this);
        mQinShuVpRoot = root.findViewById(R.id.qinshu_vp_root);
        mQinshuIndicator = (SmartIndicator) root.findViewById(R.id.qinshu_indicator);

        mNewsRetry = root.findViewById(R.id.news_retry);
        mNewsMore = root.findViewById(R.id.news_more);
        mNewsLoading = root.findViewById(R.id.news_loading);
        mHomeNewsListView = (HomeNewsListView) root.findViewById(R.id.home_news_list);
        mJiJiuCheState = (Button) root.findViewById(R.id.btn_jijiuche_state);
        mJiJiuCheState.setOnClickListener(this);
        queryRelativeIndex();
        queryRelativeList();
        queryNews();
        //addRelatives();
        initGps();
        root.findViewById(R.id.iv_shebei_icon).setOnClickListener(this);
        root.findViewById(R.id.iv_gps_icon).setOnClickListener(this);
        root.findViewById(R.id.iv_network_icon).setOnClickListener(this);
        root.findViewById(R.id.dianliang_root).setOnClickListener(this);
        root.findViewById(R.id.credit_root).setOnClickListener(this);
        mQinshuRetry.setOnClickListener(this);
        mNewsRetry.setOnClickListener(this);
        mNewsMore.setOnClickListener(this);
        mQinShuTitle.setOnClickListener(this);
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }

        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                mQinshuIndicator.onPageScrolled(v, i);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        queryHelpRecord(mTitleView.getContext());
        queryInvitationInfo(root);
    }

    private void queryInvitationInfo(View root) {
        mInviteShare = root.findViewById(R.id.share_layout);
        mInviteShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                App.getInstance().doShare(getActivity(), "分享", mTitleView.getResources().getString(R.string.invite_share_content, "" + mInvitationCode.getText().toString()), "", "http://7.77-7.com/x.htm");
                return true;
            }
        });

        mInvitationCode = (TextView) root.findViewById(R.id.tv_invitationCode);
        mInvitationCode.setOnClickListener(this);
        mParamValue = (TextView) root.findViewById(R.id.tv_paramValue);
        mInviteShare.setVisibility(View.GONE);
        showLoading();
        ConnectionManager.getInstance().queryInvitationInfo(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                dismissLoading();
                InviteInfoBean bean = App.getInstance().getBeanFromJson(ret, InviteInfoBean.class);
                if (bean.success && bean.data != null) {
                    String mInviteCode = bean.data.invitationCode;
                    mInviteShare.setVisibility(View.VISIBLE);
                    mInvitationCode.setText("" + bean.data.invitationCode);
//                    mInvitationCode.setText(mInvitationCode.getResources().getString(R.string.invite_code_format, bean.data.invitation?Code));
                    mParamValue.setText("￥" + bean.data.paramValue);
                } else {
                    mInvitationCode.setVisibility(View.GONE);
                    mInviteShare.setVisibility(View.GONE);
                }
            }
        });
    }


    /*更新亲属数据*/
    private void updateQinShuData() {
        LogUtil.d("updateQinShuData", "updateQinShuData:" + mRelativeListBean.data);
        if (mRelativeListBean.data != null && mRelativeListBean.data.size() > 0) {
            String[] names = new String[mRelativeListBean.data.size()];
            String[] icons = new String[mRelativeListBean.data.size()];
            LogUtil.d("updateQinShuData", "updateQinShuData_names:" + names.length);
            LogUtil.d("updateQinShuData", "updateQinShuData_icons:" + icons.length);
            int i = 0;
            StringBuffer sb = new StringBuffer();
            for (RelativeItemBean item : mRelativeListBean.data) {
                LogUtil.d("item.relativesName", "item.relativesName:" + item.relativesName);
                LogUtil.d("item.smallImageUrl", "item.smallImageUrl:" + item.smallImageUrl);
                FragmentHomeQinShu fragmentHomeQinShu = new FragmentHomeQinShu();
                fragmentHomeQinShu.setData(item);
                mFragments.add(fragmentHomeQinShu);

                if ((item.relativesName == null) || item.relativesName.equals("")) {
                    item.relativesName = "未设置昵称";
                }
                names[i] = "" + item.relativesName;
                if ((item.smallImageUrl == null) || item.smallImageUrl.equals("")) {
                    item.smallImageUrl = "http://7xlh8k.com1.z0.glb.clouddn.com/default.png";
                }
                icons[i] = "" + item.smallImageUrl;
                LogUtil.d(" names[i]", " names[i]:" + names[i]);
                LogUtil.d("icons[i] ", "icons[i] :" + icons[i]);
                sb.append(item.phone);
                if (i < mRelativeListBean.data.size() - 1) {
                    sb.append(",");
                }
                i++;

            }
            SPUtil.getInstant(mTitleView.getContext()).save("relative_phones", sb.toString());
            mQinshuIndicator.setDatas(3, names, icons, mViewPager);
        }
        mAdapter.notifyDataSetChanged();
        ;
    }

    private boolean mIsDeviceOpen = false;
    private boolean mIsDevicePlugIn = false;
    RelativeIndexBean mRelativeIndexBean;

    private void queryRelativeIndex() {

        ConnectionManager.getInstance().getRelativeIndex(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {

                mRelativeIndexBean = App.getInstance().getBeanFromJson(ret, RelativeIndexBean.class);
                LogUtil.d("cx", mRelativeIndexBean.data + "  ret-queryRelativeIndex-- " + ret);
                mJiJiuCheState.setVisibility(View.GONE);
                if (mRelativeIndexBean.success) {
                    if (mRelativeIndexBean.data == null) {
                        mIsDeviceOpen = false;
                        //updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.no_device));
                        updateDeviceState();
                        mJiJiuCheState.setVisibility(View.GONE); //2016--3-07 暂时消除
                    } else {
                        if (mRelativeIndexBean.data.openFlag == null) {
                            mIsDeviceOpen = false;
                            //updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.no_device));
                            updateDeviceState();
                        } else if ("Y".equals(mRelativeIndexBean.data.openFlag)) {
                            mIsDeviceOpen = true;
                            //updateStateText(mShebeiState, 0, mShebeiState.getResources().getString(R.string.open));
                            updateDeviceState();
                        } else if ("N".equals(mRelativeIndexBean.data.openFlag)) {
                            mIsDeviceOpen = true;
                            //updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.not_open));
                            updateDeviceState();
                        }
                        if ("Y".equals(mRelativeIndexBean.data.alarmFlag)) {
                            mJiJiuCheState.setText(R.string.after_alram);
                            mJiJiuCheState.setBackgroundColor(mTitleView.getResources().getColor(R.color.title_bg_color));
                            mJiJiuCheState.setVisibility(View.VISIBLE);
                        } else if ("N".equals(mRelativeIndexBean.data.alarmFlag)) {
                            mJiJiuCheState.setVisibility(View.VISIBLE);
                            mJiJiuCheState.setText(mTitleView.getResources().getString(R.string.before_alram));
                            mJiJiuCheState.setBackgroundColor(0xff7043);
                            mJiJiuCheState.invalidate();
                        } else {
                            mJiJiuCheState.setVisibility(View.GONE);
                        }
                    }
                    mJiJiuCheState.setVisibility(View.GONE); //2016--3-07 暂时消除
                }

            }
        });

    }

    private RelativeListBean mRelativeListBean;

    private void queryRelativeList() {
        mQinshuRetry.setVisibility(View.GONE);
        mQinShuVpRoot.setVisibility(View.GONE);
        mQinShuLoading.setVisibility(View.VISIBLE);
        ConnectionManager.getInstance().queryRelativeList(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx", "queryRelativeList  ret " + ret);
                mQinShuLoading.setVisibility(View.GONE);

                mRelativeListBean = App.getInstance().getBeanFromJson(ret, RelativeListBean.class);
                if (mRelativeListBean.success) {
                    if (BuildConfig.addTestData) {
                        mRelativeListBean.data = testQinShuData();
                    }

                    mQinshuRetry.setVisibility(View.GONE);
                    if (mRelativeListBean.data.size() > 0) {
                        mQinShuVpRoot.setVisibility(View.VISIBLE);
                        updateQinShuData();

                    } else {
                        SPUtil.getInstant(mTitleView.getContext()).save("relative_phones", "");
                        mQinShuVpRoot.setVisibility(View.GONE);
                    }
                    mQinShuTitle.setText(mTitleView.getResources().getString(R.string.home_relative_title, mRelativeListBean.data.size()));
                } else {
                    mQinshuRetry.setVisibility(View.VISIBLE);
                    mQinShuVpRoot.setVisibility(View.GONE);
                    mQinShuTitle.setText(R.string.home_relative_title_empry);
                }

                LogUtil.d("cx", "ret-queryRelativeList-- " + ret);
            }
        });
    }

    private List<RelativeItemBean> testQinShuData() {
        List<RelativeItemBean> testData = new ArrayList<RelativeItemBean>();
        for (int i = 0; i < 5; i++) {
            RelativeItemBean bean = new RelativeItemBean();
            bean.relativesName = "name " + i;
            bean.phone = "1376588810" + i;
            bean.smallImageUrl = "http://img2.imgtn.bdimg.com/it/u=3947386643,2401800583&fm=21&gp=0.jpg";
            testData.add(bean);
        }
        return testData;
    }

    private NewsListBean mNewsListBean;

    private void queryNews() {
        mNewsLoading.setVisibility(View.VISIBLE);
        mNewsRetry.setVisibility(View.GONE);
        ConnectionManager.getInstance().queryNews(mTitleView.getContext(), "2", new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx", "queryNews  ==  " + ret);
                mNewsLoading.setVisibility(View.GONE);
                mNewsListBean = App.getInstance().getBeanFromJson(ret, NewsListBean.class);
                testNews();
                if (mNewsListBean.success) {
                    mHomeNewsListView.setDatas(mNewsListBean);
                    mNewsRetry.setVisibility(View.GONE);
                    mNewsMore.setVisibility(View.VISIBLE);
                } else {
                    mNewsRetry.setVisibility(View.VISIBLE);
                    mNewsMore.setVisibility(View.GONE);
                }
            }
        });

    }

    private void testNews() {
        if (!BuildConfig.addTestData) {
            return;
        }
        mNewsListBean.success = true;
        mNewsListBean.data = new ArrayList<NewsItemBean>();
        for (int i = 0; i < 10; i++) {
            NewsItemBean item = new NewsItemBean();
            item.title = "这是新闻标题，是假的，服务器没有新闻啊 " + i;
            item.content = "新闻内容就是这样的，我也不知道该怎么办弄，数据去哪了" + i;
            item.publish_time = "2016年1月6日 10:2" + i;
            mNewsListBean.data.add(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OPEN_DEVICE && resultCode == Activity.RESULT_OK) {
            queryRelativeIndex();
        } else if (requestCode == REQUEST_VIEW_RELATIVES && resultCode == Activity.RESULT_OK) {
            queryRelativeList();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qinshu_title: {
                LogUtil.d("cx", "onclick----");
                Intent intent = new Intent();
//                intent.setClass(v.getContext(), RelationListActivity.class);
                intent.setClass(v.getContext(), RelativesListActivity.class);
                startActivityForResult(intent, REQUEST_VIEW_RELATIVES);
                break;
            }
            case R.id.iv_gps_icon: {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent); //此为设置完成后返回到获取界面
                break;
            }
            case R.id.iv_network_icon: {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction(Intent.ACTION_VIEW);
                }
                this.startActivity(intent);
                break;
            }
            case R.id.qinshu_retry: {
                queryRelativeList();
                break;
            }
            case R.id.news_retry: {
                queryNews();
                break;
            }
            case R.id.btn_jijiuche_state: {
                if (mRelativeIndexBean != null && mRelativeIndexBean.data != null && "Y".equals(mRelativeIndexBean.data.alarmFlag)) {
                    if (getActivity() != null && getActivity() instanceof MainActivity) {
                        if (BuildConfig.hasProduct) {
                            ((MainActivity) getActivity()).mViewPager.setCurrentItem(2);
                        } else {
                            ((MainActivity) getActivity()).mViewPager.setCurrentItem(1);
                        }

                    }
                }
                break;
            }
            case R.id.iv_shebei_icon: {
                if (!mIsDeviceOpen) {
                    Intent intent = new Intent();
                    intent.setClass(mTitleView.getContext(), OpenDeviceActivity.class);
                    startActivityForResult(intent, REQUEST_OPEN_DEVICE);
                }
                break;
            }
            case R.id.dianliang_root: {
                Intent intent = new Intent();
                intent.setClass(mTitleView.getContext(), DianLiangSettingsActivity.class);
                mTitleView.getContext().startActivity(intent);
                break;
            }
            case R.id.credit_root: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), WebActivity.class);
                intent.putExtra("ext_title", getString(R.string.xingyong_shuoming_title));
                intent.putExtra("ext_url", "http://7.77-7.com/bz/lrxy.htm");
                v.getContext().startActivity(intent);
                break;
            }
            case R.id.news_more: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), NewsListActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.top_panel: {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), OpenDeviceListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.baojing_staus: {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setTab(2);
                break;
            }
            case R.id.tv_invitationCode:
            {

                Intent intent = new Intent(getActivity(), CodeCreatorActivity.class);
//                intent.putExtra("codeCreatorURL","http://7.77-7.com/x.htm");
                startActivity(intent);
                break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registHomeReceiver();
        updateGpsState();
        updateNetworkState();
        updateDeviceState();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHomeReceiver != null) {
            this.getActivity().unregisterReceiver(mHomeReceiver);
        }
    }

    HomeReceiver mHomeReceiver;

    private void registHomeReceiver() {
        mHomeReceiver = new HomeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(HeadSetService.ACTION_HEADSET_MODE_CHOOSE_TIMEOUT);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(FragmentNear.ACTION_LOCATED);
        this.getActivity().registerReceiver(mHomeReceiver, filter);
    }


    public class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            LogUtil.d("cx", "--- " + intent.getAction());
            if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction())) {
                mIsDevicePlugIn = false;
                if (intent.hasExtra("state")) {
                    if (intent.getIntExtra("state", 0) == 0) {
                        updateDeviceState();
                        //updateStateText(mShebeiState,1,mShebeiState.getResources().getString(R.string.not_connected));
                    } else {
                        mIsDevicePlugIn = true;
                        updateDeviceState();
                        //updateStateText(mShebeiState,0,mShebeiState.getResources().getString(R.string.connected));
                    }
                }
            } else if (HeadSetService.ACTION_HEADSET_MODE_CHOOSE_TIMEOUT.equals(intent.getAction())) {
                Toast.makeText(context, R.string.mode_seted_by_timeout, Toast.LENGTH_SHORT).show();
                //updateStateText(mShebeiState, 0, mShebeiState.getResources().getString(R.string.connected));
                updateDeviceState();
            } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                updateNetworkState();
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int status = intent.getIntExtra("status", -1);
                int health = intent.getIntExtra("health", -1);
                int level = -1; // percentage, or -1 for unknown
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                mBatteryState.setText(mBatteryState.getResources().getString(R.string.battery_format, level) + "%");
            } else if (FragmentNear.ACTION_LOCATED.equals(intent.getAction())) {
                query120CenterInfo(intent.getStringExtra("ext_provice"), intent.getStringExtra("ext_city"), intent.getStringExtra("ext_country"));
            }
        }

    }

    private void updateDeviceState() {
        if (!mIsDevicePlugIn) {
            updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.not_connected));
        } else if (!mIsDeviceOpen) {
            updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.no_device));
        } else {
            if (!App.getInstance().mIsIntelligent) {
                updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.not_connected));
            } else if (mRelativeIndexBean == null || mRelativeIndexBean.data == null) {
                updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.no_device));
            } else if ("Y".equals(mRelativeIndexBean.data.openFlag)) {
                updateStateText(mShebeiState, 0, mShebeiState.getResources().getString(R.string.open));
            } else if ("N".equals(mRelativeIndexBean.data.openFlag)) {
                updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.not_open));
            } else {
                updateStateText(mShebeiState, 1, mShebeiState.getResources().getString(R.string.unknown));
            }
        }
    }

    private void updateStateText(TextView tv, int state, String text) {
        if (state == 0) {
            //正常
            tv.setBackgroundResource(R.mipmap.background_zhuangtai);
            tv.setTextColor(tv.getResources().getColor(R.color.home_state_green));
        } else if (state == 1) {
            //红色
            tv.setBackgroundResource(R.mipmap.background_zhuangtai2);
            tv.setTextColor(tv.getResources().getColor(R.color.home_state_red));
        }
        tv.setText(text);
    }

    private void initGps() {
        LocationManager alm = (LocationManager) mTitleView.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mTitleView.getContext(), "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent); //此为设置完成后返回到获取界面
        }
    }

    private void updateGpsState() {
        LocationManager alm = (LocationManager) mTitleView.getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            updateStateText(mGpsState, 1, mTitleView.getResources().getString(R.string.not_connected));
        } else {
            updateStateText(mGpsState, 0, mTitleView.getResources().getString(R.string.connected));
        }
    }

    private void updateNetworkState() {
        final String currentNetwork = ConnectionUtil.getInstance().getCurrentNetType(mTitleView.getContext());
        if ("2G".equals(currentNetwork) || mTitleView.getResources().getString(R.string.not_connected).equals(currentNetwork)) {
            updateStateText(mNetworkState, 1, currentNetwork);
        } else {
            updateStateText(mNetworkState, 0, currentNetwork);
        }
    }

    private void queryHelpRecord(Context context) {
        ConnectionManager.getInstance().queryHelpRecord(context, "" + App.getInstance().getCustId(context), App.getInstance().getMoblieNo(context), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx", " help ret  " + ret);
            }
        });
    }

    private void addRelatives() {
        ConnectionManager.getInstance().addRelatives(mTitleView.getContext(), "" + App.getInstance().getCustId(mTitleView.getContext()), "13424351256", new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx", " ----------addRelatives------------  " + ret);
            }
        });
    }


    private void query120CenterInfo(String province, String city, String country) {
        ConnectionManager.getInstance().query120Center(mTitleView.getContext(), province, city, country, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                Query120CenterBean bean = App.getInstance().getBeanFromJson(ret, Query120CenterBean.class);
                if (bean.success) {
                    if (bean.data != null && bean.data.size() > 0) {
                        updateStateText(m120State, 0, mTitleView.getResources().getString(R.string.connected));
                    } else {
                        updateStateText(m120State, 1, mTitleView.getResources().getString(R.string.not_connected));
                    }
                } else {
                    updateStateText(m120State, 1, mTitleView.getResources().getString(R.string.unknown));

                }
            }
        });
    }
}
