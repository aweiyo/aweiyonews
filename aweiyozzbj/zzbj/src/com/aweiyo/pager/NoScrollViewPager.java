package com.aweiyo.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * onInterceptTouchEvent()�����ڴ����¼���������Ԥ������ȻҲ���Բ��������ı��¼��Ĵ��ݷ���
	 * Ҳ���Ǿ����Ƿ�����Touch�¼��������£��ӿؼ������ݣ�һ������True�������¼��ڵ�ǰ��viewGroup�лᱻ������
	 * �����´���֮·���ضϣ������ӿؼ���û�л������Touch�¼�����ͬʱ���¼����ݸ���ǰ�Ŀؼ���onTouchEvent()����
	 * ����false������¼������ӿؼ���onInterceptTouchEvent()
	 */
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ��ֹ�������viewpager�Ļ����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
