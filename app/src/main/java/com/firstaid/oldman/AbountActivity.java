package com.firstaid.oldman;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firstaid.view.TitleView;

import org.w3c.dom.Text;

/**
 * Created by lenovo on 2016/1/13.
 */
public class AbountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        TitleView titleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(titleView, Color.TRANSPARENT);
        titleView.setTitleBackVisibility(View.VISIBLE);
        titleView.setTitleBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleView.setTitle(R.string.abount_title);
        TextView titleName = (TextView) findViewById(R.id.tv_title_name);
        TextView tvVN = (TextView) findViewById(R.id.versionName);
        if(BuildConfig.hasProduct){
            titleName.setText("急安宝.亲属端");
        }else{
            titleName.setText("急安宝.老人端");
        }
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            tvVN.setText("v" + versionName);
            
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
