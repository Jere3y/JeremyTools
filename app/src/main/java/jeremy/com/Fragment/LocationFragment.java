package jeremy.com.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dd.CircularProgressButton;

import jeremy.com.R;
import jeremy.com.adapter.BusResultRecyclerAdapter;
import jeremy.com.utils.SpUtil;
import jeremy.com.utils.ToastUtil;


/**
 * Created by Xin on 2017/3/15 0015.
 * 选择路线的fragment
 */

public class LocationFragment extends Fragment implements RouteSearch.OnRouteSearchListener, com.amap.api.location.AMapLocationListener {

    private static final int BUS_ROUTE_SUCCESS = 1;
    private static final int BUS_ROUTE_FAIL = -1;
    private static final int LOCATION_FAIL = 884;
    private final String TAG = "LocationFragment";

    private TextView tv_show_location;

    RouteSearch routeSearch;
    //乘车方式，默认给个默认
    private final int BUS_DEFAULT = RouteSearch.BUS_DEFAULT;
    private final int BUS_LEASE_CHANGE = RouteSearch.BUS_LEASE_CHANGE;
    private final int BUS_LEASE_WALK = RouteSearch.BUS_LEASE_WALK;
    private final int BUS_SAVE_MONEY = RouteSearch.BUS_SAVE_MONEY;

    private double currentLatitude;
    private double currentLongitude;
    private String currentStreet;
    private String currentDistrict;
    private String currentStreetNum;
    private String currentCity;
    private RecyclerView rv_way_to_anywhere;
    private LatLonPoint from;
    private LatLonPoint to;
    private int pathCount;
    CircularProgressButton mButton;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    private CircularProgressButton bn_go_home;
    private CircularProgressButton bn_go_to_work;
    private CircularProgressButton bn_go_to_anywhere;
    private TextView tv_taxi_cost;
    //    private ListView lv_bus;
    private RecyclerView lv_bus;
    private BusRouteResult mBusRouteResult;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, null);
        findView(view);
        findMyLocation();
        mContext = getActivity().getApplicationContext();
        routeSearch = new RouteSearch(getContext());
        //设置数据回调监听器
        routeSearch.setRouteSearchListener(this);
        return view;
    }


    private void findView(View view) {
        tv_show_location = (TextView) view.findViewById(R.id.tv_show_location);
        tv_taxi_cost = (TextView) view.findViewById(R.id.tv_taxi_cost);

        bn_go_home = (CircularProgressButton) view.findViewById(R.id.bn_go_home);
        bn_go_to_work = (CircularProgressButton) view.findViewById(R.id.bn_go_to_work);
        bn_go_to_anywhere = (CircularProgressButton) view.findViewById(R.id.bn_go_to_anywhere);
//        lv_bus = (ListView) view.findViewById(R.id.lv_bus);
        lv_bus = (RecyclerView) view.findViewById(R.id.lv_bus);
        lv_bus.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_bus.setItemAnimator(new DefaultItemAnimator());

        bn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
        bn_go_to_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWork();
            }
        });
        bn_go_to_anywhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAnywhere();
            }
        });

        view.findViewById(R.id.ll_my_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findMyLocation();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 开始定位
     */
    private void findMyLocation() {
        Log.i(TAG, "开始定位: 150");
        tv_show_location.setText("刷新位置中...");
        if (mLocationClient == null) {

            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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
//        mLocationOption.setInterval(3000);
        //设置定位回调监听

        mLocationClient.setLocationListener(this);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 定位成功后，回调的方法
     *
     * @param amapLocation 定位成功后，返回的所有位置信息
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.d(TAG, "定位回调：" + amapLocation.getErrorCode());
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //获取纬度
                currentLatitude = amapLocation.getLatitude();
                //获取经度
                currentLongitude = amapLocation.getLongitude();
                //城区信息
                currentDistrict = amapLocation.getDistrict();
                //街道信息
                currentStreet = amapLocation.getStreet();
                //街道门牌号信息
                currentStreetNum = amapLocation.getStreetNum();
                //城市信息
                currentCity = amapLocation.getCity();
                tv_show_location.setText(currentDistrict + currentStreet + currentStreetNum);
                SpUtil.putString(getContext(), SpUtil.CURRENT_CITY, currentCity);
                from = new LatLonPoint(currentLatitude, currentLongitude);
                Log.d("定位成功", "from:" + from);
                //amapLocation.getAccuracy();//获取精度信息
                //amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                //amapLocation.getCountry();//国家信息
                //amapLocation.getProvince();//省信息
                //amapLocation.getCityCode();//城市编码
                //amapLocation.getAdCode();//地区编码
                //amapLocation.getAoiName();//获取当前定位点的AOI信息
                //amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                //amapLocation.getFloor();//获取当前室内定位的楼层
            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }
    private void goHome() {
        mButton = bn_go_home;
        if (bn_go_to_anywhere.getProgress() != 0) {
            bn_go_to_anywhere.setProgress(0);
        }
        if (bn_go_to_work.getProgress() != 0) {
            bn_go_to_work.setProgress(0);
        }
        //从sp中拿出存储的家的地址经纬度。因为sp中不能存放double类型，所以用string类型存储
        //根据经纬度，执行查询
        doQuery(SpUtil.HOME_LATITUDE, SpUtil.HOME_LONGITUDE);

    }

    private void goWork() {
        mButton = bn_go_to_work;
        if (bn_go_home.getProgress() != 0) {
            bn_go_home.setProgress(0);
        }
        if (bn_go_to_anywhere.getProgress() != 0) {
            bn_go_to_anywhere.setProgress(0);
        }
        doQuery(SpUtil.WORK_LATITUDE, SpUtil.WORK_LONGITUDE);
    }

    private void goAnywhere() {
        mButton = bn_go_to_anywhere;
        if (bn_go_home.getProgress() != 0) {
            bn_go_home.setProgress(0);
        }
        if (bn_go_to_work.getProgress() != 0) {
            bn_go_to_work.setProgress(0);
        }
        doQuery(SpUtil.OTHER_LATITUDE, SpUtil.OTHER_LONGITUDE);
    }

    /**
     * 根据在sp中的经纬度key，执行查询
     *
     * @param latitudeKey  latitudeKye
     * @param longitudeKey longitudeKey
     */
    private void doQuery(String latitudeKey, String longitudeKey) {
        mButton.setIndeterminateProgressMode(true);
        mButton.setProgress(10);
        String latitude = SpUtil.getString(getContext(), latitudeKey, "");
        String longitude = SpUtil.getString(getContext(), longitudeKey, "");
        //只有两个经纬度不是空的时候才执行查询，空则提示用户设置位置
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            to = new LatLonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (from != null) {
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, BUS_DEFAULT, currentCity, 0);
                routeSearch.calculateBusRouteAsyn(query);//开始规划路径
            } else {
                setProgressError();
                ToastUtil.showLong(getContext(), "请耐心等待定位完成！！！");
            }
        } else {
            setProgressError();
            ToastUtil.showLong(getContext(), "请在设置中设置位置！");
        }
    }

    /**
     * @param result bus路线搜索结果
     * @param rCode  返回code
     */
    @Override
    public void onBusRouteSearched(BusRouteResult result, int rCode) {
        Log.d(TAG, "onBusRouteSearched: rCode" + rCode);
        if (1000 == rCode) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mBusRouteResult = result;
//                    BusResultListAdapter mBusResultListAdapter =
//                            new BusResultListAdapter(mContext, mBusRouteResult);
//                    lv_bus.setAdapter(mBusResultListAdapter);
                    BusResultRecyclerAdapter adapter =
                            new BusResultRecyclerAdapter(mContext, mBusRouteResult);
                    lv_bus.setAdapter(adapter);
                    setProgressComplete();
                } else if (result != null && result.getPaths() == null) {
                    setProgressError();
                }
            }
        } else {
            setProgressError();
        }
    }



    private void setProgressComplete() {
        mButton.setProgress(100);
    }

    private void setProgressError() {
        mButton.setProgress(-1);
    }


    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
