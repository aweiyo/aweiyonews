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
	 * �ӻ�������ǵ�һ��ҳ������Ҫ���ؼ�����
	 * �󻬣���������һ��ҳ������Ҫ���ؼ�����
	 * ���»�������Ҫ���ؼ�����
	 * 
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);//���󸸿ؼ���Ҫ����
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int endX=(int) ev.getRawX();
			int endY=(int) ev.getRawY();
			
			//���һ�
			if(Math.abs(endX-startX)>Math.abs(endY-startY)){
				//�һ�
				if(endX>startX){
					if(getCurrentItem()==0){
						getParent().requestDisallowInterceptTouchEvent(false);//���󸸿ؼ�����
					}
				}else if(endX<startX){
					//��
					if(getCurrentItem()==getAdapter().getCount()-1){
						getParent().requestDisallowInterceptTouchEvent(false);//���󸸿ؼ�����
					}
				}
			}else{                      
//				���»���
				getParent().requestDisallowInterceptTouchEvent(false);//���󸸿ؼ�����
			}
			break;
			 
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	
}
