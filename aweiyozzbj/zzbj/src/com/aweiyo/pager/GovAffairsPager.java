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
		System.out.println("����");
		tvTitle.setText("����");
		TextView text=new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
	}
	
}
