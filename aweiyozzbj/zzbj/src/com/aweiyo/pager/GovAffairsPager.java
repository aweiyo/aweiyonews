package com.aweiyo.pager;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void initView() {
		super.initView();
	}
	
	@Override
	public void initDate() {
		System.out.println("政务");
		tvTitle.setText("政务");
		TextView text=new TextView(mActivity);
		text.setText("政务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
	}
	
}
