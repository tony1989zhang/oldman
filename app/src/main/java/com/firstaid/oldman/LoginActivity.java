package com.firstaid.oldman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.bean.BaseBean;
import com.firstaid.bean.LoginBean;
import com.firstaid.service.HeadSetService;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.TitleView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "e2387ec61b54";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "c0593693a0b2750046989181cec7f8e9";

    private Button login;
    private TitleView mTitleView;
    private EditText mEditPhone;
    private Button mBtnSendCode;
    private EditText mEditCode;
    private Button mBtnVoiceCode;
    private EditText mEditInviteCode;
    private TextView mTextLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHeadSetService();
        LogUtil.d("cx", BuildConfig.custType + " onCreate ");
        if (!TextUtils.isEmpty(SPUtil.getInstant(this).get("mobileNo", "").toString())) {
            gotoMain();
            return;
        }
        setContentView(R.layout.activity_login);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(mTitleView, this.getResources().getColor(R.color.title_bg_color));
        mTitleView.setTitle(R.string.title_login);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleRightImg(R.mipmap.sandian);
        mTitleView.setTitleRightOnClickListener(this);
        login = (Button) findViewById(R.id.login_btn_lg);
        mEditPhone = (EditText) findViewById(R.id.edit_phone);
        mBtnSendCode = (Button) findViewById(R.id.btn_send_code);
        mEditCode = (EditText) findViewById(R.id.edit_code);
        mBtnVoiceCode = (Button) findViewById(R.id.btn_voice_code);
        mEditInviteCode = (EditText) findViewById(R.id.edit_invite_code);
        mTextLegal = (TextView) findViewById(R.id.text_legal);

        mTextLegal.setOnClickListener(this);
        mBtnSendCode.setOnClickListener(this);
        mBtnVoiceCode.setOnClickListener(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        /*        if(TextUtils.isEmpty(mEditPhone.getText().toString())){
                    Toast.makeText(v.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEditCode.getText().toString())){
                    Toast.makeText(v.getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("86", mEditPhone.getText().toString(), mEditCode.getText().toString());
                }*/
                login(); //Tony 暂时取消掉 注释，要运行可行的代码时候，注释掉
            }
        });
        App.getInstance().registHeadSet(this);
        initSmsSdk();
    }

    private void initHeadSetService() {
        Intent intent = new Intent();
        intent.setClass(this, HeadSetService.class);
        this.startService(intent);
    }

    private void initSmsSdk() {
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mSmsSdkHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
    }

    private Handler mSmsSdkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            LogUtil.d("cx", result + "  data   " + data);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    // textView2.setText("提交验证码成功");
                    login();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                    mCurrentSecond = 60;
                    mBtnSendCode.setText(LoginActivity.this.getString(R.string.send_sms_wait, mCurrentSecond));
                    mBtnSendCode.setEnabled(false);
                    mHandler.sendEmptyMessageDelayed(MSG_SEND_SMS_WAIT, 1000);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                ((Throwable) data).printStackTrace();
                Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void login() {
        showLoading();
        ConnectionManager.getInstance().userlogin(this, mEditPhone.getText().toString(), mEditCode.getText().toString(), mEditInviteCode.getText().toString(), mEditCode.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                LogUtil.d("cx", BuildConfig.custType + " longin ret " + ret);
                dismissLoading();
                LoginBean loginBean = App.getInstance().getBeanFromJson(ret, LoginBean.class);
                if (loginBean.success) {
                    SPUtil.getInstant(LoginActivity.this).save("custId", loginBean.custId);
                    SPUtil.getInstant(LoginActivity.this).save("mobileNo", loginBean.mobileNo);
                    SPUtil.getInstant(LoginActivity.this).save("custName", loginBean.custName);
                    gotoMain();
                } else if (!TextUtils.isEmpty(loginBean.msg)) {
                    Toast.makeText(LoginActivity.this, loginBean.msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void gotoMain() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private int mCurrentSecond;
    private static final int MSG_SEND_SMS_WAIT = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SEND_SMS_WAIT: {
                    mCurrentSecond--;
                    if (mCurrentSecond <= 0) {
                        mBtnSendCode.setText(R.string.send_code);
                        mBtnSendCode.setEnabled(true);
                    } else {
                        mBtnSendCode.setText(LoginActivity.this.getString(R.string.send_sms_wait, mCurrentSecond));
                        mBtnSendCode.setEnabled(false);
                        mHandler.sendEmptyMessageDelayed(MSG_SEND_SMS_WAIT, 1000);
                    }

                    break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back: {
                this.finish();
                ;
                break;
            }
            case R.id.title_right:
            case R.id.title_right_img: {

                break;
            }
            case R.id.btn_send_code: {
                if (TextUtils.isEmpty(mEditPhone.getText().toString())) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (mEditPhone.getText().toString().length() != 11) {
                    Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.getVerificationCode("86", mEditPhone.getText().toString());
                    showLoading();
                    ConnectionManager.getInstance().checkUser(this, mEditPhone.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            LogUtil.d("cx", "ret " + ret);
                            dismissLoading();
                            final CheckUserBean checkUser = App.getInstance().getBeanFromJson(ret, CheckUserBean.class);
                            if (checkUser.success && checkUser.status) {
                                ((View) mEditInviteCode.getParent()).setVisibility(View.GONE);
                            } else {
                                ((View) mEditInviteCode.getParent()).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                break;
            }
            case R.id.btn_voice_code: {
                if (TextUtils.isEmpty(mEditPhone.getText().toString())) {
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (mEditPhone.getText().toString().length() != 11) {
                    Toast.makeText(this, "请输入11位手机号", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.getVoiceVerifyCode("86", mEditPhone.getText().toString());
                    showLoading();
                    ConnectionManager.getInstance().checkUser(this, mEditPhone.getText().toString(), new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            LogUtil.d("cx", "ret " + ret);
                            dismissLoading();
                            final CheckUserBean checkUser = App.getInstance().getBeanFromJson(ret, CheckUserBean.class);
                            if (checkUser.success && checkUser.status) {
                                ((View) mEditInviteCode.getParent()).setVisibility(View.GONE);
                            } else {
                                ((View) mEditInviteCode.getParent()).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
                break;
            }
            case R.id.text_legal:
                Intent intent = new Intent();
                intent.setClass(v.getContext(), WebActivity.class);
                intent.putExtra("ext_title", getString(R.string.agree));
                intent.putExtra("ext_url", "http://7.77-7.com/bz/zcsm.htm");
                v.getContext().startActivity(intent);
                break;
        }
    }

    class CheckUserBean extends BaseBean {
        /**
         * 是否已注册，true：是，false：否
         */
        boolean status;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null && mHandler.hasMessages(MSG_SEND_SMS_WAIT)) {
            mHandler.removeMessages(MSG_SEND_SMS_WAIT);
        }
    }

    ProgressDialog mLoadingDialog;

    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(this);
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }

    }
}
