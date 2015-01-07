package com.cdd.activity.minepage;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.cdd.R;
import com.cdd.base.BaseActivity;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.about_activity);
		initTitle("关于");
		TextView versionT = (TextView) findViewById(R.id.version_t);
		versionT.setText("version " + getVerName());
	}

	private String getVerName() {
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
