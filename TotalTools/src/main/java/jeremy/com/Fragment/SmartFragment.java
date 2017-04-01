package jeremy.com.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jeremy.com.R;
import jeremy.com.utils.LogUtil;
import jeremy.com.utils.SpUtil;

/**
 * Created by Xin on 2017/3/26 0026,20:09.
 */

public class SmartFragment extends Fragment {

    TextView mTv_date;
    TextView mTv_weather;
    TextView mTv_weather_tip;
    LinearLayout mLl_today;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_smart, container, false);
        findView(view);
        LogUtil.d("SmartFragment", "onCreateView");
        initView();
        return view;
    }

    private void findView(View view) {
        mLl_today = (LinearLayout) view.findViewById(R.id.ll_today);
        mTv_date = (TextView) view.findViewById(R.id.tv_date);
        mTv_weather = (TextView) view.findViewById(R.id.tv_weather);
        mTv_weather_tip = (TextView) view.findViewById(R.id.tv_weather_tip);
    }

    private void initView() {
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日,EEEE", Locale.CHINA);
        String date = dateFormat.format(new Date());
        mTv_date.setText("今天是" + date + "：");
        mTv_weather.setText(SpUtil.getString(getContext(), SpUtil.SMART_WEATHER, getString(R.string.nodata)));
        mTv_weather_tip.setText(SpUtil.getString(getContext(), SpUtil.WEATHER_TIP, getString(R.string.nodata)));
    }

    public void setWeather(String weather, String tip) {
        DateFormat dateFormat = new SimpleDateFormat("MM月dd日,EEEE", Locale.CHINA);
        String date = dateFormat.format(new Date());
        mTv_date.setText("今天是" + date + "：");
        mTv_weather.setText(weather);
        mTv_weather_tip.setText(tip);
    }

}
