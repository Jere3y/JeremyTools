package jeremy.com.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import jeremy.com.R;
import jeremy.com.utils.LogUtil;
import jeremy.com.utils.SpUtil;
import jeremy.com.view.activity.SelectFromAmapActivity;

/**
 * Created by Xin on 2017/3/19 0019,17:24.
 * 设置界面
 */

public class AboutWayFragment extends Fragment implements View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener {
    private final int REQUEST_LOCATION_CODE = 0;
    private final String TAG = "AboutWayFragment";
    private TextView tv_set_other_address;
    private TextView tv_set_work_address;
    private TextView tv_set_home_address;
    private GeocodeSearch geocoderSearch;
    private String address;
    private int id;
    private double latitude;
    private double longitude;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_way, container, false);
        tv_set_home_address = (TextView) view.findViewById(R.id.tv_set_home_address);
        tv_set_work_address = (TextView) view.findViewById(R.id.tv_set_work_address);
        tv_set_other_address = (TextView) view.findViewById(R.id.tv_set_other_address);

        tv_set_home_address.setText(SpUtil.getString(getContext(), SpUtil.HOME_ADDRESS, "请点击设置家的地址"));
        tv_set_work_address.setText(SpUtil.getString(getContext(), SpUtil.WORK_ADDRESS, "请点击设置工作地址"));
        tv_set_other_address.setText(SpUtil.getString(getContext(), SpUtil.OTHER_ADDRESS, "请点击设置其他地址"));

        tv_set_other_address.setOnClickListener(this);
        tv_set_home_address.setOnClickListener(this);
        tv_set_work_address.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        geocoderSearch = new GeocodeSearch(getContext());
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SelectFromAmapActivity.class);
        intent.putExtra("id", v.getId());
        startActivityForResult(intent, REQUEST_LOCATION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (resultCode == SelectFromAmapActivity.RESULT_CODE) {
                latitude = data.getDoubleExtra("latitude", 0);
                longitude = data.getDoubleExtra("longitude", 0);
                id = data.getIntExtra("id", 0);
                //只有解析出经纬度，才执行反地理编码
                if (latitude != 0 && longitude != 0) {
                    //根据经纬度解析出地址
                    RegeocodeQuery query = new RegeocodeQuery(
                            new LatLonPoint(latitude, longitude),
                            100, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                }

            }
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            LogUtil.d(TAG, "解析地址成功！！！");
            address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
            switch (id) {
                case R.id.tv_set_home_address:
                    SpUtil.putString(getContext(), SpUtil.HOME_LATITUDE, latitude + "");
                    SpUtil.putString(getContext(), SpUtil.HOME_LONGITUDE, longitude + "");
                    SpUtil.putString(getContext(), SpUtil.HOME_ADDRESS, address);
                    tv_set_home_address.setText(address);
                    break;
                case R.id.tv_set_work_address:
                    SpUtil.putString(getContext(), SpUtil.WORK_LATITUDE, latitude + "");
                    SpUtil.putString(getContext(), SpUtil.WORK_LONGITUDE, longitude + "");
                    SpUtil.putString(getContext(), SpUtil.WORK_ADDRESS, address);
                    tv_set_work_address.setText(address);
                    break;
                case R.id.tv_set_other_address:
                    SpUtil.putString(getContext(), SpUtil.OTHER_LATITUDE, latitude + "");
                    SpUtil.putString(getContext(), SpUtil.OTHER_LONGITUDE, longitude + "");
                    SpUtil.putString(getContext(), SpUtil.OTHER_ADDRESS, address);
                    tv_set_other_address.setText(address);
                    break;
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
