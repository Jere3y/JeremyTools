package jeremy.com;

import android.app.Application;
import android.content.Context;

/**
 * Created by Xin on 2017/4/17 0017,21:30.
 */

public class MyApp extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
