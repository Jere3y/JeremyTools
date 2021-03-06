package jeremy.com.utils;

/**
 * Created by Xin on 2017/4/1 0001,20:44.
 */

import android.util.Log;

public class LogUtil {
    private LogUtil() {
    }

    private static int LOG_LEVEL = 0;

    public static void e(String tag, String msg) {
        if (LOG_LEVEL < 1)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL < 1)
            Log.e(tag, msg, tr);
    }


    public static void w(String tag, String msg) {
        if (LOG_LEVEL < 2)
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL < 2)
            Log.w(tag, msg, tr);
    }


    public static void i(String tag, String msg) {
        if (LOG_LEVEL < 3)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL < 3)
            Log.i(tag, msg, tr);
    }


    public static void d(String tag, String msg) {
        if (LOG_LEVEL < 4)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL < 4)
            Log.d(tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (LOG_LEVEL < 5)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL < 5)
            Log.v(tag, msg, tr);
    }
}