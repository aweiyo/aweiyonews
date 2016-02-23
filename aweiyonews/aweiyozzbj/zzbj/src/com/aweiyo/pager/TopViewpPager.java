package com.aweiyo.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopViewpPager extends ViewPager {

	private int startX;
	private int startY;


	public TopViewpPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopViewpPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	/****
	 * 佑滑：如果是第一个页面则需要父控件拦截
	 * 左滑：如果是最后一个页面则需要父控件拦截
	 * 上下滑动：需要父控件拦截
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不要拦截
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX=(int) ev.getRawX();
			int endY=(int) ev.getRawY();
			
			//左右滑
			if(Math.abs(endX-startX)>Math.abs(endY-startY)){
				//右滑
				if(endX>startX){
					if(getCurrentItem()==0){
						getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截
					}
				}else if(endX<startX){
					//左滑
					if(getCurrentItem()==getAdapter().getCount()-1){
						getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截
					}
				}
			}else{                      
//				上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截
			}
			break;
			 
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	
}
