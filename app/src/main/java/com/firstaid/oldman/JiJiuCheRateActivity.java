package com.firstaid.oldman;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.bean.BaseBean;
import com.firstaid.bean.HelpRecordDataBean;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;

/**
 * Created by lenovo on 2016/1/20.
 */
public class JiJiuCheRateActivity extends BaseActivity {

    HelpRecordDataBean mHelpRecordDataBean;
    RatingBar mRatingBar;
    TextView mRateingTv;
    EditText mContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jijiuche_rate);
        if(getIntent() == null || !getIntent().hasExtra("ext_item")){
            finish();
            return;
        }
        mHelpRecordDataBean = (HelpRecordDataBean)getIntent().getSerializableExtra("ext_item");
        ((TextView)findViewById(R.id.item_name)).setText(mHelpRecordDataBean.rescueName);
        ((TextView)findViewById(R.id.item_time)).setText(mHelpRecordDataBean.aEvaluateTime);
        ((TextView)findViewById(R.id.item_no)).setText(mHelpRecordDataBean.ambulanceNo);
        findViewById(R.id.item_rate).setVisibility(View.INVISIBLE);
        findViewById(R.id.item_content).setVisibility(View.INVISIBLE);
        mRatingBar = (RatingBar) findViewById(R.id.rate);
        mRateingTv = (TextView) findViewById(R.id.item_rate_tv);
        mContent = (EditText) findViewById(R.id.content);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRateingTv.setText(JiJiuCheRateActivity.this.getString(R.string.rate_format,""+rating));
            }
        });
        mRatingBar.setMax(5);
        mRatingBar.setRating(4);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContent.getText()== null || TextUtils.isEmpty(mContent.getText().toString())){
                    Toast.makeText(v.getContext(),R.string.empty_rate,Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoading();
                ConnectionManager.getInstance().evaluationHelp(JiJiuCheRateActivity.this, "" + App.getInstance().getCustId(JiJiuCheRateActivity.this), mHelpRecordDataBean.helpId, "1"
                        , mContent.getText().toString(), "" + mRatingBar.getRating(), new ConnectionUtil.OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                        dismissLoading();
                        BaseBean bean = App.getInstance().getBeanFromJson(ret,BaseBean.class);
                        Toast.makeText(JiJiuCheRateActivity.this, bean.msg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
