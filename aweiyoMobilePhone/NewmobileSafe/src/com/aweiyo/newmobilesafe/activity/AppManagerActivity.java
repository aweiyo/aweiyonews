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
	//ʹ��ע������ȡid
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
		//��ȡ����ʾʣ��Ŀռ�
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
		                //�Ѷ�������������Ŀ����
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
				textView.setText("�û�����"+"("+userAppInfo.size()+")");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			}else if(position==userAppInfo.size()+1){
				TextView textView=new TextView(AppManagerActivity.this);
				textView.setText("ϵͳ����"+"("+systemAppInfo.size()+")");
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				return textView;
			}
			
	
			View view=null;
			ViewHolder viewHolder=null;
			//convertView�Ǹ���
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
	                //�Ѷ�������������Ŀ����
				  info = userAppInfo.get(position - 1);

	            } else {

	                int location = userAppInfo.size() + 2;

	                info = systemAppInfo.get(position - location);
	            }
			
			viewHolder.icon.setBackground(info.getIcon());
			viewHolder.tvApkSize.setText(Formatter.formatFileSize(AppManagerActivity.this, info.getApkSize()));
			viewHolder.tvName.setText(info.getApkName());
			if(info.isRom()){
				viewHolder.tvLocation.setText("�ڴ���");
			}else{
				viewHolder.tvLocation.setText("SD��");
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
		//�������̣߳��õ������ֻ��ϵ�Ӧ�ó������Ϣ�Ǻ�ʱ�Ĳ���
		new Thread(){
			public void run() {
				//�õ�����Ӧ�ó������Ϣ
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
		
		//��listview���ù�������
		listview.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(userAppInfo!=null&&systemAppInfo!=null){
					if(firstVisibleItem>userAppInfo.size()+1){
						tvApp.setText("ϵͳ����"+"("+systemAppInfo.size()+")");
					}else{
						tvApp.setText("�û�����"+"("+userAppInfo.size()+")");
					}
				}
			}
		});
	}

	@SuppressLint("NewApi")
	private void getSpace() {
		//��ȡrom��ʣ��ռ�
		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
		//��ȡsd��ʣ��ռ�
		long sdFreeSpace=Environment.getExternalStorageDirectory().getFreeSpace();
		
		tvRom.setText("�ڲ��洢���� : "+Formatter.formatFileSize(this, romFreeSpace));
		tvSD.setText("sd���� : "+Formatter.formatFileSize(this, sdFreeSpace));
	}

	private void initUI() {
		setContentView(R.layout.activity_app_manager);
		ViewUtils.inject(this);
	}
}
