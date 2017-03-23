package jeremy.com.utils;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import jeremy.com.templete.AbsLocationFragment;

/**
 * Created by Xin on 2017/3/19 0019,13:56.
 * 封装的工具类，用来获取当前位置的所有信息。
 * 初始化时，需要指定两个属性，Context和继承了LocationFragment的实例
 */
public class MyLocationUtil {
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;
    private static MyLocationUtil myLocationUtil;
    private Context context;
    private AbsLocationFragment myLocationListener;

    /**
     * @param context            getApplicationContext()
     * @param myLocationListener 继承了LocationFragment的this
     */
    public static MyLocationUtil newInstance(Context context, AbsLocationFragment myLocationListener) {
        if (myLocationUtil == null) {
            myLocationUtil = new MyLocationUtil(context, myLocationListener);
        }
        return myLocationUtil;
    }


    /**
     * 停止定位后，本地定位服务并不会被销毁
     */
    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    /**
     * 销毁定位客户端，同时销毁本地定位服务
     */
    public void destroyLocation() {
        mLocationClient.onDestroy();
    }


    private MyLocationUtil(Context context, AbsLocationFragment myLocationListener) {
        this.context = context;
        this.myLocationListener = myLocationListener;
    }

    public void findMyLocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(context);
        }
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //返回地里位置信息
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setHttpTimeOut(3500);
//        mLocationOption.setInterval(3000);
        //设置定位回调监听
        mLocationClient.setLocationListener(myLocationListener);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
}
