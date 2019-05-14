package com.llk.sl.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * author: yau
 * time: 2019/4/16 16:16
 * desc:
 */
public class PermissionUtil {

    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        for (String permission : permissions) {
            if (!checkPermission(activity, permission)) {
                requestPermissions(activity, permissions, requestCode);
                return false;
            }
        }
        return true;
    }

    private static boolean checkPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
