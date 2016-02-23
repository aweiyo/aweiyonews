package com.aweiyo.newmobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.db.dao.AnivirusDao;
import com.aweiyo.newmobilesafe.utils.MD5Utils;

/**
 * ������ɱ
 * @author aweiyoo
 *
 */
public class AntivirusActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	private void initData() {
		PackageManager packageManager = getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			String appName =  packageInfo.applicationInfo.loadLabel(packageManager).toString();
			//���Ӧ�ó�����ļ�����
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			String md5=MD5Utils.getFileMd5(sourceDir);
			//�ж�MD5ֵ�Ƿ������ݿ���
			String desc = AnivirusDao.checkFileVirus(md5);
			if(desc==null){
				
			}
		}
	}

	private void initUI() {
		setContentView(R.layout.activity_antivirus);
		ImageView ivScanning=(ImageView) findViewById(R.id.iv_scanning);
		RotateAnimation animation=new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(3000);
		//���ö�������ѭ��
		animation.setRepeatCount(Animation.INFINITE);
		ivScanning.startAnimation(animation);
		
	}
}
