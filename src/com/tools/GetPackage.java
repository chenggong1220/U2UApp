package com.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class GetPackage {

	public static String getPackageInfo(Context context) {
		String version = "";

		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			version = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return version;
	}
}
