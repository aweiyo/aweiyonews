package com.aweiyo.newmobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity{
	
	private GridView gvHome;
	
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	private SharedPreferences mPref;

	private String savePassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gvHome=(GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		//监听GridView的item条目
		gvHome.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					passwordSetDailog();
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this,TaskManageActivity.class));
					break;
				case 5:
					startActivity(new Intent(HomeActivity.this,AntivirusActivity.class));
					break;	
					
				case 7:
					startActivity(new Intent(HomeActivity.this,AToolsActivity.class));
					break;
				case 8:
					Intent intent=new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}});
	}
	
	/**
	 * 进入手机防盗页面的密码界面
	 */
	protected void passwordSetDailog() {
		savePassword = mPref.getString("password", null);
		if(TextUtils.isEmpty(savePassword)){
			showPasswordSetDailog();
		}else{
			showPasswordInputDialog();
		}
	}

	/**
	 * 设置了密码，再次登录
	 */
	private void showPasswordInputDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(HomeActivity.this, R.layout.dialog_input_password, null);
		dialog.setView(view);
		
		final EditText etCheckPassword = (EditText) view.findViewById(R.id.et_check_password);
		Button btnCheckOk=(Button) view.findViewById(R.id.btn_chenk_ok);
		Button btnCheckCancle=(Button) view.findViewById(R.id.btn_check_cancel);
		
		btnCheckOk.setOnClickListener(new OnClickListener() {
			
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String checkPassword=etCheckPassword.getText().toString();
				if(!checkPassword.isEmpty()){
					if(checkPassword.equals(savePassword)){
						Toast.makeText(HomeActivity.this, "登陆成功", 0).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this,LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "密码错误", 0).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
				}
			}
		});
		
		btnCheckCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 没设置密码，初次登录
	 */
	private void showPasswordSetDailog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		dialog.setView(view,0,0,0,0);
		
		
		
		final Button btnOk=(Button) view.findViewById(R.id.btn_ok);
		final Button btnCancle=(Button) view.findViewById(R.id.btn_cancel);
		final EditText etPassword=(EditText) view.findViewById(R.id.et_password);
		final EditText etPasswordComfire=(EditText)view.findViewById(R.id.et_password_confirm);
		
		
		
		btnOk.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				String password=etPassword.getText().toString();
				String passwordComfire=etPasswordComfire.getText().toString();
				if(!TextUtils.isEmpty(password)&&!passwordComfire.isEmpty()){
					if(password.equals(passwordComfire)){
						Toast.makeText(HomeActivity.this, "登陆成功", 0).show();
						
						mPref.edit().putString("password", password).commit();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this,LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "密码不一致，请重新输入", 0).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
				}
//							Toast.makeText(HomeActivity.this, "密码不能为空", 0).show();
//				}else if(!(btnOk.getText().toString()).equals(btnCancle.getText().toString())){
//					Toast.makeText(HomeActivity.this, "密码不一致，请重新输入", 0).show();
//				}else{
//					Toast.makeText(HomeActivity.this, "登陆成功", 0).show();
//				}
			}
		});
		
		btnCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		
		dialog.show();
	}


	class HomeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			HolderView holderView=null;
//			if(holderView==null){
//				convertView=View.inflate(HomeActivity.this, R.layout.home_list_item, null);
//				holderView.ivItem=(ImageView) convertView.findViewById(R.id.iv_item);
//				holderView.tvItem=(TextView) convertView.findViewById(R.id.tv_item);
//			}else{
//				holderView=(HolderView) convertView.getTag();
//			}
//			holderView.ivItem.setBackgroundResource(mPics[position]);
//			holderView.tvItem.setText(mItems[position]);
//			return convertView;
			View view=View.inflate(HomeActivity.this, R.layout.home_list_item, null);
			ImageView ivHomeItem=(ImageView) view.findViewById(R.id.iv_item);
			TextView tvHomeItem=(TextView) view.findViewById(R.id.tv_item);
			ivHomeItem.setBackgroundResource(mPics[position]);
			tvHomeItem.setText(mItems[position]);
			return view;
		}
		
	}
	
	class HolderView{
		private TextView tvItem=null;
		private ImageView ivItem=null;
	}
}
