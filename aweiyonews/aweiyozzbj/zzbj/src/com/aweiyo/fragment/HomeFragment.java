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
		rgGroup.check(R.id.rb_home);//Ĭ�ϵĹ�ѡ��ҳ��
		list = new ArrayList<BasePager>();
		list.add(new HomePager(mActivity));
		list.add(new NewsCenterPager(mActivity));
		list.add(new SmartServicePager(mActivity));
		list.add(new GovAffairsPager(mActivity));
		list.add(new SettingPager(mActivity));
		vpHome.setAdapter(new BaseAdapter());
		
		//��ʼ����һ��ҳ�档�Ѿ�������Ĭ�ϵĹ�ѡ��ҳ�棬ΪʲôҪ��ʼ���أ���Ϊÿ��ҳ�涼�ǲ�һ���ģ�Ĭ�ϵ�ֻ�Ǵ�ҵĹ���
		list.get(0).initDate();
		/**
		 * ����ҳ��ѡ��仯
		 */
		vpHome.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				list.get(arg0).initDate();// ��ȡ��ǰ��ѡ�е�ҳ��, ��ʼ����ҳ������
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
					vpHome.setCurrentItem(0,false);//����Ϊ��һ��ҳ�棬����û�л����Ķ���
					break;
				case R.id.rb_news:
					vpHome.setCurrentItem(1,false);//����Ϊ��2��ҳ�棬����û�л����Ķ���
					break;
				case R.id.rb_smart:
					vpHome.setCurrentItem(2,false);//����Ϊ��2��ҳ�棬����û�л����Ķ���
					break;
				case R.id.rb_gov:
					vpHome.setCurrentItem(3,false);//����Ϊ��2��ҳ�棬����û�л����Ķ���
					break;
				case R.id.rb_setting:
					vpHome.setCurrentItem(4,false);//����Ϊ��2��ҳ�棬����û�л����Ķ���
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
	 * ���viewpager�е��������ĵ�ҳ��
	 * @return
	 */
	public NewsCenterPager getNewsCenterPager(){
		NewsCenterPager newsCenterPager = (NewsCenterPager) list.get(1);
		return newsCenterPager;
	}
	
}
