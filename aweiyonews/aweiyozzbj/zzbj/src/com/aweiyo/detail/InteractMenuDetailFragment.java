package com.aweiyo.detail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * ��������ҳ
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
		text.setText("�˵�����ҳ--����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
