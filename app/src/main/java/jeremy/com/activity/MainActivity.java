package jeremy.com.activity;

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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import jeremy.com.Fragment.AboutFragment;
import jeremy.com.Fragment.ReadFragment;
import jeremy.com.Fragment.TaskListFragment;
import jeremy.com.Fragment.WayToAnywhereFragment;
import jeremy.com.R;
import jeremy.com.utils.PermissionUtil;
import jeremy.com.utils.SpUtil;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        , WeatherSearch.OnWeatherSearchListener
        , OnMenuItemClickListener {

    /**
     * 权限列表
     */
    private final String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private static final String TAG = "MainActivity";
    private DrawerLayout drawer_layout;
    private FragmentManager fragmentManager;
    private Toolbar tb_global;

    //三个fragment
    private WayToAnywhereFragment wayToAnywhereFragment;
    private AboutFragment aboutFragment;
    private ReadFragment readFragment;
    private TaskListFragment taskListFragment;

    WeatherSearch mweathersearch;
    private TextView tv_weather_city;
    private TextView tv_weather_refresh_time;
    private TextView tv_weather_state;
    private TextView tv_weather_temp;
    private TextView tv_weather_wind;
    private TextView tv_weather_wind_level;
    private TextView tv_weather_humidity_level;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private String currentCity;
    private List<TextView> weatherTextList;
    private WeatherSearchQuery queryLive;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求权限
        PermissionUtil.CheckAndRequestPermission(this, permissions, 100);

        initNavigationView();
        initToolBar();
        initFragment();
        initMenuFragment();

        //开启事务，默认加载的是WayToAnywhereFragment
        showTaskListFragment();

    }

    private void initToolBar() {
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tb_global = (Toolbar) findViewById(R.id.tb_global);
        setSupportActionBar(tb_global);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, tb_global, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(this);
        findIdAndRefreshWeather(navigation_view);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        wayToAnywhereFragment = new WayToAnywhereFragment();
        readFragment = new ReadFragment();
        taskListFragment = new TaskListFragment();
        aboutFragment = new AboutFragment();
        fragmentList = new ArrayList<>();

        fragmentList.add(wayToAnywhereFragment);
        fragmentList.add(readFragment);
        fragmentList.add(taskListFragment);
        fragmentList.add(aboutFragment);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment fragment
                : fragmentList) {
            transaction.add(R.id.ll_content_main, fragment);
        }
        transaction.commit();
    }


    private void showThisFragmentHideOthers(Fragment showFragment) {
        FragmentTransaction t = fragmentManager.beginTransaction();
        for (Fragment fragment :
                fragmentList) {
            if (fragment == showFragment) {
                t.show(fragment);
            } else {
                t.hide(fragment);
            }
        }
        t.commit();
    }

    private void showTaskListFragment() {
        showThisFragmentHideOthers(taskListFragment);
        tb_global.setTitle("清单");
    }

    private void showAboutFragment() {
        showThisFragmentHideOthers(aboutFragment);
        tb_global.setTitle("设置");
    }

    private void showReadFragment() {
        showThisFragmentHideOthers(readFragment);
        tb_global.setTitle("文件");
    }

    private void showWayToAnywhereFragment() {
        showThisFragmentHideOthers(wayToAnywhereFragment);
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
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setMessage("我要退出？")
                .create()
                .show();
    }
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
//        mMenuDialogFragment.setItemLongClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item_way) {
            showWayToAnywhereFragment();
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
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                showWayToAnywhereFragment();
                break;
            case 2:
                showReadFragment();
                break;
            case 3:
                showTaskListFragment();
                break;
        }
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


    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.bn_cancel);

        MenuObject way = new MenuObject("路线");
        way.setResource(R.drawable.ic_item_way);

        MenuObject reader = new MenuObject("文件");
        reader.setResource(R.drawable.ic_nav_item_reader);

        MenuObject blog = new MenuObject("清单");
        blog.setResource(R.drawable.ic_nav_item_blog);


        menuObjects.add(close);
        menuObjects.add(way);
        menuObjects.add(reader);
        menuObjects.add(blog);
        return menuObjects;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 找出所有导航栏中头布局的所有控件，用来显示天气
     * 初始化天气查询，用sp中存储的当前城市执行查询
     *
     * @param navigation_view navigation_view
     */
    private void findIdAndRefreshWeather(NavigationView navigation_view) {
        View headerView = navigation_view.getHeaderView(0);
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
        Log.d(TAG, "查询实时天气成功的回调:" + rCode);
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                tv_weather_refresh_time.setText(weatherlive.getReportTime() + "发布");
                tv_weather_state.setText(weatherlive.getWeather());
                tv_weather_temp.setText(weatherlive.getTemperature() + "℃");
                tv_weather_wind.setText(weatherlive.getWindDirection() + "风");
                tv_weather_wind_level.setText(weatherlive.getWindPower() + "级");
                tv_weather_humidity_level.setText(weatherlive.getHumidity() + "%");

                mweathersearch.setQuery(new WeatherSearchQuery(currentCity,
                        WeatherSearchQuery.WEATHER_TYPE_FORECAST));
                mweathersearch.searchWeatherAsyn();
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
        Log.d(TAG, "预报天气的回调: " + rCode);
        if (1000 == rCode) {
            if (localWeatherForecastResult != null && localWeatherForecastResult.getForecastResult() != null) {
                LocalWeatherForecast forecastResult = localWeatherForecastResult.getForecastResult();
                List<LocalDayWeatherForecast> weatherForecast = forecastResult.getWeatherForecast();
                int size = weatherForecast.size();
                if (size == 4) {
                    for (int i = 0; i < size; i++) {
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
