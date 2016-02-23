package com.aweiyo.detail;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.domain.NewsData.NewsCenterData;
import com.aweiyo.domain.TabData;
import com.aweiyo.domain.TabData.TabNewsData;
import com.aweiyo.domain.TabData.TopNewsData;
import com.aweiyo.global.GlobalContants;
import com.aweiyo.pager.RefreshListView;
import com.aweiyo.pager.RefreshListView.OnRefreshListener;
import com.aweiyo.utils.CacheUtils;
import com.aweiyo.zzbj.NewDetailActivity;
import com.aweiyo.zzbj.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * 新闻页面中的新闻详情页
 * @author aweiyoo
 *
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener{

	private NewsCenterData mNewsCenterData; 
	private TextView text;
	private String mUrl;
	private ViewPager mViewPager;
	private TabData mTabData;
	private View view;
	private CirclePageIndicator mIndicator;
	private RefreshListView mListView;
	/**
	 * 新闻标题
	 */
	private TextView tvTitle;
	private ArrayList<TopNewsData> mTopnews;
	/**
	 * listview的新闻的数据
	 */
	private ArrayList<TabNewsData> mNews;
	private View mHeaderView;
	
	private String mMoreUrl;
	private NewsAdapter mNewsAdapter;
	
	private SharedPreferences mPref;
	
	private Handler mHandler;
	
	public TabDetailPager(Activity activity, NewsCenterData newsCenterData) {
		super(activity);
		// TODO Auto-generated constructor stub
		mNewsCenterData=newsCenterData;
		mUrl=mNewsCenterData.url;
	}

	@Override
	public View initView() {
		view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		mHeaderView = View.inflate(mActivity, R.layout.list_header_topnews, null);
		mViewPager=(ViewPager) mHeaderView.findViewById(R.id.vp_news);
		tvTitle=(TextView) mHeaderView.findViewById(R.id.tv_title);
		mIndicator=(CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);
		mListView=(RefreshListView) view.findViewById(R.id.lv_list);
		
		mViewPager.setOnPageChangeListener(this);//监听viewpager的页面滑动事件

		mListView.addHeaderView(mHeaderView);
		
		//listview实现数据的刷新接口
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if(mMoreUrl!=null){
					//加载更多数据
					getMoreDataFromServer();
				}else{
					//给用户友好提示
					Toast.makeText(mActivity, "到底了...", 0).show();
//不加这一句实验					
					mListView.onRefreshComplete(false);
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println(position);
				mPref = mActivity.getSharedPreferences("config", mActivity.MODE_PRIVATE);
				
				String nowID=mNews.get(position).id;
				//String的默认值是"",而不是null
				String read_ids=mPref.getString("read_ids", "");
				if(nowID!=null){
					if(!read_ids.contains(nowID)){
						mPref.edit().putString("read_ids", read_ids);
						read_ids=read_ids+nowID+";";
					}				
				}
				Intent intent =new Intent(mActivity,NewDetailActivity.class);
				intent.putExtra("url", mNews.get(position).url);
				mActivity.startActivity(intent);
				changeReadColor(view);
			}
		});
		
		return view;
	}
	
	/**
	 *	点击后改变新闻的颜色
	 */
	protected void changeReadColor(View view) {
		TextView tvTitle=(TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	/**
	 * 加载更多的数据
	 */
	private void getMoreDataFromServer() {
		HttpUtils utils=new HttpUtils();
		if(mMoreUrl!=null){
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result; 
				parseData(result,true);
				mListView.onRefreshComplete(true);
			}

			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, 0).show();
				mListView.onRefreshComplete(false);
			}
		});
		}
	}

	@Override
	public void initData() {
System.out.println("执行了initdate..................");		
		String cache = CacheUtils.getCache(mActivity, GlobalContants.SERVER_URI+mUrl);
		if(!TextUtils.isEmpty(cache)){
			parseData(cache, false);
System.out.println("标签页的缓存不为空..........");
System.out.println("标签页的缓存为空..........");
		}else{
			getDataFromServer();
		}
		
	}
 
	private void getDataFromServer() {
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContants.SERVER_URI+mUrl, new RequestCallBack<String>() {

			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				String result=responseInfo.result;
				System.out.println("页签详情页返回结果"+result);
				parseData(result,false);
				mListView.onRefreshComplete(true);//刷新完成后收起下拉刷新控件
				CacheUtils.setCache(mActivity, GlobalContants.SERVER_URI+mUrl, result);
				}
 
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "联网失败哦...", 0).show();
				mListView.onRefreshComplete(false);//刷新完成后收起下拉刷新控件
			}
		});
		
	}

	/***
	 * 解析服务器传过来的新闻页面的数据
	 * @param result 
	 */
	protected void parseData(String result,boolean isMore) {
		Gson gson=new Gson();
		mTabData = gson.fromJson(result, TabData.class);
		System.out.println("mTabData"+mTabData);
		//解析得到下一页的地址
		mMoreUrl = mTabData.data.more;
		if(TextUtils.isEmpty(mMoreUrl)){
			mMoreUrl=null;
		}else{
			mMoreUrl=GlobalContants.SERVER_URI+mMoreUrl;//完整的服务器路径
		}
		
		//用isMore來判斷是刷新列表还是加载下一页
		if(isMore){
			ArrayList<TabNewsData> news = mTabData.data.news;//这里获得的数据是下一页的，因为路径
			mNews.addAll(news);//将下拉刷新的listview追加到值钱的，所以现在就是一个整体。不追加，后面出来的数据就会把前面出来的数据代替，前面出来的数据就会失效
			mNewsAdapter.notifyDataSetChanged();
		}else{
			mTopnews = mTabData.data.topnews;//这里解析的数据是下拉刷新的，因为路径是下拉刷新的路径
			mNews = mTabData.data.news;
			mViewPager.setAdapter(new TopAdapter());  //设置动作发生的事件的顺序很重要
			mIndicator.setViewPager(mViewPager);
			mIndicator.setSnap(true);
			mIndicator.onPageSelected(0);//默认选择地0页
			tvTitle.setText(mTopnews.get(0).title);

			//设置listview的是适配器
			mNewsAdapter = new NewsAdapter();
			mListView.setAdapter(new NewsAdapter());
			
			// 自动轮播条显示
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();

						if (currentItem < mTopnews.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}

						mViewPager.setCurrentItem(currentItem);// 切换到下一个页面
						mHandler.sendEmptyMessageDelayed(0, 3000);// 继续延时3秒发消息,
																	// 形成循环
					};
				};

				mHandler.sendEmptyMessageDelayed(0, 3000);// 延时3秒后发消息
			}
		}
		
		
	}

	class TopAdapter extends PagerAdapter{

		private BitmapUtils utils;

		public TopAdapter(){
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTopnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;  
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView image=new ImageView(mActivity);
//			image.setImageResource(R.drawable.topnews_item_default);//设置未加载数据前默认的图片
			image.setScaleType(ScaleType.FIT_XY);//填充屏幕
			TopNewsData list = mTopnews.get(position);//拿到当前的TopNewsData对象
			utils.display(image, list.topimage);//利用xutils加载图片
			container.addView(image);
			
			image.setOnTouchListener(new TopNewsTouchListener());
			
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View)object);
		}
	}

	class TopNewsTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case MotionEvent.ACTION_CANCEL:
				mHandler.removeCallbacksAndMessages(null);//删除Handler中的所有消息
				break;
			case MotionEvent.ACTION_DOWN:
				mHandler.sendEmptyMessageDelayed(0, 3000);		
				break;
			case MotionEvent.ACTION_UP:
				mHandler.removeCallbacksAndMessages(null);//删除Handler中的所有消息
				break;

			default:
				break;
			}
			return false;
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
		TopNewsData topNewsData = mTopnews.get(arg0);
		tvTitle.setText(topNewsData.title);
	} 
	
	class NewsAdapter extends BaseAdapter{

		private BitmapUtils utils;

		//利用xutils加载图片
		public NewsAdapter(){
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_news_item, null);
				viewHolder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
				viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
				viewHolder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			TabNewsData tabNewsData = (TabNewsData) getItem(position);
			viewHolder.tvTitle.setText(tabNewsData.title);
			viewHolder.tvDate.setText(tabNewsData.pubdate);
			utils.display(viewHolder.ivPic, tabNewsData.listimage);//加载图片资源
			return convertView;
		}
	}
	static  class ViewHolder{
		public ImageView ivPic;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
