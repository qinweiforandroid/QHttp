package com.qw.http.sample;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by qinwei on 2016/11/22 10:46
 * email:qinwei_it@163.com
 */

public class PermissionHelper {
    public static boolean requestPermission(Activity context, String permission, int requestCode) {
        return requestPermission(context, new String[]{permission}, requestCode);
    }

    public static boolean requestPermission(Activity context, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermission = ContextCompat.checkSelfPermission(context, permissions[0]);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, permissions, requestCode);
                return false;
            }
        }
        return true;
    }
}
