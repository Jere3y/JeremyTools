package jeremy.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import jeremy.com.R;
import jeremy.com.utils.SpUtil;

/**
 * 选择地址的类，可以startActivityForResult（），用来获取请求的地址参数
 */
public class SelectFromAmapActivity extends Activity implements LocationSource, AMapLocationListener, AMap.OnCameraChangeListener {
    public final static int RESULT_CODE = 1;
    private static final String TAG = "SelectFromAmapActivity";
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    private MapView mMapView;
    private Intent mIntent;
    private AMap aMap;
    MyLocationStyle myLocationStyle;
    private UiSettings mUiSettings;//定义一个UiSettings对象


    private ImageView iv_select_icon;
    private Rect rect;
    private FrameLayout fl_root_map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_from_amap);
        iv_select_icon = (ImageView) findViewById(R.id.iv_select_icon);
        mMapView = (MapView) findViewById(R.id.map);
        fl_root_map = (FrameLayout) findViewById(R.id.fl_root_map);

        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        aMap.setTrafficEnabled(false);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        // 设置定位监听
        aMap.setLocationSource(this);
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnCameraChangeListener(this);
        float zoom = aMap.getCameraPosition().zoom;
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
        aMap.moveCamera(CameraUpdateFactory.zoomTo(SpUtil.getFloat(this, SpUtil.ZOOM_LEVEL, zoom)));

        rect = new Rect();
        mIntent = getIntent();
        int id = mIntent.getIntExtra("id", 0);
        Log.d(TAG, "id: " + id);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Point globalOffset = new Point();
        fl_root_map.getGlobalVisibleRect(rect, globalOffset);
        iv_select_icon.getGlobalVisibleRect(rect);
        int x = (rect.left + rect.right) / 2;

        int y = (rect.bottom + rect.top) / 2 - globalOffset.y;

        LatLng latLng = aMap.getProjection().fromScreenLocation(new Point(x, y));
        Log.d(TAG, "latLng: " + latLng);
        mIntent.putExtra("latitude", latLng.latitude);
        mIntent.putExtra("longitude", latLng.longitude);
        setResult(RESULT_CODE, mIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    /**
     * 镜头改变完成，存储上次改变的zoom级别，下次打开还是按照个这个级别打开
     *
     * @param cameraPosition cameraPosition
     */
    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        SpUtil.putFloat(this, SpUtil.ZOOM_LEVEL, cameraPosition.zoom);
    }
}
