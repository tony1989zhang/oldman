package com.firstaid.oldman;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firstaid.service.HeadSetService;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.TitleView;

/**
 * Created by lenovo on 2016/1/12.
 */
public class DianLiangSettingsActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private TitleView mTitleView;
    private SeekBar mSeekBar;
    private TextView mCurrentDianLiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_dianliang_settings);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        mTitleView.setTitle(R.string.dianliang_baojing_title);
        findViewById(R.id.title_view).setVisibility(View.GONE); //取消电话号码显示
        ((TextView)findViewById(R.id.tv_name)).setText(this.getString(R.string.dianliang_name_format, App.getInstance().getCustName(this)));
        ((TextView)findViewById(R.id.tv_phone)).setText(App.getInstance().getMoblieNo(this));
        mSeekBar = (SeekBar) findViewById(R.id.dianliang_seek);
        mSeekBar.setOnSeekBarChangeListener(this);
        mCurrentDianLiang = (TextView) findViewById(R.id.current_dianliang);
        int currentDianLiangSeted = (Integer)SPUtil.getInstant(this).get("baojing_diangliang",15);
        mSeekBar.setProgress(currentDianLiangSeted);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        LogUtil.d("cx", "   onStopTrackingTouch  ");
        SPUtil.getInstant(this).save("baojing_diangliang", seekBar.getProgress());
        Intent intent = new Intent();
        intent.setClass(this, HeadSetService.class);
        intent.putExtra("ext_cmd","lowBatteryChanged");
        startService(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        LogUtil.d("cx","-------onProgressChanged  "+progress);
        mCurrentDianLiang.setText(progress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        LogUtil.d("cx","-------onStartTrackingTouch  ");
    }
}
