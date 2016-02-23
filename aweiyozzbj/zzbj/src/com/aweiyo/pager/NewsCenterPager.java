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
	 * �ӷ������õ�������
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
		System.out.println("����");
		setSlidingMenuEnable(true);
		tvTitle.setText("����");
		TextView text = new TextView(mActivity);
		// text.setText("����");
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

		// �����ŵ�����ҳȫ���ӽ���
		list = new ArrayList<BaseMenuDetailPager>();
		list.add(new NewsMenuDetailPager(mActivity, mData.data.get(0).children));
		list.add(new TopicMumeDetailFragment(mActivity));
		list.add(new PhotoMenuDetailFragment(mActivity,btnPhoto));//�Ѱ�ť�ؼ�����ȥʵ�ֵ���¼�
		list.add(new InteractMenuDetailFragment(mActivity));

		setCurrentDetailMenuPager(0);// ��������ҳ���Ĭ��ҳ������������ҳ
		// System.out.println(json);  
	}

	/**
	 * ���õ�ǰ�˵�����ҳ
	 */
	public void setCurrentDetailMenuPager(int position) {
		// TODO Auto-generated method stub
		// HomeActivity mainUI=(HomeActivity) mActivity;
		// HomeFragment homeMenuFragment =
		// mainUI.getHomeMenuFragment();//�õ���ҳ��fragment

		BaseMenuDetailPager pager = list.get(position);// �õ��������о��������ҳ
		fl_content.removeAllViews();
		fl_content.addView(pager.mRootView);// ������ҳ������ҳ��Ϊ���������õ�����������ҳ
		NewsMenuData newsMenuData = mData.data.get(position);
		// ���ñ��������Ų��������ĵ�����Ķ�
		tvTitle.setText(newsMenuData.title);

		pager.initData();
		
		if(pager instanceof PhotoMenuDetailFragment){
			btnPhoto.setVisibility(View.VISIBLE);
		}else{
			btnPhoto.setVisibility(View.GONE);
		}
	}
}
