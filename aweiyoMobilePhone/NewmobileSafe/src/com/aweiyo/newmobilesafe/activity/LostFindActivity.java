package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.aweiyo.newmobilesafe.R;

public class LostFindActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		//判断是否进入过设置向导页   configed为记录该事情的变量
		boolean configed = mPref.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
		}else{
			startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
			finish();
		}
	}
	
	public void reEnter(View view){
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}

}
