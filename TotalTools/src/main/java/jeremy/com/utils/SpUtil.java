package jeremy.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Xin on 2017/3/19 0019,13:58.
 * 工具类，用于读/写SharedPrefrence
 */

public class SpUtil {
    public static final String ZOOM_LEVEL = "zoom_level";

    public static final String HOME_ADDRESS = "home_address";
    public static final String WORK_ADDRESS = "work_address";
    public static final String OTHER_ADDRESS = "other_address";

    public static final String HOME_LATITUDE = "home_latitude";
    public static final String WORK_LATITUDE = "work_latitude";
    public static final String OTHER_LATITUDE = "other_latitude";

    public static final String HOME_LONGITUDE = "home_longitude";
    public static final String WORK_LONGITUDE = "work_longitude";
    public static final String OTHER_LONGITUDE = "other_longitude";
    public static final String CURRENT_CITY = "current_city";
    public static final String FONT_SIZE = "font_size";
    public static final String CURRENT_PATH = "current_path";

    public static final String REPORT_TIME = "report_time";
    public static final String LIVE_WEATHER = "live_weather";
    public static final String TEMPERATURE = "temperature";
    public static final String WIND_DIR = "wind_dir";
    public static final String WIND_POWER = "windPower";
    public static final String HUMIDITY = "humidity";
    public static final String LAST_UPDATE_TIME = "last_update_time";
    public static final String SMART_WEATHER = "SMARTWEATHER";
    public static final String WEATHER_TIP = "WEATHER_TIP";

    private static SharedPreferences sp;

    private static void getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("conf", MODE_PRIVATE);
        }
    }

    public static void putString(Context context, String key, String value) {
        getSp(context);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defValue) {
        getSp(context);
        return sp.getString(key, defValue);
    }


    public static void putFloat(Context context, String key, float value) {
        getSp(context);
        sp.edit().putFloat(key, value).apply();
    }

    public static Float getFloat(Context context, String key, Float defValue) {
        getSp(context);
        return sp.getFloat(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        getSp(context);
        return sp.getInt(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getSp(context);
        sp.edit().putInt(key, value).apply();
    }


}
