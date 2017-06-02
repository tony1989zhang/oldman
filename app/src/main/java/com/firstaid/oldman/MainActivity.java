package com.firstaid.oldman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.mapapi.SDKInitializer;
import com.firstaid.bean.BaseBean;
import com.firstaid.fragment.FragmentMy;
import com.firstaid.fragment.FragmentNear;
import com.firstaid.fragment.HomeFragment;
import com.firstaid.fragment.ProductListFragment;
import com.firstaid.service.HeadSetService;
import com.firstaid.service.MyPushIntentService;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.KeyUtil;
import com.firstaid.util.LogUtil;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {

    public ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private int currentIndex;

    /**
     * 底部三个按钮
     */
    private RelativeLayout mHome;
    private RelativeLayout mNear;
    private RelativeLayout mMy;
    private RelativeLayout mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        UmengUpdateAgent.update(this);
        HeadSetService.startsevice(this);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        LogUtil.d("cx", "  no " + App.getInstance().getMoblieNo(this));
        initView();
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
//                if(BuildConfig.hasProduct){
                switch (position) {
                    case 0:
                        ((ImageView) mHome.findViewById(R.id.iv_home))
                                .setImageResource(R.mipmap.home2);
                        break;
                    case 1:
                        ((ImageView) mProduct.findViewById(R.id.iv_product))
                                .setImageResource(R.mipmap.home_product_h);
                        break;
                    case 2:
                        ((ImageView) mNear.findViewById(R.id.iv_near))
                                .setImageResource(R.mipmap.near2);
                        break;
                    case 3:
                        ((ImageView) mMy.findViewById(R.id.iv_my))
                                .setImageResource(R.mipmap.my2);
                        break;
                }
//                } else {
//                    switch (position) {
//                        case 0:
//                            ((ImageView) mHome.findViewById(R.id.iv_home))
//                                    .setImageResource(R.mipmap.home2);
//                            break;
//                        case 1:
//                            ((ImageView) mNear.findViewById(R.id.iv_near))
//                                    .setImageResource(R.mipmap.near2);
//                            break;
//                        case 2:
//                            ((ImageView) mMy.findViewById(R.id.iv_my))
//                                    .setImageResource(R.mipmap.my2);
//                            break;
//                    }
//                }
//


                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initHeadSetService();

        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setResourcePackageName("com.firstaid.oldman");
        mPushAgent.enable(mRegisterCallback);
        String device_token = UmengRegistrar.getRegistrationId(this);
        LogUtil.d("cx", mPushAgent.isEnabled() + "  device_token " + device_token);
        ConnectionManager.getInstance().bindDeviceToken(MainActivity.this, "" + App.getInstance().getCustId(this), "" + device_token, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("OnLoadEnd", "OnLoadEnd:" + ret);
                BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                if (baseBean.success) {

                } else {

                }
            }
        });
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        //服务端控制代码
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER);
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);

        int intExtra = getIntent().getIntExtra(MyPushIntentService.MYPUSHKEY, 0);
        if(intExtra != 0) {
            setTab(intExtra);
            FragmentNear fragment = (FragmentNear) mFragments.get(intExtra);
            fragment.setData();
        }
}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int intExtra = intent.getIntExtra(MyPushIntentService.MYPUSHKEY, 0);
        setTab(intExtra);
        FragmentNear fragment = (FragmentNear) mFragments.get(intExtra);
        fragment.setData();
    }

    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            LogUtil.d("cx", "---------------============-------     onRegistered     " + registrationId);
        }

    };

    /**
     * 对Button进行重置
     */
    protected void resetTabBtn() {

        ((ImageView) mHome.findViewById(R.id.iv_home)).setImageResource(R.mipmap.home);
        ((ImageView) mNear.findViewById(R.id.iv_near)).setImageResource(R.mipmap.near);
        ((ImageView) mMy.findViewById(R.id.iv_my)).setImageResource(R.mipmap.my);
        ((ImageView) mProduct.findViewById(R.id.iv_product)).setImageResource(R.mipmap.home_product_n);
    }


    /**
     * 初始化控件
     */
    private void initView() {

        mHome = (RelativeLayout) findViewById(R.id.rl_home);
        mNear = (RelativeLayout) findViewById(R.id.rl_near);
        mMy = (RelativeLayout) findViewById(R.id.rl_my);
        mProduct = (RelativeLayout) findViewById(R.id.rl_product);
//        if(BuildConfig.hasProduct){
        mProduct.setVisibility(View.VISIBLE);
//        } else {
//            mProduct.setVisibility(View.GONE);
//        }
        mHome.setOnClickListener(this);
        mNear.setOnClickListener(this);
        mMy.setOnClickListener(this);
        mProduct.setOnClickListener(this);

        HomeFragment tab01 = new HomeFragment();
        FragmentNear tab02 = new FragmentNear();
        FragmentMy tab03 = new FragmentMy();

        mFragments.add(tab01);
//        if(BuildConfig.hasProduct){
        mFragments.add(new ProductListFragment());
//        }
        mFragments.add(tab02);
        mFragments.add(tab03);

        currentIndex = 0;
    }

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
//        if(BuildConfig.hasProduct){
        switch (v.getId()) {
            case R.id.rl_home:
                currentIndex = 0;
                break;
            case R.id.rl_product:
                currentIndex = 1;
                break;
            case R.id.rl_near:
                currentIndex = 2;
                break;
            case R.id.rl_my:
                currentIndex = 3;
                break;
        }
//
//        } else {
//            switch (v.getId()) {
//                case R.id.rl_home:
//                    currentIndex = 0;
//                    break;
//                case R.id.rl_near:
//                    currentIndex = 1;
//                    break;
//                case R.id.rl_my:
//                    currentIndex = 2;
//                    break;
//            }
//        }


        mViewPager.setCurrentItem(currentIndex);
    }

    private void initHeadSetService() {
        Intent intent = new Intent();
        intent.setClass(this, HeadSetService.class);
        this.startService(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            // 音量减小
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                Log.i("abc", "音量DOWN");
//
//                return true;
//
//            // 音量增大
//            case KeyEvent.KEYCODE_VOLUME_UP:
//
//                if (event.getRepeatCount() == 7) {
//                    App.getInstance().mIsIntelligent = true;
//                    Log.i("repeatCount", "repeatCount:" + event.getRepeatCount());
//                    Intent sIntent = new Intent(this,HeadSetService.class);
//                    sIntent.putExtra("ext_cmd","mainActivity");
//
//                    startService(sIntent);
//                }
//                return true;
//        }
//        return super.onKeyLongPress(keyCode, event);
//    }

    public void setTab(int i) {
        if (i == 2) {
            onClick(mNear);
        }
    }
}
