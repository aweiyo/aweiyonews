package com.aweiyo.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.aweiyo.pager.BasePager;
import com.aweiyo.pager.GovAffairsPager;
import com.aweiyo.pager.HomePager;
import com.aweiyo.pager.NewsCenterPager;
import com.aweiyo.pager.SettingPager;
import com.aweiyo.pager.SmartServicePager;
import com.aweiyo.zzbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HomeFragment extends BaseFragment {
	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;

	@ViewInject(R.id.vp_home)
	private ViewPager vpHome;

	private List<BasePager> list;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_home, null);
		// rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initDate() {
		rgGroup.check(R.id.rb_home);//默认的勾选的页面
		list = new ArrayList<BasePager>();
		list.add(new HomePager(mActivity));
		list.add(new NewsCenterPager(mActivity));
		list.add(new SmartServicePager(mActivity));
		list.add(new GovAffairsPager(mActivity));
		list.add(new SettingPager(mActivity));
		vpHome.setAdapter(new BaseAdapter());
		
		//初始化第一个页面。已经设置了默认的勾选的页面，为什么要初始化呢？因为每个页面都是不一样的，默认的只是大家的共性
		list.get(0).initDate();
		/**
		 * 监听页面选择变化
		 */
		vpHome.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				list.get(arg0).initDate();// 获取当前被选中的页面, 初始化该页面数据
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					vpHome.setCurrentItem(0,false);//设置为第一个页面，并且没有滑动的动画
					break;
				case R.id.rb_news:
					vpHome.setCurrentItem(1,false);//设置为第2个页面，并且没有滑动的动画
					break;
				case R.id.rb_smart:
					vpHome.setCurrentItem(2,false);//设置为第2个页面，并且没有滑动的动画
					break;
				case R.id.rb_gov:
					vpHome.setCurrentItem(3,false);//设置为第2个页面，并且没有滑动的动画
					break;
				case R.id.rb_setting:
					vpHome.setCurrentItem(4,false);//设置为第2个页面，并且没有滑动的动画
					break;
				default:
					break;
				}
			}
		});
	}

	class BaseAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = list.get(position);
			container.addView(pager.view);
			return pager.view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	/**
	 * 获得viewpager中的新闻中心的页面
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager(){
		NewsCenterPager newsCenterPager = (NewsCenterPager) list.get(1);
		return newsCenterPager;
	}
	
}
