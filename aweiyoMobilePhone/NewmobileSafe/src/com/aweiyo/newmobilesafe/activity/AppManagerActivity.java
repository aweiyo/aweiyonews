package com.aweiyo.newmobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.bean.AppInfo;
import com.aweiyo.newmobilesafe.engine.AppInfos;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

@SuppressLint("NewApi")
public class AppManagerActivity extends Activity {
	//使用注解来获取id
	@ViewInject(R.id.list_view)
	private ListView listview;
	@ViewInject(R.id.tv_rom)
	private TextView tvRom;
	@ViewInject(R.id.tv_sd)
	private TextView tvSD;
	@ViewInject(R.id.tv_app)
	private TextView tvApp;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfo;
	private List<AppInfo> systemAppInfo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		initUI();
		//获取并显示剩余的空间
		getSpace();
		initDate();
	}

	private  Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			AppManagerAdapter adapter=new AppManagerAdapter();
			listview.setAdapter(adapter);
		};
	};
	
	
	class AppManagerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfo.size()+systemAppInfo.size()+2;
		}

		@Override
		public Object getItem(int position) {
			if(position==0&&position==userAppInfo.size()+1){
			return null;
			}else{
				AppInfo info=null;
//				if(position<userAppInfo.size()+1){
//					info=userAppInfo.get(position-1);
//				}else {
//					int location=userAppInfo.size()+2; 
//					info=systemAppInfo.get(position-location);
//				}
//				
				  if (position < userAppInfo.size() + 1) {
		                //把多出来的特殊的条目减掉
					  info = userAppInfo.get(position - 1);

		            } else {

		                int location = userAppInfo.size() + 2;

		                info = systemAppInfo.get(position - location);
		            }
				  return info;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(position==0){
				TextView textView=new TextView(AppManagerActivity.this);
				textView.setText("用户程序"+"("+userAppInfo.size()+")");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			}else if(position==userAppInfo.size()+1){
				TextView textView=new TextView(AppManagerActivity.this);
				textView.setText("系统程序"+"("+systemAppInfo.size()+")");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			}
			
	
			View view=null;
			ViewHolder viewHolder=null;
			//convertView是复用
			if(convertView!=null && convertView instanceof LinearLayout){
				view=convertView;
				viewHolder=(ViewHolder) convertView.getTag();
			}else{
				view=View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);
				viewHolder=new ViewHolder();
				viewHolder.icon=(ImageView) view.findViewById(R.id.iv_icon);
				viewHolder.tvApkSize=(TextView) view.findViewById(R.id.tv_apk_size);
				viewHolder.tvLocation=(TextView) view.findViewById(R.id.tv_location);
				viewHolder.tvName=(TextView) view.findViewById(R.id.tv_name);
				view.setTag(viewHolder);
			}
			
			AppInfo info=null;
//			if(position<userAppInfo.size()+1){
//				info=userAppInfo.get(position-1);
//			}else {
//				int location=userAppInfo.size()+2; 
//				info=systemAppInfo.get(position-location);
//			}
//			
			  if (position < userAppInfo.size() + 1) {
	                //把多出来的特殊的条目减掉
				  info = userAppInfo.get(position - 1);

	            } else {

	                int location = userAppInfo.size() + 2;

	                info = systemAppInfo.get(position - location);
	            }
			
			viewHolder.icon.setBackground(info.getIcon());
			viewHolder.tvApkSize.setText(Formatter.formatFileSize(AppManagerActivity.this, info.getApkSize()));
			viewHolder.tvName.setText(info.getApkName());
			if(info.isRom()){
				viewHolder.tvLocation.setText("内存中");
			}else{
				viewHolder.tvLocation.setText("SD中");
			}
			return view;
		}
		
	} 
	
	static class ViewHolder{
		ImageView icon;
		TextView tvApkSize;
		TextView tvLocation;
		TextView tvName;
	}
	private void initDate() {
		//开启子线程，拿到所有手机上的应用程序的信息是耗时的操作
		new Thread(){
			public void run() {
				//拿到所有应用程序的信息
				appInfos = AppInfos.getAppInfos(AppManagerActivity.this);
				userAppInfo = new ArrayList<AppInfo>();
				systemAppInfo = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if(appInfo.isUserApp()){
						userAppInfo.add(appInfo);
					}else{
						systemAppInfo.add(appInfo);
					}
				}
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
		//给listview设置滚动监听
		listview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(userAppInfo!=null&&systemAppInfo!=null){
					if(firstVisibleItem>userAppInfo.size()+1){
						tvApp.setText("系统程序"+"("+systemAppInfo.size()+")");
					}else{
						tvApp.setText("用户程序"+"("+userAppInfo.size()+")");
					}
				}
			}
		});
	}

	@SuppressLint("NewApi")
	private void getSpace() {
		//获取rom的剩余空间
		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
		//获取sd的剩余空间
		long sdFreeSpace=Environment.getExternalStorageDirectory().getFreeSpace();
		
		tvRom.setText("内部存储可用 : "+Formatter.formatFileSize(this, romFreeSpace));
		tvSD.setText("sd可用 : "+Formatter.formatFileSize(this, sdFreeSpace));
	}

	private void initUI() {
		setContentView(R.layout.activity_app_manager);
		ViewUtils.inject(this);
	}
}
