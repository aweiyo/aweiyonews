package com.aweiyo.pager;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

public class SmartServicePager extends BasePager {

	public SmartServicePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	public void initDate() {
		System.out.println("�ǻ۷���");
		setSlidingMenuEnable(true);
		tvTitle.setText("�ǻ۷���");
		TextView text=new TextView(mActivity);
		text.setText("�ǻ۷���");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
	}

}
