package com.aweiyo.newmobilesafe.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.bean.TaskInfo;
import com.aweiyo.newmobilesafe.engine.TaskInfoParser;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TaskManageActivity extends Activity {

	@ViewInject(R.id.tv_task_process_count)
	private TextView tvTaskProcessCount;

	@ViewInject(R.id.tv_task_memory)
	private TextView tvTaskMemory;

	@ViewInject(R.id.list_view)
	private ListView listView;

	private TaskManageAdapter adapter;
	
	private String readLine;

	private List<TaskInfo> sysTaskInfo;

	private List<TaskInfo> userTaskInfo;

	private List<TaskInfo> taskInfos;
	
	private SharedPreferences mPref;//������¼checkbox�Ĺ�ѡ״̬

	private long freeMemory;

	private long totalMemory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initDate();
	}

	// ����1
	// private Handler mHandler=new Handler(){
	// public void handleMessage(android.os.Message msg) {
	// //setAdapter
	// };
	// };

	private void initDate() {
		new Thread() {
			public void run() {
				taskInfos = TaskInfoParser
						.getTaskInfos(TaskManageActivity.this);
				// ��adapter�ı�uiӦ�÷������߳�
				/**
				 * ����1.��������handler ����2.
				 */
				// mHandler.sendEmptyMessage(0);
				//���ϱ����������
				userTaskInfo = new ArrayList<TaskInfo>();
				sysTaskInfo = new ArrayList<TaskInfo>();

				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUserApp()) {
						userTaskInfo.add(taskInfo);
					} else {
						sysTaskInfo.add(taskInfo);
					}
				}
				// Log.v("taskInfos", taskInfos.size()+"");
				// Log.v("userTaskInfo", userTaskInfo.size()+"");
				// Log.v("sysTaskInfo", sysTaskInfo.size()+"");

				runOnUiThread(new Runnable() {
					public void run() {
						adapter = new TaskManageAdapter();
						listView.setAdapter(adapter);
					}
				});
			};
		}.start();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TaskInfo taskInfo= (TaskInfo) listView.getItemAtPosition(position);
				ViewHolder holder = (ViewHolder) view.getTag();
//				mPref = getSharedPreferences("config", MODE_PRIVATE);
//				boolean isChecked = mPref.getBoolean("isChecked", true);
//				if(isChecked){
//					taskInfo.setChecked(true);
//					holder.ckAppStatus.setChecked(true);
//				}else{
//					taskInfo.setChecked(false);
//					holder.ckAppStatus.setChecked(false);
//				}
				if(taskInfo.isChecked()){
					taskInfo.setChecked(false); //���ֵ�������жϺͼ�¼��
					holder.ckAppStatus.setChecked(false);//����������ı乴ѡ״̬��  ���䲻��  �����߹�
//					mPref.edit().putBoolean("isChecked", false).commit();
				}else {
					taskInfo.setChecked(true);
					holder.ckAppStatus.setChecked(true);
//					mPref.edit().putBoolean("isChecked", true).commit();
				}
			}

		});
	}

	@SuppressLint("NewApi")
	private void initUI() {
		setContentView(R.layout.activity_task_manager);
		ViewUtils.inject(TaskManageActivity.this);

		// ���activityManager
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// ����������еĽ���
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();

		tvTaskProcessCount.setText("�������еĽ���(" + runningAppProcesses.size()
				+ ")��");

		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);// ��һ�����Ҫ�ӣ����������ȡ������Ϣ
		totalMemory = memoryInfo.totalMem;
		freeMemory = memoryInfo.availMem;

		/**
		 * ���ڴ����ֻ�����ģ�/proc/meminfoĿ¼��
		 */

		FileInputStream fis;
		try {
			fis = new FileInputStream(new File("/proc/meminfo"));
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			readLine = br.readLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer sb = new StringBuffer();
		for (char c : readLine.toCharArray()) {// ���ַ���ת�����ַ����飬Ȼ��������ҳ������������
			if (c >= '0' && c <= '9') {
				sb.append(c);
			}
		}
		totalMemory = Long.parseLong(sb.toString()) * 1024;// ��������ֻ�����֣�Ҫ����1024���ǶԵ�

		tvTaskMemory.setText("�����ڴ�/���ڴ�:"
				+ Formatter.formatFileSize(TaskManageActivity.this, freeMemory)
				+ "/"
				+ Formatter
						.formatFileSize(TaskManageActivity.this, totalMemory));

		// activityManager.getMemoryInfo(outInfo)
	}

	/**
	 * ȫѡ��ť
	 * @param view
	 */
	public void selectAll(View view){
		for (TaskInfo info : taskInfos) {
			info.setChecked(true);
		}
		
//		for (TaskInfo info : userTaskInfo) {
//			info.setChecked(true);
//		}
//		
//		for (TaskInfo info : sysTaskInfo) {
//			info.setChecked(true);
//		}
		//adapter�Ľ���ı�ˢ�½���
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * ��ѡ��ť
	 * @param view
	 */
	public void selectOppsite(View view){
		for (TaskInfo info : taskInfos) {
			info.setChecked(!info.isChecked());
		}
		//adapter�������uiҪʹ�����ˢ��
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * �������
	 * @param view
	 */
	public void killProcess(View view){
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> list=new ArrayList<TaskInfo>();
		for (TaskInfo info : taskInfos) {
			if(info.isChecked()){
				
			//�ж��ǲ����Լ���	
				if(info.getPackageName().equals(getPackageName())){
					return;
				}
				
//			activityManager.killBackgroundProcesses(info.getPackageName());
			//��ɾ�������ؼ�������ȥ��
//			taskInfos.remove(info);//�����ٵ�����ʱ��ɾ�����item��ֻ�ܵ������ͳһɾ������Ϊ�������ϵ�ʱ���ܸı伯�ϵĴ�С������ɾ�����������Ԫ�ؾ��Ǹı��˼�������Ĵ�С
			list.add(info);
//			Toast.makeText(TaskManageActivity.this, "�ܹ��ͷ���"+Formatter.formatFileSize(TaskManageActivity.this, info.getMemorySize())+"�ռ�", 0).show();
			}
		}
		long memory=0;
		for (TaskInfo info : list) {
			activityManager.killBackgroundProcesses(info.getPackageName());
			taskInfos.remove(info);
			memory+=info.getMemorySize();
		
			Toast.makeText(TaskManageActivity.this, "�ܹ��ͷ���"+Formatter.formatFileSize(TaskManageActivity.this,memory )+"�ռ�", 0).show();
		}
		
		freeMemory+=memory;
		tvTaskMemory.setText("�����ڴ�/���ڴ�:"
				+ Formatter.formatFileSize(TaskManageActivity.this, freeMemory)
				+ "/"
				+ Formatter
						.formatFileSize(TaskManageActivity.this, totalMemory));
		
		//listview����������adapterҪˢ��
		adapter.notifyDataSetChanged();
		
	}
	class TaskManageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return taskInfos.size();
		}

		@Override
		public Object getItem(int position) {
			
			return taskInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			// if(position==0&&userTaskInfo.size()!=0){
			// TextView textView=new TextView(TaskManageActivity.this);
			// textView.setText("�û�����"+"("+userTaskInfo.size()+")");
			// textView.setBackgroundColor(Color.GRAY);
			// textView.setTextColor(Color.WHITE);
			// return textView;
			// }else if(position==userTaskInfo.size()+1){
			// TextView textView=new TextView(TaskManageActivity.this);
			// textView.setText("ϵͳ����"+"("+sysTaskInfo.size()+")");
			// textView.setBackgroundColor(Color.GRAY);
			// textView.setTextColor(Color.WHITE);
			// return textView;
			// }
			// Log.v("userTaskInfo", userTaskInfo.size()+"");
			// Log.v("sysTaskInfo", sysTaskInfo.size()+"");

			if (convertView != null && convertView instanceof LinearLayout) {

				viewHolder = (ViewHolder) convertView.getTag();

			} else {
				viewHolder = new ViewHolder();
				convertView = View.inflate(TaskManageActivity.this,
						R.layout.item_task_manager, null);
				viewHolder.ivAppIcon = (ImageView) convertView
						.findViewById(R.id.iv_app_icon);
				viewHolder.tvAppName = (TextView) convertView
						.findViewById(R.id.tv_app_name);
				viewHolder.ckAppStatus = (CheckBox) convertView
						.findViewById(R.id.ck_app_status);
				viewHolder.tvAppMemorySize = (TextView) convertView
						.findViewById(R.id.tv_app_memory_size);
				convertView.setTag(viewHolder);

			}

			/*
			 * TaskInfo taskInfo=null; if(position<userTaskInfo.size()+1){
			 * taskInfo=userTaskInfo.get(position-1); }else
			 * if(position>userTaskInfo.size()+2){ int
			 * location=userTaskInfo.size()+1;
			 * taskInfo=sysTaskInfo.get(position-location-1); }
			 */

			TaskInfo taskInfo = taskInfos.get(position);
			viewHolder.ivAppIcon.setImageDrawable(taskInfo.getIcon());
			viewHolder.tvAppName.setText(taskInfo.getAppName());
			viewHolder.tvAppMemorySize.setText(Formatter.formatFileSize(
					TaskManageActivity.this, taskInfo.getMemorySize()));
			//���Д�ĕr������viewHolder.ckAppStatus�����Ҫ���w������һ��item����viewHolder.ckAppStatus���������w����һ��
//			if (viewHolder.ckAppStatus.isChecked()) {
				if (taskInfo.isChecked()) {
				viewHolder.ckAppStatus.setChecked(true);
			}else{
				viewHolder.ckAppStatus.setChecked(false);
			}

			//�ж��ǲ����Լ���Ӧ�ã�������Լ���Ӧ�ø����Ͱѹ�ѡ��checkbox����Ϊ���ɼ�
			if(taskInfo.getPackageName().equals(getPackageName())){
				viewHolder.ckAppStatus.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.ckAppStatus.setVisibility(View.VISIBLE);
			}	
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView ivAppIcon;
		TextView tvAppName;
		CheckBox ckAppStatus;
		TextView tvAppMemorySize;
	}

}
