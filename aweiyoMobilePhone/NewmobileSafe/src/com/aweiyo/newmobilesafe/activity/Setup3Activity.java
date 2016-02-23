package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aweiyo.newmobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}


	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup4Activity.class));
		finish();
		// ���������л��Ķ���
				overridePendingTransition(R.anim.tran_in, R.anim.tran_out);// ���붯�����˳�����
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		// ���������л��Ķ���
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);// ���붯�����˳�����
	}
}
