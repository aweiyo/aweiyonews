package com.aweiyo.zzbj;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class GuideActivity extends Activity {

	private ViewPager vpGuide;
	
	private static final int[] mImagesId=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

	private List<ImageView> list;

	private Button btnStart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		
	}

	private void initUI() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		btnStart = (Button) findViewById(R.id.btn_start);
		list = new ArrayList<ImageView>();
		for (int imageID : mImagesId) {  
			ImageView imageView=new ImageView(GuideActivity.this);
			imageView.setBackgroundResource(imageID);    
			list.add(imageView);
		}
		
		MyPagerAdapter adapter=new MyPagerAdapter();
		vpGuide.setAdapter(adapter);
		//监Viewpager页面的变化
		vpGuide.setOnPageChangeListener(new MyOnPagerChangeListener());
		
	}
	
	class MyOnPagerChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			if(position==mImagesId.length-1){
				btnStart.setVisibility(View.VISIBLE);
			}else{
				btnStart.setVisibility(View.INVISIBLE);
			}
			
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}}
	
	
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImagesId.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}

		
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
//			super.destroyItem(container, position, object);
		}
		
	}
	
	
	/**
	 * 点击"开始体验".跳转主页
	 */
	public void enterHome(View view){
		startActivity(new Intent(GuideActivity.this,HomeActivity.class));
		finish();
	}
	
}
