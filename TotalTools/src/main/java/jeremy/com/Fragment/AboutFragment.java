package jeremy.com.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jeremy.com.R;
import jeremy.com.adapter.MyPagerAdapter;

/**
 * Created by Xin on 2017/3/19 0019,16:19.
 * 这个是设置界面的root Fragment
 * 他包含3个fragment AboutWayFragment，AboutReadFragment，AboutBLogUtilFragment
 */

public class AboutFragment extends Fragment {
    private List<Fragment> fragments;
    private String[] titles = {"路线设置", "阅读设置", "清单设置"};

    public AboutFragment() {
        super();
        fragments = new ArrayList<>();
        fragments.add(new AboutWayFragment());
        fragments.add(new AboutReadFragment());
        fragments.add(new AboutBLogUtilFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ViewPager vp_about = (ViewPager) view.findViewById(R.id.vp_about);
        TabLayout tl_about = (TabLayout) view.findViewById(R.id.tl_about);
        for (int i = 0; i < titles.length; i++) {
            tl_about.addTab(tl_about.newTab());
        }
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, titles);
        vp_about.setAdapter(adapter);

        return view;
    }

}
