package com.aweiyo.detail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 互动详情页
 * @author aweiyoo
 *
 */
public class InteractMenuDetailFragment extends BaseMenuDetailPager {

	public InteractMenuDetailFragment(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		TextView text=new TextView(mActivity);
		text.setText("菜单详情页--互动");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
