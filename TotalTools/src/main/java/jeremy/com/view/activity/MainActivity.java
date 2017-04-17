package jeremy.com.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.util.ArrayList;
import java.util.List;

import jeremy.com.R;
import jeremy.com.utils.LogUtil;
import jeremy.com.utils.PermissionUtil;
import jeremy.com.utils.SpUtil;
import jeremy.com.view.fragment.AboutFragment;
import jeremy.com.view.fragment.ReadFragment;
import jeremy.com.view.fragment.RouteWayFragment;
import jeremy.com.view.fragment.SmartFragment;
import jeremy.com.view.fragment.TaskListFragment;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        , WeatherSearch.OnWeatherSearchListener {

    /**
     * 权限列表
     */
    private final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private static final String TAG = "MainActivity";
    private static final int FRAGMENT_SIZE = 5;
    private static final int SMART_FRAGMENT_INDEX = 0;
    private static final int ROUTE_WAY_FRAGMENT = 1;
    private static final int READ_FRAGMENT = 2;
    private static final int TASK_LIST_FRAGMENT = 3;
    private static final int ABOUT_FRAGMENT = 4;

    private DrawerLayout drawer_layout;
    private FragmentManager fragmentManager;
    private Toolbar tb_global;

    private Fragment[] fragmentList;

    private WeatherSearch mweathersearch;
    private TextView tv_weather_city;
    private TextView tv_weather_refresh_time;
    private TextView tv_weather_state;
    private TextView tv_weather_temp;
    private TextView tv_weather_wind;
    private TextView tv_weather_wind_level;
    private TextView tv_weather_humidity_level;

    private String currentCity;
    private List<TextView> weatherTextList;
    private WeatherSearchQuery queryLive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求权限
        PermissionUtil.CheckAndRequestPermission(this, permissions, 100);

        initNavigationAndWeather();
        initToolBar();
        initFragment();

//        showTaskListFragment();
        showSmartFragment();
    }

    private void initToolBar() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tb_global = (Toolbar) findViewById(R.id.tb_global);
        tb_global.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(tb_global);

        tb_global.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void initNavigationAndWeather() {
        NavigationView navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);
        findIdAndRefreshWeather(navigation_view);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();

        fragmentList = new Fragment[FRAGMENT_SIZE];

        fragmentList[SMART_FRAGMENT_INDEX] = new SmartFragment();
        fragmentList[ROUTE_WAY_FRAGMENT] = new RouteWayFragment();
        fragmentList[READ_FRAGMENT] = new ReadFragment();
        fragmentList[TASK_LIST_FRAGMENT] = new TaskListFragment();
        fragmentList[ABOUT_FRAGMENT] = new AboutFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = SMART_FRAGMENT_INDEX; i < FRAGMENT_SIZE; i++) {
            transaction.add(R.id.ll_content_main, fragmentList[i], i + "");
        }


        transaction.commit();

    }


    private void replaceThisFragment(int index) {

        FragmentTransaction ft = fragmentManager.beginTransaction();
        for (int i = SMART_FRAGMENT_INDEX; i < FRAGMENT_SIZE; ++i) {
            if (fragmentList[i] == null) {
                fragmentList[i] = fragmentManager.findFragmentByTag(i + "");
            }

            if (i == index) {
                ft.show(fragmentList[i]);
            } else {
                ft.hide(fragmentList[i]);
            }

        }
        ft.commit();

    }

    private void showSmartFragment() {
        replaceThisFragment(SMART_FRAGMENT_INDEX);
        tb_global.setTitle("智能首页");
    }

    private void showTaskListFragment() {
        replaceThisFragment(TASK_LIST_FRAGMENT);
        tb_global.setTitle("清单");
    }

    private void showAboutFragment() {
        replaceThisFragment(ABOUT_FRAGMENT);
        tb_global.setTitle("设置");
    }

    private void showReadFragment() {
        replaceThisFragment(READ_FRAGMENT);
        tb_global.setTitle("文件");
    }

    private void showRouteFragment() {
        replaceThisFragment(ROUTE_WAY_FRAGMENT);
        tb_global.setTitle("路线");
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_nav_item_exit)
                .setTitle("真的要退出吗？")
                .setNegativeButton("不退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("退出啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finish();
                    }
                })
                .setMessage("我要退出？")
                .create()
                .show();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_way) {
            showRouteFragment();
        } else if (itemId == R.id.item_read) {
            showReadFragment();
        } else if (itemId == R.id.item_list) {
            showTaskListFragment();
        } else if (itemId == R.id.item_about) {
            showAboutFragment();
        } else if (itemId == R.id.item_exit) {
            showExitDialog();
        }


        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    showExitDialog();
                }
                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_index:
                showSmartFragment();
                break;
            case R.id.menu_way:
                showRouteFragment();
                break;
            case R.id.menu_read:
                showReadFragment();
                break;
            case R.id.menu_list:
                showTaskListFragment();
                break;
        }
        return true;
    }

    /**
     * 找出所有导航栏中头布局的所有控件，用来显示天气
     * 初始化天气查询，用sp中存储的当前城市执行查询
     *
     * @param navigation_view navigation_view
     */
    private void findIdAndRefreshWeather(NavigationView navigation_view) {
        View headerView = navigation_view.getHeaderView(SMART_FRAGMENT_INDEX);
        tv_weather_city = (TextView) headerView.findViewById(R.id.tv_weather_city);
        tv_weather_refresh_time = (TextView) headerView.findViewById(R.id.tv_weather_refresh_time);
        tv_weather_state = (TextView) headerView.findViewById(R.id.tv_weather_state);
        tv_weather_temp = (TextView) headerView.findViewById(R.id.tv_weather_temp);
        tv_weather_wind = (TextView) headerView.findViewById(R.id.tv_weather_wind);
        tv_weather_wind_level = (TextView) headerView.findViewById(R.id.tv_weather_wind_level);
        tv_weather_humidity_level = (TextView) headerView.findViewById(R.id.tv_weather_humidity_level);
        headerView.findViewById(R.id.ll_header_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryWeather();
                tv_weather_refresh_time.setText("刷新天气中...");
            }
        });
        //下面是预报天气控件
        TextView tv_weather_today = (TextView) headerView.findViewById(R.id.tv_weather_today);
        TextView tv_weather_second_day = (TextView) headerView.findViewById(R.id.tv_weather_second_day);
        TextView tv_weather_third_day = (TextView) headerView.findViewById(R.id.tv_weather_third_day);
        TextView tv_weather_forth_day = (TextView) headerView.findViewById(R.id.tv_weather_forth_day);
        weatherTextList = new ArrayList<>();
        weatherTextList.add(tv_weather_today);
        weatherTextList.add(tv_weather_second_day);
        weatherTextList.add(tv_weather_third_day);
        weatherTextList.add(tv_weather_forth_day);

        currentCity = SpUtil.getString(this, SpUtil.CURRENT_CITY, "北京");
        tv_weather_city.setText(currentCity);

        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        queryWeather();

    }

    private void queryWeather() {
        if (queryLive == null) {
            queryLive = new WeatherSearchQuery(currentCity, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        }
        mweathersearch.setQuery(queryLive);
        mweathersearch.searchWeatherAsyn();
    }


    /**
     * 查询实时天气成功的回调
     *
     * @param weatherLiveResult 查询结果
     * @param rCode             结果码
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        LogUtil.d(TAG, "查询实时天气成功的回调:" + rCode);
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherLive = weatherLiveResult.getLiveResult();


                final long updateTime = System.currentTimeMillis();

                final String reportTime = weatherLive.getReportTime() + "发布";
                final String liveWeather = weatherLive.getWeather();
                final String temperature = weatherLive.getTemperature() + "℃";
                final String windDir = weatherLive.getWindDirection() + "风";
                final String windPower = weatherLive.getWindPower() + "级";
                final String humidity = weatherLive.getHumidity() + "%";

                Long lastTime = Long.parseLong(SpUtil.getString(this, SpUtil.LAST_UPDATE_TIME, "0"));

                tv_weather_refresh_time.setText(reportTime);
                tv_weather_state.setText(liveWeather);
                tv_weather_temp.setText(temperature);
                tv_weather_wind.setText(windDir);
                tv_weather_wind_level.setText(windPower);
                tv_weather_humidity_level.setText(humidity);

                String tip;
                if (liveWeather.equals("晴")) {
                    tip = "今天天气晴朗，阳光明媚，祝你拥有美好的一天-.-";
                } else if (liveWeather.contains("雨")) {
                    tip = "今天要下雨哦，出门注意带伞，淋雨会感冒的-.-";
                } else {
                    tip = "今天天气不错，会有好事发生哦-.-";
                }
                String smartWeather = "今天天气：" + liveWeather + "，" + temperature;

                ((SmartFragment) fragmentList[SMART_FRAGMENT_INDEX]).setWeather(smartWeather, tip);

                SpUtil.putString(getApplicationContext(), SpUtil.SMART_WEATHER, smartWeather);
                SpUtil.putString(getApplicationContext(), SpUtil.WEATHER_TIP, tip);

                mweathersearch.setQuery(new WeatherSearchQuery(currentCity,
                        WeatherSearchQuery.WEATHER_TYPE_FORECAST));
                mweathersearch.searchWeatherAsyn();


                if (lastTime - updateTime > 1000 * 60 * 30) {
                    new Thread(new Runnable() {
                        public void run() {
                            SpUtil.putString(getApplicationContext(), SpUtil.LAST_UPDATE_TIME, updateTime + "");
                            SpUtil.putString(getApplicationContext(), SpUtil.REPORT_TIME, reportTime);
                            SpUtil.putString(getApplicationContext(), SpUtil.LIVE_WEATHER, liveWeather);
                            SpUtil.putString(getApplicationContext(), SpUtil.TEMPERATURE, temperature);
                            SpUtil.putString(getApplicationContext(), SpUtil.WIND_DIR, windDir);
                            SpUtil.putString(getApplicationContext(), SpUtil.WIND_POWER, windPower);
                            SpUtil.putString(getApplicationContext(), SpUtil.HUMIDITY, humidity);
                        }
                    }).start();

                }
            }

        }
    }

    /**
     * 预报天气的回调
     *
     * @param localWeatherForecastResult localWeatherForecastResult
     * @param rCode                      i
     */
    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int rCode) {
        LogUtil.d(TAG, "预报天气的回调: " + rCode);
        if (1000 == rCode) {
            if (localWeatherForecastResult != null && localWeatherForecastResult.getForecastResult() != null) {
                LocalWeatherForecast forecastResult = localWeatherForecastResult.getForecastResult();
                List<LocalDayWeatherForecast> weatherForecast = forecastResult.getWeatherForecast();
                int size = weatherForecast.size();
                if (size == 4) {
                    for (int i = SMART_FRAGMENT_INDEX; i < size; i++) {
                        LocalDayWeatherForecast weather = weatherForecast.get(i);
                        String date = weather.getDate();
                        String week = weather.getWeek();
                        String dayTemp = weather.getDayTemp();
                        String nightTemp = weather.getNightTemp();
                        String dayWeather = weather.getDayWeather();
                        String nightWeather = weather.getNightWeather();
                        if (week.equals("1")) {
                            week = "一";
                        } else if (week.equals("2")) {
                            week = "二";
                        } else if (week.equals("3")) {
                            week = "三";
                        } else if (week.equals("4")) {
                            week = "四";
                        } else if (week.equals("5")) {
                            week = "五";
                        } else if (week.equals("6")) {
                            week = "六";
                        } else if (week.equals("7")) {
                            week = "日";
                        }
                        String formatStr = date + " " + "周" + week + "   " + dayTemp + "/" + nightTemp + "   " + dayWeather + "/" + nightWeather;
                        weatherTextList.get(i).setText(formatStr);
                    }
                }

            }
        }
    }

}
