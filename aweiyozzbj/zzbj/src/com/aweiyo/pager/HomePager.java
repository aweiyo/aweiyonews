package com.aweiyo.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

public class HomePager extends BasePager {

	public HomePager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView() {
		super.initView();
		
	}
	
	@Override
	public void initDate() {
		System.out.println("�ǻ۱���");
		setSlidingMenuEnable(false);
		btnMenu.setVisibility(View.INVISIBLE);
		tvTitle.setText("�ǻ۱���");
		TextView text=new TextView(mActivity);
		text.setText("��ҳ");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
	}
}
