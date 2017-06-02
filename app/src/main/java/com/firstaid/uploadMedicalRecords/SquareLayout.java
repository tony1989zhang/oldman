//			                  _oo0oo_
//			                 o8888888o
//			                 88" . "88
//			                 (| -_- |)
//						     0\  =  /0
//						   ___/`___'\___
//						 .' \\|     |// '.
//						/ \\|||  :  |||// \
//					   / _||||| -:- |||||_ \
//					  |   | \\\  _  /// |   |
//					  | \_|  ''\___/''  |_/ |
//					  \  .-\__  '_'  __/-.  /
//					___'. .'  /--.--\  '. .'___
//				  ."" '<  .___\_<|>_/___. '>' "".
//			   | | :  `_ \`.;` \ _ / `;.`/ - ` : | |
//			   \ \  `_.   \_ ___\ /___ _/   ._`  / /
//			====`-.____` .__ \_______/ __. -` ___.`====
//							 `=-----='
//         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                    佛祖保佑           永无BUG
//                   
//                     Power   By    4evercai
//         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package com.firstaid.uploadMedicalRecords;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @ClassName: SquareLayout
 * @Description: TODO
 * @author 4evercai
 * @date 2014年8月27日 上午11:15:16
 * 
 */

public class SquareLayout extends RelativeLayout {

	public SquareLayout(Context context) {
		super(context);
	}

	public SquareLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@SuppressWarnings("unused")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// For simple implementation, or internal size is always 0.
		// We depend on the container to specify the layout size of
		// our view. We can't really know what it is since we will be
		// adding and removing different arbitrary views and do not
		// want the layout to change as this happens.
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));

		// Children are just made to fill our space.
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		// 高度和宽度一样
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
				MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
