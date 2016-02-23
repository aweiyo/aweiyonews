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
 * ����ҳ���е���������ҳ
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
	 * ���ű���
	 */
	private TextView tvTitle;
	private ArrayList<TopNewsData> mTopnews;
	/**
	 * listview�����ŵ�����
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
		
		mViewPager.setOnPageChangeListener(this);//����viewpager��ҳ�滬���¼�

		mListView.addHeaderView(mHeaderView);
		
		//listviewʵ�����ݵ�ˢ�½ӿ�
		mListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadMore() {
				if(mMoreUrl!=null){
					//���ظ�������
					getMoreDataFromServer();
				}else{
					//���û��Ѻ���ʾ
					Toast.makeText(mActivity, "������...", 0).show();
//������һ��ʵ��					
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
				//String��Ĭ��ֵ��"",������null
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
	 *	�����ı����ŵ���ɫ
	 */
	protected void changeReadColor(View view) {
		TextView tvTitle=(TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	/**
	 * ���ظ��������
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
System.out.println("ִ����initdate..................");		
		String cache = CacheUtils.getCache(mActivity, GlobalContants.SERVER_URI+mUrl);
		if(!TextUtils.isEmpty(cache)){
			parseData(cache, false);
System.out.println("��ǩҳ�Ļ��治Ϊ��..........");
System.out.println("��ǩҳ�Ļ���Ϊ��..........");
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
				System.out.println("ҳǩ����ҳ���ؽ��"+result);
				parseData(result,false);
				mListView.onRefreshComplete(true);//ˢ����ɺ���������ˢ�¿ؼ�
				CacheUtils.setCache(mActivity, GlobalContants.SERVER_URI+mUrl, result);
				}
 
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "����ʧ��Ŷ...", 0).show();
				mListView.onRefreshComplete(false);//ˢ����ɺ���������ˢ�¿ؼ�
			}
		});
		
	}

	/***
	 * ����������������������ҳ�������
	 * @param result 
	 */
	protected void parseData(String result,boolean isMore) {
		Gson gson=new Gson();
		mTabData = gson.fromJson(result, TabData.class);
		System.out.println("mTabData"+mTabData);
		//�����õ���һҳ�ĵ�ַ
		mMoreUrl = mTabData.data.more;
		if(TextUtils.isEmpty(mMoreUrl)){
			mMoreUrl=null;
		}else{
			mMoreUrl=GlobalContants.SERVER_URI+mMoreUrl;//�����ķ�����·��
		}
		
		//��isMore���Д���ˢ���б��Ǽ�����һҳ
		if(isMore){
			ArrayList<TabNewsData> news = mTabData.data.news;//�����õ���������һҳ�ģ���Ϊ·��
			mNews.addAll(news);//������ˢ�µ�listview׷�ӵ�ֵǮ�ģ��������ھ���һ�����塣��׷�ӣ�������������ݾͻ��ǰ����������ݴ��棬ǰ����������ݾͻ�ʧЧ
			mNewsAdapter.notifyDataSetChanged();
		}else{
			mTopnews = mTabData.data.topnews;//�������������������ˢ�µģ���Ϊ·��������ˢ�µ�·��
			mNews = mTabData.data.news;
			mViewPager.setAdapter(new TopAdapter());  //���ö����������¼���˳�����Ҫ
			mIndicator.setViewPager(mViewPager);
			mIndicator.setSnap(true);
			mIndicator.onPageSelected(0);//Ĭ��ѡ���0ҳ
			tvTitle.setText(mTopnews.get(0).title);

			//����listview����������
			mNewsAdapter = new NewsAdapter();
			mListView.setAdapter(new NewsAdapter());
			
			// �Զ��ֲ�����ʾ
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = mViewPager.getCurrentItem();

						if (currentItem < mTopnews.size() - 1) {
							currentItem++;
						} else {
							currentItem = 0;
						}

						mViewPager.setCurrentItem(currentItem);// �л�����һ��ҳ��
						mHandler.sendEmptyMessageDelayed(0, 3000);// ������ʱ3�뷢��Ϣ,
																	// �γ�ѭ��
					};
				};

				mHandler.sendEmptyMessageDelayed(0, 3000);// ��ʱ3�����Ϣ
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
//			image.setImageResource(R.drawable.topnews_item_default);//����δ��������ǰĬ�ϵ�ͼƬ
			image.setScaleType(ScaleType.FIT_XY);//�����Ļ
			TopNewsData list = mTopnews.get(position);//�õ���ǰ��TopNewsData����
			utils.display(image, list.topimage);//����xutils����ͼƬ
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
				mHandler.removeCallbacksAndMessages(null);//ɾ��Handler�е�������Ϣ
				break;
			case MotionEvent.ACTION_DOWN:
				mHandler.sendEmptyMessageDelayed(0, 3000);		
				break;
			case MotionEvent.ACTION_UP:
				mHandler.removeCallbacksAndMessages(null);//ɾ��Handler�е�������Ϣ
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

		//����xutils����ͼƬ
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
			utils.display(viewHolder.ivPic, tabNewsData.listimage);//����ͼƬ��Դ
			return convertView;
		}
	}
	static  class ViewHolder{
		public ImageView ivPic;
		public TextView tvTitle;
		public TextView tvDate;
	}

}
