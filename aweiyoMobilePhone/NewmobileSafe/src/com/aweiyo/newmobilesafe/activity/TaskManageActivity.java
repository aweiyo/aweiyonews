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
	
	private SharedPreferences mPref;//用来记录checkbox的勾选状态

	private long freeMemory;

	private long totalMemory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initDate();
	}

	// 方法1
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
				// 用adapter改变ui应该放在子线程
				/**
				 * 方法1.放在利用handler 方法2.
				 */
				// mHandler.sendEmptyMessage(0);
				//集合必须放在外面
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
					taskInfo.setChecked(false); //这个值是用来判断和记录的
					holder.ckAppStatus.setChecked(false);//这个是用来改变勾选状态的  勾变不勾  不勾边勾
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

		// 获得activityManager
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// 获得正在运行的进程
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();

		tvTaskProcessCount.setText("正在运行的进程(" + runningAppProcesses.size()
				+ ")个");

		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);// 这一句必须要加，不加这个获取不到信息
		totalMemory = memoryInfo.totalMem;
		freeMemory = memoryInfo.availMem;

		/**
		 * 总内存在手机里面的：/proc/meminfo目录下
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
		for (char c : readLine.toCharArray()) {// 把字符串转换成字符数组，然后遍历，找出里面的数字来
			if (c >= '0' && c <= '9') {
				sb.append(c);
			}
		}
		totalMemory = Long.parseLong(sb.toString()) * 1024;// 读书来的只是数字，要乘以1024才是对的

		tvTaskMemory.setText("可用内存/总内存:"
				+ Formatter.formatFileSize(TaskManageActivity.this, freeMemory)
				+ "/"
				+ Formatter
						.formatFileSize(TaskManageActivity.this, totalMemory));

		// activityManager.getMemoryInfo(outInfo)
	}

	/**
	 * 全选按钮
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
		//adapter的界面改变刷新界面
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 反选按钮
	 * @param view
	 */
	public void selectOppsite(View view){
		for (TaskInfo info : taskInfos) {
			info.setChecked(!info.isChecked());
		}
		//adapter里面更新ui要使用这个刷新
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 清理进程
	 * @param view
	 */
	public void killProcess(View view){
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> list=new ArrayList<TaskInfo>();
		for (TaskInfo info : taskInfos) {
			if(info.isChecked()){
				
			//判断是不是自己的	
				if(info.getPackageName().equals(getPackageName())){
					return;
				}
				
//			activityManager.killBackgroundProcesses(info.getPackageName());
			//把删除掉的重集合里面去掉
//			taskInfos.remove(info);//不能再迭代的时候删除这个item，只能迭代完成统一删除，因为迭代集合的时候不能改变集合的大小，现在删除集合里面的元素就是改变了集合里面的大小
			list.add(info);
//			Toast.makeText(TaskManageActivity.this, "总共释放了"+Formatter.formatFileSize(TaskManageActivity.this, info.getMemorySize())+"空间", 0).show();
			}
		}
		long memory=0;
		for (TaskInfo info : list) {
			activityManager.killBackgroundProcesses(info.getPackageName());
			taskInfos.remove(info);
			memory+=info.getMemorySize();
		
			Toast.makeText(TaskManageActivity.this, "总共释放了"+Formatter.formatFileSize(TaskManageActivity.this,memory )+"空间", 0).show();
		}
		
		freeMemory+=memory;
		tvTaskMemory.setText("可用内存/总内存:"
				+ Formatter.formatFileSize(TaskManageActivity.this, freeMemory)
				+ "/"
				+ Formatter
						.formatFileSize(TaskManageActivity.this, totalMemory));
		
		//listview更新完界面后，adapter要刷新
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
			// textView.setText("用户程序"+"("+userTaskInfo.size()+")");
			// textView.setBackgroundColor(Color.GRAY);
			// textView.setTextColor(Color.WHITE);
			// return textView;
			// }else if(position==userTaskInfo.size()+1){
			// TextView textView=new TextView(TaskManageActivity.this);
			// textView.setText("系统程序"+"("+sysTaskInfo.size()+")");
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
			//不判斷的時候不能用viewHolder.ckAppStatus，因為要具體到是哪一個item，而viewHolder.ckAppStatus并不會具體到哪一個
//			if (viewHolder.ckAppStatus.isChecked()) {
				if (taskInfo.isChecked()) {
				viewHolder.ckAppStatus.setChecked(true);
			}else{
				viewHolder.ckAppStatus.setChecked(false);
			}

			//判断是不是自己的应用，如果是自己的应用个，就把勾选得checkbox设置为不可见
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
