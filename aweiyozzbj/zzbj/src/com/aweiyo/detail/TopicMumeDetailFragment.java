package com.aweiyo.detail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * ר��˵�����ҳ
 * @author aweiyoo
 *
 */
public class TopicMumeDetailFragment extends BaseMenuDetailPager {

	public TopicMumeDetailFragment(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		// TODO Auto-generated method stub
		TextView text=new TextView(mActivity);
		text.setText("�˵�����ҳ--ר��");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		return text;
	}

}
