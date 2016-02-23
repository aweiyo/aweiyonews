package com.aweiyo.newmobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {
	private SettingItemView sivSim;
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		String sim = mPref.getString("simNumber", null);
		if(TextUtils.isEmpty(sim)){
			sivSim.setCheckded(false);
			sivSim.setDesc("未绑定sim卡");
		}else{
			sivSim.setCheckded(true);
			sivSim.setDesc("已经绑定sim卡");
			
		}
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(sivSim.isChecked()){
					sivSim.setCheckded(false);
					mPref.edit().remove("simNumber");
				}else{
					sivSim.setCheckded(true);
					TelephonyManager manager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simNumber = manager.getSimSerialNumber();
					mPref.edit().putString("simNumber", simNumber);				
				}
			}
		});
		
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
				overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// 进入动画和退出动画
		
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// 进入动画和退出动画
	}
}
