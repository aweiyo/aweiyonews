package com.aweiyo.pager;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	public void initDate() {
		System.out.println("����");
		setSlidingMenuEnable(true);
		tvTitle.setText("����");
		TextView text=new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
	}
}
