package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.firstaid.bean.MyIndexBean;
import com.firstaid.event.ShareEvent;
import com.firstaid.event.UserEvent;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.youzan.sdk.YouzanBridge;
import com.youzan.sdk.YouzanSDK;
import com.youzan.sdk.YouzanUser;
import com.youzan.sdk.http.engine.OnRegister;
import com.youzan.sdk.http.engine.QueryError;
import com.youzan.sdk.web.plugin.YouzanWebClient;

/**
 * #html5交互版
 * #======
 *
 * #不必继承BaseWebActivity, 仅参考作用
 * #
 * #记住不要用短链接，短链接类似：http://kdt.im/......, 使用长连接
 * #不然会多一次跳转http://wap.koudaitong.com/v2/showcase/homepage?alias=xxxxxx
 */
public class H5Activity extends BaseActivity {
    public static final String SIGN_URL = "https://wap.koudaitong.com/v2/showcase/homepage?alias=5o8c2esh";
    /**
     * H5和原生的桥接对象
     */
    private YouzanBridge bridge;
    /**
     * WebView
     */
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h5productdetail);

        iniView();
        initBridge();
//        openWebview();
        registerYouzanUser();
    }

    /**
     * 打开有赞入口网页需先注册有赞用户
     *
     * 如果你们App的用户这个时候还没有登录, 请先跳转你们的登录页面, 然后再回来同步用户信息
     */
    private void registerYouzanUser() {
        showLoading();
        ConnectionManager.getInstance().queryMyIndexInfo(this, "" + App.getInstance().getCustId(this), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                MyIndexBean  bean = App.getInstance().getBeanFromJson(ret, MyIndexBean.class);
                setYouzanUser(bean);
            }
        });

    }

    private void setYouzanUser(MyIndexBean bean) {
        if(bean.success) {
            YouzanUser user = new YouzanUser();
            user.setUserId("" + App.getInstance().getCustId(this));
            user.setAvatar(bean.data.headImg);
            user.setGender(1);
            user.setNickName("" + bean.data.nickName);
            user.setTelephone(App.getInstance().getMoblieNo(this));
            user.setUserName("" + bean.data.nickName);

            YouzanSDK.asyncRegisterUser(user, new OnRegister() {
                /**
                 * 注册失败, 请参考错误信息修改注册参数
                 * <p/>
                 * 如报非法请求, 请检查UA是否正确
                 */
                @Override
                public void onFailed(QueryError queryError) {
                    Toast.makeText(H5Activity.this, queryError.getMsg(), Toast.LENGTH_SHORT).show();
                    dismissLoading();
                }

                /**
                 * 注册成功, 打开有赞入口网页
                 */
                @Override
                public void onSuccess() {
                    openWebview();
                }
            });
        }else{
            Toast.makeText(H5Activity.this, "" + bean.msg, Toast.LENGTH_SHORT).show();
            dismissLoading();
        }
    }


    /**
     * 初始化视图
     */
    private void iniView() {
//        LinearLayout rootView = new LinearLayout(this);
//        LinearLayout configView = new LinearLayout(this);
//        Button buttonShare = new Button(this);
//        Button buttonRefresh = new Button(this);
//        TextView tvTips=new TextView(this);
//        TitleView titleView = (TitleView) findViewById(R.id.title_view);
//        initSystemBarTint(titleView, Color.TRANSPARENT);
//        titleView.setTitle("商品详情");
//        titleView.setTitleBackVisibility(View.VISIBLE);
//        titleView.setTitleBackOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                ;
//            }
//        });
        web = (WebView)findViewById(R.id.webView_h5);

//        rootView.setOrientation(LinearLayout.VERTICAL);
//        configView.setOrientation(LinearLayout.HORIZONTAL);

//        buttonShare.setText("分享");
//        buttonRefresh.setText("刷新");
//        tvTips.setText("DEMO演示");
//        configView.addView(buttonShare);
//        configView.addView(buttonRefresh);
//        configView.addView(tvTips);
//        rootView.addView(configView);
//        rootView.addView(web, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        setContentView(rootView);

//        buttonShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (bridge != null) {//分享
//                    bridge.sharePage();
//                }
//            }
//        });
//        buttonRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (web != null) {//刷新
//                    web.reload();
//                }
//            }
//        });
    }

    /**
     * 打开链接
     */
    private void openWebview() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(SIGN_URL);
        if (url != null) {
            loadPage(url);
        }
    }


    /**
     * 初始化桥接对象
     *
     * {@link YouzanBridge}是一个桥接对象, 用于打通Html5页面和原生的交互.
     * 通过{@code YouzanBridge.build(activity, webView).create(); }的方式初始化.
     *
     * 通过{@code YouzanBridge.add(event)}可以添加桥接事件, 现在支持的事件有:
     *
     * {@link com.youzan.sdk.web.event.ShareDataEvent} => 分享事件
     * 说明:  调用{@code YouzanBridge.sharePage()}来触发
     *
     * {@link com.youzan.sdk.web.event.UserInfoEvent} => 用户同步登录事件(高阶实现)
     * 说明:  如果不使用{@code YouzanSDK.asyncRegisterUser(user);}
     *
     * {@link com.youzan.sdk.web.event.WXAppPayEvent} => 微信APP支付事件(高阶实现)
     * 说明:  需在有赞后台已经开启微信APP支付, 事件返回支付参数, 开发者使用微信SDK
     * http://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419319167&token=&lang=zh_CN
     * 实现跳转支付.
     *
     * {@link com.youzan.sdk.web.event.WebReadyEvent} =>页面初始化完成(废弃)
     *
     */
    private void initBridge(){
        bridge = YouzanBridge.build(this, web)
                .setWebClient(new WebClient())//client拓展示例, 不进行拓展可以删除这行代码
                .create();

        //根据需求添加相应的桥接事件
        bridge.add(new ShareEvent())//分享
                .add(new UserEvent());

    }


    /**
     * 加载链接
     *
     * @param url 链接, 推荐不要使用短链接(多一次重定向)
     */
    private void loadPage(String url) {
        if (web != null && !TextUtils.isEmpty(url)) {
            web.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if( url.startsWith("http:") || url.startsWith("https:") ) {
                        return false;
                    }

                    // Otherwise allow the OS to handle things like tel, mailto, etc.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dismissLoading();
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showLoading();
                }
            });
            web.loadUrl(url);

        }

    }

    /**
     * 页面回退
     *
     * bridge.pageGoBack()返回True表示处理的是网页的回退
     */
    @Override
    public void onBackPressed() {
        if (bridge == null || !bridge.pageGoBack()) {
            super.onBackPressed();
        }
    }

    /**
     * TODO-WARNING: client拓展示例, 不用可删除
     *
     * 根据具体实现扩展WebViewClient
     *
     * 但重写的时候务必记得回调父类相应的方法
     */
    private final static class WebClient extends YouzanWebClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            boolean isDeal = super.shouldOverrideUrlLoading(view, url);//记得回调父方法, 否则会造成微信支付失败
            LogUtil.d("url","url:" + url);
            if( url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }
            if (!isDeal && !TextUtils.isEmpty(url)) {//这是演示代码
                if (url.toLowerCase().startsWith("tel:")) {
                    isDeal = true;
                    Toast.makeText(getActivity(), "禁止打电话", Toast.LENGTH_SHORT).show();
                } else if (url.toLowerCase().startsWith("mailto:")) {
                    isDeal = true;
                    Toast.makeText(getActivity(), "禁止发邮件", Toast.LENGTH_SHORT).show();
                }
            }
            return isDeal;
        }
    }

}
