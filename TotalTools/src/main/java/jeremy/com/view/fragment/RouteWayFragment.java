package jeremy.com.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jeremy.com.R;
import jeremy.com.adapter.MyPagerAdapter;

/**
 * 路线功能的root fragment
 * 包括两个fragment，ByBusFragment和SelectLocationFragment
 * Created by Xin on 2017/3/19 0019,12:15.
 */

public class RouteWayFragment extends Fragment {
    private ArrayList<Fragment> fragments;
    private String[] titles = {"路线", "地图"};

    public RouteWayFragment() {
        super();
        fragments = new ArrayList<>();
        fragments.add(new LocationFragment());
        fragments.add(new SelectLocationFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_way_to_anywhere, container, false);

        ViewPager vp_pager = (ViewPager) view.findViewById(R.id.vp_pager);
        TabLayout tl_tab = (TabLayout) view.findViewById(R.id.tl_tab);
        //根据标题数量动态添加标签
        for (int i = 0; i < titles.length; i++) {
            tl_tab.addTab(tl_tab.newTab());
        }

        MyPagerAdapter adapter =
                new MyPagerAdapter(getChildFragmentManager(), fragments, titles);
        vp_pager.setAdapter(adapter);

        return view;
    }

}
