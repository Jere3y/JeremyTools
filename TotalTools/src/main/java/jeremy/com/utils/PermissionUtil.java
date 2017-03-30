package jeremy.com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xin on 2017/3/16 0016,19:22.
 * 工具类,请求权限用
 */

public class PermissionUtil {
    private static final String TAG = "PermissionUtil";

    public static void CheckAndRequestPermission(final Activity context, final String[] permissions, final int requestCode) {
        String[] permissionDenieds = findPermissionDenied(context, permissions);
        if (permissionDenieds.length > 0) {
            ActivityCompat.requestPermissions(context,permissionDenieds,requestCode);
        }
    }

    private static String[] findPermissionDenied(final Context context, final String[] permissions){
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permissions[i]);
            }
        }

        String[] strings = deniedPermissions.toArray(new String[deniedPermissions.size()]);
        return strings;
    }
}
