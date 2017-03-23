package jeremy.com.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.dd.CircularProgressButton;

import java.util.ArrayList;
import java.util.List;

import jeremy.com.R;
import jeremy.com.bean.BusLineTotalInfo;
import jeremy.com.templete.AbsLocationFragment;
import jeremy.com.utils.MyLocationUtil;
import jeremy.com.utils.SpUtil;
import jeremy.com.utils.ToastUtil;


/**
 * Created by Xin on 2017/3/15 0015.
 * 选择路线的fragment
 */

public class LocationFragment extends AbsLocationFragment implements RouteSearch.OnRouteSearchListener {
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

    private List<BusLineTotalInfo> busLineTotalInfoList;
    private MyAdapter adapter;

    private MyLocationUtil myLocationUtil;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mButton.setProgress(0);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, null);
        findView(view);
        initRecyclerView();
        myLocationUtil = MyLocationUtil.newInstance(getActivity().getApplicationContext(), this);
        routeSearch = new RouteSearch(getContext());
        //设置数据回调监听器
        routeSearch.setRouteSearchListener(this);
        return view;
    }

    private void initRecyclerView() {
        rv_way_to_anywhere.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        adapter = new MyAdapter();
        rv_way_to_anywhere.setAdapter(adapter);
        rv_way_to_anywhere.setItemAnimator(new DefaultItemAnimator());
        rv_way_to_anywhere.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void findView(View view) {
        tv_show_location = (TextView) view.findViewById(R.id.tv_show_location);
        rv_way_to_anywhere = (RecyclerView) view.findViewById(R.id.rv_way_to_anywhere);

        final CircularProgressButton bn_go_home = (CircularProgressButton) view.findViewById(R.id.bn_go_home);
        final CircularProgressButton bn_go_to_work = (CircularProgressButton) view.findViewById(R.id.bn_go_to_work);
        final CircularProgressButton bn_go_to_anywhere = (CircularProgressButton) view.findViewById(R.id.bn_go_to_anywhere);


        bn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton = bn_go_home;
                goHome();
            }
        });
        bn_go_to_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton = bn_go_to_work;
                goWork();
            }
        });
        bn_go_to_anywhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton = bn_go_to_anywhere;
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

    /**
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        findMyLocation();
    }

    private void findMyLocation() {
        tv_show_location.setText("刷新位置中...");
        myLocationUtil.findMyLocation();

    }

    private void goHome() {
        Log.d(TAG, "bn_go_home");
        //从sp中拿出存储的家的地址经纬度。因为sp中不能存放double类型，所以用string类型存储
        //根据经纬度，执行查询
        doQuery(SpUtil.HOME_LATITUDE, SpUtil.HOME_LONGITUDE);

    }

    private void goWork() {
        doQuery(SpUtil.WORK_LATITUDE, SpUtil.WORK_LONGITUDE);
    }

    private void goAnywhere() {
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
        mButton.setProgress(0);
        String latitude = SpUtil.getString(getContext(), latitudeKey, "");
        String longitude = SpUtil.getString(getContext(), longitudeKey, "");
        //只有两个经纬度不是空的时候才执行查询，空则提示用户设置位置
        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
            to = new LatLonPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (from != null) {
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, BUS_DEFAULT, currentCity, 0);
                routeSearch.calculateBusRouteAsyn(query);//开始规划路径
                mButton.setProgress(20);
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
            if (result != null) {

                if (busLineTotalInfoList == null) {
                    busLineTotalInfoList = new ArrayList<>();
                }
                Log.d(TAG, "result != null");
                listBusLines(result);
            }
        } else {
            setProgressError();
        }
    }

    /**
     * 定位成功后，回调的方法
     *
     * @param amapLocation 定位成功后，返回的所有位置信息
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
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


    /**
     * 处理查询结果数据，然后显示到RececlerView上
     *
     * @param result 查询结果
     */
    private void listBusLines(BusRouteResult result) {

        List<BusPath> paths = result.getPaths();
        float taxiCost = result.getTaxiCost();
        Log.d(TAG, "taxiCost: " + taxiCost);
        if (paths != null && (pathCount = paths.size()) != 0) {
            String bus_line_number;
            String start_station;
            String bus_station_count;
            String end_station;

            String time;
            String walk_dis;

            busLineTotalInfoList.clear();
            Log.d(TAG, "找到" + pathCount + "个回去的公交路线");
            Log.d("----", "------------------------------------------");
            for (int i = 0; i < pathCount; i++) {
                //公交路线组
                BusPath path = paths.get(i);
                //创建bean对象
                Log.d(TAG, "第" + i + "个公交路线");
                //时间和步行距离放进Bean中
                walk_dis = "步行" + path.getWalkDistance() + "米";
                time = "大约" + path.getDuration() / 60 + "分钟";

                //公交的乘坐方法
                List<BusStep> steps = path.getSteps();

                Log.d(TAG, "一共有" + steps.size() + "个公交乘坐方法");

                for (BusStep step :
                        steps) {

                    List<RouteBusLineItem> busLines = step.getBusLines();
                    for (RouteBusLineItem busLine :
                            busLines) {
                        //bus路线号码
                        //放入bean中
                        bus_line_number = busLine.toString();
                        //返回此公交换乘路段的出发站
                        //出发站名字放入bean中
                        start_station = "上车站：" + busLine.getDepartureBusStation().getBusStationName();
                        //返回此公交换乘路段的到达站
                        //到达站名字放入bean中
                        end_station = "下车站：" + busLine.getArrivalBusStation().getBusStationName();
                        //此公交换乘路段经过的站点数目（除出发站、到达站）
                        //经过多少站放入bean中
                        bus_station_count = "路过" + busLine.getPassStationNum() + "站";

                        busLineTotalInfoList.add(
                                new BusLineTotalInfo(
                                        bus_line_number,
                                        start_station,
                                        bus_station_count,
                                        end_station,
                                        time,
                                        walk_dis));
                    }

                }
                Log.d("----", "------------------------------------------");
            }
            setProgressComplete();
//                adapter.notifyAll();
            rv_way_to_anywhere.getRecycledViewPool().clear();


        } else {
            setProgressError();
            busLineTotalInfoList.clear();
            ToastUtil.showShort(getContext(), "没有找到回去的公交路线");
        }

        adapter.notifyDataSetChanged();
    }

    private void setProgressComplete() {
        mButton.setProgress(100);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private void setProgressError() {
        mButton.setProgress(-1);

        handler.sendEmptyMessageDelayed(0, 1000);


    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LocationFragment.this.getContext()).inflate(R.layout.rececler_bus_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

            BusLineTotalInfo info = busLineTotalInfoList.get(position);
            holder.tv_bus_line_number.setText(info.getBus_line_number());
            holder.tv_bus_station_count.setText(info.getBus_station_count());
            holder.tv_end_station.setText(info.getEnd_station());
            holder.tv_walk_dis.setText(info.getWalk_dis());
            holder.tv_start_station.setText(info.getStart_station());
            holder.tv_time.setText(info.getTime());

        }

        @Override
        public int getItemCount() {
            if (busLineTotalInfoList != null) {
                if (!busLineTotalInfoList.isEmpty()) {
                    return busLineTotalInfoList.size();
                }
            }
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_bus_line_number;
            TextView tv_start_station;
            TextView tv_time;
            TextView tv_bus_station_count;
            TextView tv_walk_dis;
            TextView tv_end_station;

            MyViewHolder(View itemView) {
                super(itemView);
                tv_bus_line_number = (TextView) itemView.findViewById(R.id.tv_bus_line_number);
                tv_start_station = (TextView) itemView.findViewById(R.id.tv_start_station);
                tv_end_station = (TextView) itemView.findViewById(R.id.tv_end_station);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                tv_bus_station_count = (TextView) itemView.findViewById(R.id.tv_bus_station_count);
                tv_walk_dis = (TextView) itemView.findViewById(R.id.tv_walk_dis);

            }
        }

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
