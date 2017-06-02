package com.firstaid.uploadMedicalRecords;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * @author Tsai
 * @文件名称：ScrollGridView.java
 * @所在包名： com.i4evercai.kit.View
 * @文件描述： 嵌套ScrollView时可正常显示
 * @创建时间： 2014-4-29 下午3:53:01
 * @文件版本： V1.0
 */
public class ScrollGridView extends GridView {

	public ScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollGridView(Context context) {
		super(context);
	}

	public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
