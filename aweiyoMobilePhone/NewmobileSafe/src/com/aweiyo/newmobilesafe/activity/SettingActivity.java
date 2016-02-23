package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.view.SettingClickView;
import com.aweiyo.newmobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView sivSett;
	private SharedPreferences mPref;
	private SettingClickView scv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sivSett = (SettingItemView) findViewById(R.id.siv_sett);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		//利用sharedpreferences来记录更新的状态
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			//sivSett.setDesc("自动更新已开");
			sivSett.setCheckded(true);
		}else{
		//	sivSett.setDesc("自动更新已关");
			sivSett.setCheckded(false);
		}
		//sivSett.setTitle("自动更新状态");
		
		sivSett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//System.out.println("监听到了...");				
				//判断自动更新是否已经开启
				if(sivSett.isChecked()){
					sivSett.setCheckded(false);
			//		sivSett.setDesc("自动更新已关");
					mPref.edit().putBoolean("auto_update", false).commit();
				}else{
					sivSett.setCheckded(true);
			//		sivSett.setDesc("自动更新已开");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
		initAddressStyle();
	}
	
	/**
	 * 初始化打电话归属地的样式
	 */
	public void initAddressStyle(){
		
		scv=(SettingClickView) findViewById(R.id.scv_address_style);
		
		scv.setTitle("归属地提示框风格");
		
		int style=mPref.getInt("address_Style", 0);
		scv.setDesc(items[style]);
		
		scv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showSingleDialog();
			}
		});
	}

	final String[] items=new String[]{"半透明","活力橙","苹果绿","夕阳红"}; 
	protected void showSingleDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("归属地提示框风格");
		
		int style=mPref.getInt("address_style", 0);
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mPref.edit().putInt("address_style", which).commit();
				scv.setDesc(items[which]);
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return ;
			}
		});
		
		builder.show();
	}
}
