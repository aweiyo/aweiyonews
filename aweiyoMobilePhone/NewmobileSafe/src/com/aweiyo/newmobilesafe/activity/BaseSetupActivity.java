package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public  abstract class BaseSetupActivity extends Activity {
	
	private GestureDetector mDetector;//手势探测器
	
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


				// 判断纵向滑动幅度是否过大, 过大的话不允许切换界面
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					Toast.makeText(BaseSetupActivity.this, "不能这样划哦!",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// 判断滑动是否过慢,abs绝对值
				if (Math.abs(velocityX) < 100) {
					Toast.makeText(BaseSetupActivity.this, "滑动的太慢了!",
							Toast.LENGTH_SHORT).show();
					return true;
				}

				// 向右划,上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// 向左划, 下一页
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

	// 点击下一页按钮
		public void next(View view) {
			showNextPage();
		}

		// 点击上一页按钮
		public void previous(View view) {
			showPreviousPage();
		}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetector.onTouchEvent(event);//将监听到的event事件交给gesturedetector处理
		return super.onTouchEvent(event);
	}
}
