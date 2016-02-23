package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.aweiyo.newmobilesafe.R;

public class Setup4Activity extends BaseSetupActivity {
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}


	@Override
	public void showNextPage() {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		mPref.edit().putBoolean("configed", true).commit();
		// ���������л��Ķ���
				overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// ���붯�����˳�����
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// ���������л��Ķ���
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// ���붯�����˳�����
	}
}
