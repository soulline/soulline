package com.yyl.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class PackageUtil {

	public static boolean isInstalled(Context context, String pkgName) {
		if (context == null) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> list = pm.getInstalledApplications(0);
		for (ApplicationInfo appInfo : list) {
			if (appInfo.packageName.equals(pkgName)) {
				return true;
			}
		}
		return false;
	}
}
