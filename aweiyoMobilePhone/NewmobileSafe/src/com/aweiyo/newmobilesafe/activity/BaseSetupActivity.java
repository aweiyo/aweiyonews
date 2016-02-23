package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public  abstract class BaseSetupActivity extends Activity {
	
	private GestureDetector mDetector;//����̽����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDetector=new GestureDetector(this, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {


				// �ж����򻬶������Ƿ����, ����Ļ��������л�����
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					Toast.makeText(BaseSetupActivity.this, "����������Ŷ!",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// �жϻ����Ƿ����,abs����ֵ
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "������̫����!",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// ���һ�,��һҳ
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// ����, ��һҳ
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	public abstract  void showNextPage() ;

	public abstract void  showPreviousPage();

	// �����һҳ��ť
		public void next(View view) {
			showNextPage();
		}

		// �����һҳ��ť
		public void previous(View view) {
			showPreviousPage();
		}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);//����������event�¼�����gesturedetector����
		return super.onTouchEvent(event);
	}
}
