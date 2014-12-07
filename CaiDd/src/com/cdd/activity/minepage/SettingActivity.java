package com.cdd.activity.minepage;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.VersionEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.CheckVersionOp;

public class SettingActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.setting_activity);
		initTitle("设置");
		initView();
	}
	
	private void initView() {
		findViewById(R.id.version_check_layout).setOnClickListener(this);
		findViewById(R.id.mianze_layout).setOnClickListener(this);
		findViewById(R.id.about_layout).setOnClickListener(this);
	}
	
	private void showTipNoteDialog(final String msg, final String address) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setNegativeButton(getString(R.string.action_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
								startActivity(intent);
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}
	
	private int getVerCode() {
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private void checkVersion() {
		final CheckVersionOp checkOp = new CheckVersionOp(context);
		checkOp.setParams("100" + getVerCode());
		checkOp.onRequest(new RequestListener() {
			
			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCallBack(Object data) {
				VersionEntry version = checkOp.getVersionInfo();
				if (version != null && version.upgrade.equals("1")) {
					showTipNoteDialog(getString(R.string.new_version_note).replaceAll("%", version.ver),
							version.address);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.version_check_layout:
			checkVersion();
			break;

		default:
			break;
		}
		
	}

}
