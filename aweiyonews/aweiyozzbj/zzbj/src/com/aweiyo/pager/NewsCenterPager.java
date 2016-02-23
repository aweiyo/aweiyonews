package com.aweiyo.pager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.provider.ContactsContract.Contacts.Data;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.detail.BaseMenuDetailPager;
import com.aweiyo.detail.InteractMenuDetailFragment;
import com.aweiyo.detail.NewsMenuDetailPager;
import com.aweiyo.detail.PhotoMenuDetailFragment;
import com.aweiyo.detail.TopicMumeDetailFragment;
import com.aweiyo.domain.NewsData;
import com.aweiyo.domain.NewsData.NewsMenuData;
import com.aweiyo.fragment.HomeFragment;
import com.aweiyo.fragment.LeftMenuFragment;
import com.aweiyo.global.GlobalContants;
import com.aweiyo.utils.CacheUtils;
import com.aweiyo.zzbj.HomeActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NewsCenterPager extends BasePager {

	private List<BaseMenuDetailPager> list;
	private HomeActivity mainUI;
	/**
	 * 从服务器拿到的数据
	 */
	private NewsData mData;

	public NewsCenterPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	public void initDate() {
		System.out.println("新闻");
		setSlidingMenuEnable(true);
		tvTitle.setText("新闻");
		TextView text = new TextView(mActivity);
		// text.setText("新闻");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		fl_content.addView(text);
		String cache = CacheUtils.getCache(mActivity,
				GlobalContants.CATEGORIES_URI);
		if (!TextUtils.isEmpty(cache)) { 
			parseData(cache);
		} else {
			getDateFromServer();
		}
	}

	private void getDateFromServer() {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, GlobalContants.CATEGORIES_URI,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// System.out.println(responseInfo.result);
						String data = responseInfo.result;
						parseData(data);
						CacheUtils.setCache(mActivity, GlobalContants.CATEGORIES_URI, data);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, msg, 0).show();
					}
				});
	}

	protected void parseData(String data) {
		Gson gson = new Gson();
		mData = gson.fromJson(data, NewsData.class);
		// Log.v("mData.data.get(0).children",
		// mData.data.get(0).children.toString());
		mainUI = (HomeActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
		leftMenuFragment.setMenuData(mData);

		// 将新闻的详情页全部加进来
		list = new ArrayList<BaseMenuDetailPager>();
		list.add(new NewsMenuDetailPager(mActivity, mData.data.get(0).children));
		list.add(new TopicMumeDetailFragment(mActivity));
		list.add(new PhotoMenuDetailFragment(mActivity,btnPhoto));//把按钮控件传过去实现点击事件
		list.add(new InteractMenuDetailFragment(mActivity));

		setCurrentDetailMenuPager(0);// 设置新闻页面的默认页面是新闻详情页
		// System.out.println(json);  
	}

	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentDetailMenuPager(int position) {
		// TODO Auto-generated method stub
		// HomeActivity mainUI=(HomeActivity) mActivity;
		// HomeFragment homeMenuFragment =
		// mainUI.getHomeMenuFragment();//拿到主页的fragment

		BaseMenuDetailPager pager = list.get(position);// 拿到侧屏栏中具体的详情页
		fl_content.removeAllViews();
		fl_content.addView(pager.mRootView);// 设置主页的新闻页面为侧屏栏中拿到的新闻详情页
		NewsMenuData newsMenuData = mData.data.get(position);
		// 设置标题栏随着侧屏屏栏的点击而改动
		tvTitle.setText(newsMenuData.title);

		pager.initData();
		
		if(pager instanceof PhotoMenuDetailFragment){
			btnPhoto.setVisibility(View.VISIBLE);
		}else{
			btnPhoto.setVisibility(View.GONE);
		}
	}
}
