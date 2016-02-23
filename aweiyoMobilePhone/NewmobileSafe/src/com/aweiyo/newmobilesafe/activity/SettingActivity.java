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
		
		//����sharedpreferences����¼���µ�״̬
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if(autoUpdate){
			//sivSett.setDesc("�Զ������ѿ�");
			sivSett.setCheckded(true);
		}else{
		//	sivSett.setDesc("�Զ������ѹ�");
			sivSett.setCheckded(false);
		}
		//sivSett.setTitle("�Զ�����״̬");
		
		sivSett.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//System.out.println("��������...");				
				//�ж��Զ������Ƿ��Ѿ�����
				if(sivSett.isChecked()){
					sivSett.setCheckded(false);
			//		sivSett.setDesc("�Զ������ѹ�");
					mPref.edit().putBoolean("auto_update", false).commit();
				}else{
					sivSett.setCheckded(true);
			//		sivSett.setDesc("�Զ������ѿ�");
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
		initAddressStyle();
	}
	
	/**
	 * ��ʼ����绰�����ص���ʽ
	 */
	public void initAddressStyle(){
		
		scv=(SettingClickView) findViewById(R.id.scv_address_style);
		
		scv.setTitle("��������ʾ����");
		
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

	final String[] items=new String[]{"��͸��","������","ƻ����","Ϧ����"}; 
	protected void showSingleDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("��������ʾ����");
		
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
		
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return ;
			}
		});
		
		builder.show();
	}
}
