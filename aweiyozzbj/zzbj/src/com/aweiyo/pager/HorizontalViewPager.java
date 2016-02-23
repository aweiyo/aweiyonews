package com.aweiyo.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/***
 * ˮƽ������viewpager�����ؼ�û�����ػ����¼���viewpager
 * @author aweiyoo
 *
 */
public class HorizontalViewPager extends ViewPager {

	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HorizontalViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(getCurrentItem()!=0){
		getParent().requestDisallowInterceptTouchEvent(true);
		}else{
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}
}
