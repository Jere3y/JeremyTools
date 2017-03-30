package jeremy.com.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Xin on 2017/3/16 0016,20:02.
 * Toast工具类
 */

public class ToastUtil {
    public static void showLong(Context context, String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }
    public static void showShort(Context context, String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

}
