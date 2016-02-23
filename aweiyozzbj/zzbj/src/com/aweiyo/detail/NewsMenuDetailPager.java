package com.aweiyo.detail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.MailTo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aweiyo.domain.NewsData.NewsCenterData;
import com.aweiyo.zzbj.HomeActivity;
import com.aweiyo.zzbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

public class NewsMenuDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{

	/**
	 * װ��������ҳ�ļ���
	 */
	private ArrayList<TabDetailPager> mPagers;
	
	/**
	 * ��������������children������
	 */
	private List<NewsCenterData>  mNewsTabData;

	private ViewPager mViewPager;

	private TabPageIndicator mIndicator;

	private ImageView btnNext;
	
	public NewsMenuDetailPager(Activity activity,List<NewsCenterData> children) {
		super(activity);
		mNewsTabData=new ArrayList<NewsCenterData>();
		mNewsTabData=children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_menu_detail, null);
		mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		btnNext = (ImageView) view.findViewById(R.id.btn_next);
		/**
		 *��pagerIndicator�ڵ�ʱ�����Ҫ��pagerIndicator��������viewpager�������¼�
		 *����ViewpagerIndicator����һҳ�����¼�
		 */
		mIndicator.setOnPageChangeListener(this);
		return view;
	}
	
	@Override
	public void initData() {
		mPagers=new ArrayList<TabDetailPager>();
		for(int i=0;i<mNewsTabData.size();i++){
			TabDetailPager tabDetailMenuDetailPager=new TabDetailPager(mActivity,mNewsTabData.get(i));
			mPagers.add(tabDetailMenuDetailPager);
		}
		mViewPager.setAdapter(new MenuDetailPagerAdapter());
		mIndicator.setViewPager(mViewPager);//������viewpager������adapter֮����ܵ���
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//���viewpager��ǰ��ҳ��
				int currentItem = mViewPager.getCurrentItem();
				//����Ϊ��һ��ҳ��
				mViewPager.setCurrentItem(++currentItem);
			}
		});
	}
	
	class MenuDetailPagerAdapter extends PagerAdapter{

		/**
		 * ���ر��⣬����TabDetailPager�ı�������
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mNewsTabData.get(position).title;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mPagers.get(position);
			container.addView(pager.mRootView);
			pager.initData();
			return pager.mRootView;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		HomeActivity mainUI=(HomeActivity) mActivity;
		SlidingMenu menu = mainUI.getSlidingMenu();
		if(arg0==0){
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
	}
	
	
	
}
