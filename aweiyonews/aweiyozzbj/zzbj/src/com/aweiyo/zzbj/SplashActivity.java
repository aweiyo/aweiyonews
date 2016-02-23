package com.aweiyo.zzbj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout rlRoot;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initUI();
		initData();
		startAnim();
	}

	private void startAnim() {
		// TODO Auto-generated method stub
		AnimationSet set=new AnimationSet(false);
		
		RotateAnimation rotate=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotate.setDuration(2000);
		rotate.setFillAfter(true);//保持动画的ui
		
		AlphaAnimation alpha=new AlphaAnimation(0, 1);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);
		
		ScaleAnimation scale=new ScaleAnimation(0, 1, 0, 1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		scale.setDuration(2000);
		scale.setFillAfter(true);
		
		set.addAnimation(alpha);
		set.addAnimation(scale);
		set.addAnimation(rotate);

		set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				boolean enterHome = mPref.getBoolean("enterhome", false);
				if(enterHome){
					startActivity(new Intent(SplashActivity.this,HomeActivity.class));
					finish();
				}else{
					//动画结束跳转新手引导页面
					startActivity(new Intent(SplashActivity.this,GuideActivity.class));
					finish();
					mPref.edit().putBoolean("enterhome", true).commit();
				}
			}
		});
		
//		set.start();
		//把 动画加到布局里面
		rlRoot.startAnimation(set);
	}

	private void initData() {
	}

	private void initUI() {
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}

}
