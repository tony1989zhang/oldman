package com.firstaid.oldman;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firstaid.bean.BaseBean;
import com.firstaid.fragment.AcountListFragment;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.view.TitleView;

public class AddAccActivity extends BaseActivity implements View.OnClickListener, ConnectionUtil.OnDataLoadEndListener {
    TitleView mTitleView;
    public static boolean mAddACC = false;
    public static final String EXID_ADD_ACC = "EXID_ADD_ACC";
    EditText accType;
    EditText accNo;
    EditText accNo_blank;
    EditText openBank;
    EditText accName;
    public static int ACC_TYPE;
    View blankll;
    public String defaultMode = "Y";
    String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acc);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitle("添加账户");
        super.initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitleBackOnClickListener(this);
        mTitleView.setTitleBackVisibility(View.VISIBLE);

        Button subMit = (Button) findViewById(R.id.btn_submit);
        subMit.setOnClickListener(this);
        accType = (EditText) findViewById(R.id.accType);
        accType.setOnClickListener(this);
        accNo = (EditText) findViewById(R.id.accNo);
        accNo_blank = (EditText) findViewById(R.id.accNo_blank);
        openBank = (EditText) findViewById(R.id.openBank);
        accName = (EditText) findViewById(R.id.accName);
        CheckBox setDefaultBtn = (CheckBox) findViewById(R.id.setDefault);
        setDefaultBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    defaultMode = "Y";
                else
                    defaultMode = "N";
            }
        });
        blankll = findViewById(R.id.linearlayout_blank);
        if (null != this.getIntent() && this.getIntent().hasExtra(EXID_ADD_ACC)) {
            mAddACC = getIntent().getBooleanExtra(EXID_ADD_ACC, false);
        }
        setAccNoTypeEquest4();
        if (mAddACC) {
            mTitleView.setTitle("修改账户");
            AcountListFragment.AccountDataBean accountDataBean = (AcountListFragment.AccountDataBean) getIntent().getSerializableExtra(AcountListFragment.UPDATE_ACCNO);
            mId = accountDataBean.id;
            int updateAccType = Integer.valueOf(accountDataBean.acc_type);
            String dfflag = accountDataBean.default_flag;
            if ("Y".equals(dfflag)) {
                setDefaultBtn.setChecked(true);
            } else {
                setDefaultBtn.setChecked(false);
            }
            if (updateAccType == 4) {
                ACC_TYPE = 4;
                blankll.setVisibility(View.VISIBLE);
                accNo.setVisibility(View.GONE);
                accType.setText("银联支付");
                accNo_blank.setText(accountDataBean.acc_no);
                accName.setText(accountDataBean.acc_name);
                openBank.setText(accountDataBean.open_bank);
            } else {
                blankll.setVisibility(View.GONE);
                accNo.setVisibility(View.VISIBLE);
                if (updateAccType == 0) {
                    ACC_TYPE = 0;
                    accType.setText("支付宝支付");
                } else if (updateAccType == 1) {
                    ACC_TYPE = 1;
                    accType.setText("财付通");
                } else if (updateAccType == 3) {
                    ACC_TYPE = 3;
                    accType.setText("微信支付");
                }
                accNo.setText(accountDataBean.acc_no);
            }
        }
    }


    //当设置为银行卡的时候
    private void setAccNoTypeEquest4() {
//            accNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        accNo_blank.setInputType(InputType.TYPE_CLASS_NUMBER);
        accNo_blank.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;
            private int maxLen = 23;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;


                Editable editable = accNo_blank.getText();
                int len = editable.length();

                if (len > maxLen) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxLen);
                    accNo_blank.setText(newStr);
                    editable = accNo_blank.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isChanged) {
                    location = accNo_blank.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if (index % 5 == 4) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    accNo_blank.setText(str);
                    Editable etable = accNo_blank.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.btn_submit:
                Toast.makeText(this, "点击效果", Toast.LENGTH_SHORT).show();
                submit();
                break;
            case R.id.accType:
                Toast.makeText(this, "点击效果", Toast.LENGTH_SHORT).show();
                showDialog();
                break;
        }

    }

    private void submit() {
        showLoading();
        String accNoStr = "";
        if (ACC_TYPE == 4) {
            if (TextUtils.isEmpty(accNo_blank.getText()) || null == accNo_blank.getText()) {
                Toast.makeText(AddAccActivity.this, "银行账号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(openBank.getText()) || null == openBank.getText()) {
                Toast.makeText(AddAccActivity.this, "开户行不能为空", Toast.LENGTH_SHORT).show();
                openBank.setError("开户行不能为空");
                return;
            }
            if (TextUtils.isEmpty(accName.getText()) || null == accName.getText()) {
                Toast.makeText(AddAccActivity.this, "开户名不能为空", Toast.LENGTH_SHORT).show();
                accName.setError("开户名不能为空");
                return;
            }
            accNoStr = accNo_blank.getText().toString();
            accNoStr = accNoStr.replace(" ", "");
        } else {
            accNo.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
            if (TextUtils.isEmpty(accNo.getText()) || null == accNo.getText()) {
                Toast.makeText(AddAccActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                accNo.setError("账号不能为空");
                return;
            }
            accNoStr = accNo.getText().toString();
        }
        if (mAddACC) {
            ConnectionManager.getInstance().updateWithdrawAcc(this, "" + mId,""+ ACC_TYPE, openBank.getText().toString(), accName.getText().toString(), accNoStr, defaultMode, this);
        } else {
            ConnectionManager.getInstance().insertWithDrawacc(this, "" + App.getInstance().getCustId(this), "" + ACC_TYPE, openBank.getText().toString(), accName.getText().toString(), accNoStr, defaultMode, this);
        }
    }

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.pay_user_change_layout,
                null);

        Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
//        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = -380;//getWindowManager().getDefaultDisplay().getHeight()
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        final Dialog dialog12 = dialog;

        view.findViewById(R.id.pay_ali).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accType.setText("支付宝支付");
                ACC_TYPE = 0;
                blankll.setVisibility(View.GONE);
                accNo.setVisibility(View.VISIBLE);
                dialog12.dismiss();
            }
        });
        view.findViewById(R.id.tx_cwt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accType.setText("财付通");
                ACC_TYPE = 1;
                blankll.setVisibility(View.GONE);
                accNo.setVisibility(View.VISIBLE);
                dialog12.dismiss();
            }
        });
        view.findViewById(R.id.pay_wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accType.setText("微信支付");
                ACC_TYPE = 3;
                blankll.setVisibility(View.GONE);
                accNo.setVisibility(View.VISIBLE);
                dialog12.dismiss();
            }
        });
        view.findViewById(R.id.pay_blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accType.setText("银联支付");
                ACC_TYPE = 4;
                blankll.setVisibility(View.VISIBLE);
                accNo.setVisibility(View.GONE);
                dialog12.dismiss();
            }
        });
        accNo.setText("");
        accName.setText("");
        openBank.setText("");
    }

    @Override
    public void OnLoadEnd(String ret) {
        dismissLoading();
        Toast.makeText(this, "点击效果:" + ret, Toast.LENGTH_SHORT).show();
        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        if(baseBean.success){
            setResult(RESULT_OK);
            finish();
        }
        Toast.makeText(this, "" + baseBean.msg, Toast.LENGTH_SHORT).show();
    }
}
