package jeremy.com.routeway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;

import jeremy.com.R;
import jeremy.com.adapter.BusSegmentListAdapter;
import jeremy.com.utils.AMapUtil;

public class BusRouteDetailActivity extends Activity implements OnMapLoadedListener,
        OnMapClickListener, InfoWindowAdapter, OnInfoWindowClickListener, OnMarkerClickListener {
    private AMap mAMap;
    private MapView mMapView;
    private BusPath mBuspath;
    private BusRouteResult mBusRouteResult;
    private TextView mTitleBusRoute, mDesBusRoute;
    private ListView mBusSegmentList;
    private BusSegmentListAdapter mBusSegmentListAdapter;
    private LinearLayout mBuspathview;
    private BusRouteOverlay mBusrouteOverlay;
    private Toolbar tb_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        mMapView = (MapView) findViewById(R.id.route_map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        getIntentData();
        init();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBuspath = intent.getParcelableExtra("bus_path");
            mBusRouteResult = intent.getParcelableExtra("bus_result");
        }
    }

    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        registerListener();


        tb_route = (Toolbar) findViewById(R.id.tb_route);
        tb_route.setTitle("公交路线详情");
        tb_route.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tb_route.setNavigationIcon(R.drawable.back);
        tb_route.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleBusRoute = (TextView) findViewById(R.id.firstline);
        mDesBusRoute = (TextView) findViewById(R.id.secondline);
        String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mBuspath.getDistance());
        mTitleBusRoute.setText(dur + "(" + dis + ")");
        int taxiCost = (int) mBusRouteResult.getTaxiCost();
        mDesBusRoute.setText("打车约" + taxiCost + "元");
        mDesBusRoute.setVisibility(View.VISIBLE);
        mBuspathview = (LinearLayout) findViewById(R.id.bus_path);
        configureListView();
    }

    private void registerListener() {
        mAMap.setOnMapLoadedListener(this);
        mAMap.setOnMapClickListener(this);
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnInfoWindowClickListener(this);
        mAMap.setInfoWindowAdapter(this);
    }

    private void configureListView() {
        mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
        mBusSegmentListAdapter = new BusSegmentListAdapter(
                this.getApplicationContext(), mBuspath.getSteps());
        mBusSegmentList.setAdapter(mBusSegmentListAdapter);

    }

    public void onMapClick(View view) {
        mBuspathview.setVisibility(View.GONE);
        tb_route.setVisibility(View.GONE);
//		mBusMap.setVisibility(View.GONE);
        mMapView.setVisibility(View.VISIBLE);
        mAMap.clear();// 清理地图上的所有覆盖物
        mBusrouteOverlay = new BusRouteOverlay(this, mAMap,
                mBuspath, mBusRouteResult.getStartPos(),
                mBusRouteResult.getTargetPos());
        mBusrouteOverlay.removeFromMap();

    }

    @Override
    public void onMapLoaded() {
        if (mBusrouteOverlay != null) {
            mBusrouteOverlay.addToMap();
            mBusrouteOverlay.zoomToSpan();
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {

    }

    @Override
    public View getInfoContents(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {

    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        return false;
    }


}
