package com.aweiyo.zzbj;

import com.aweiyo.fragment.HomeFragment;
import com.aweiyo.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class HomeActivity extends SlidingFragmentActivity {
	private static final String LEFT_MENU_FRAGMENT = "left_menu_fragment";
	private static final String HOME_FRAGMENT = "home_fragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);//1.设置主页
		setBehindContentView(R.layout.menu_left);//1.设置侧屏栏
		SlidingMenu slidingMenu = getSlidingMenu();   
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(400);
		initFragmentView();
	}
	
	/**
	 * 初始化fragment的view 
	 */
	public void initFragmentView() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//开启事物
		transaction.replace(R.id.menu_left, new LeftMenuFragment(),LEFT_MENU_FRAGMENT);//2.
		transaction.replace(R.id.activity_home, new HomeFragment(),HOME_FRAGMENT);
		transaction.commit();
		
	}
	
	/**
	 * 拿侧屏栏的的对象：利用tag
	 */
	public LeftMenuFragment getLeftMenuFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment=(LeftMenuFragment) fm.findFragmentByTag(LEFT_MENU_FRAGMENT);
		return leftMenuFragment;
	}
	
	/**
	 * 拿主栏的的对象：利用tag
	 */
	public HomeFragment getHomeMenuFragment(){
		FragmentManager fm = getSupportFragmentManager();
		HomeFragment homeMenuFragment=(HomeFragment) fm.findFragmentByTag(HOME_FRAGMENT);
		return homeMenuFragment;
	}
	
}
