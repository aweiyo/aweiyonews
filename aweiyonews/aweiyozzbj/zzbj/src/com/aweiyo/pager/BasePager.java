package com.aweiyo.pager;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aweiyo.zzbj.HomeActivity;
import com.aweiyo.zzbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BasePager {

	public Activity mActivity;
	public View view;
	public TextView tvTitle;
	public FrameLayout fl_content;
	public ImageView btnMenu;
	public ImageButton btnPhoto;

	public BasePager(Activity activity) {
		mActivity = activity;
		initView();
//		initDate(); ����������������ݣ������ڰ�ҳ����뼯�ϵ�ʱ��ͻ�ȫ���ĳ�ʼ��������������Ҫ�����û�����������Ǿͳ�ʼ������
	}

	public void initDate() {
	}

	public void initView() {
		view = View.inflate(mActivity, R.layout.base_pager, null);
		btnMenu = (ImageView) view.findViewById(R.id.btn_menu);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
		btnPhoto=(ImageButton) view.findViewById(R.id.btn_photo);
		btnMenu.setOnClickListener(new OnClickListener() {
			
			//���image��ͼ�꣬��ʾ��������
			@Override
			public void onClick(View v) {
				HomeActivity activity=(HomeActivity) mActivity;
				SlidingMenu menu = activity.getSlidingMenu();
				menu.toggle();
			}
		});
	}

	public void setSlidingMenuEnable(boolean enable) {
		HomeActivity mainUI = (HomeActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		}
	}
}
