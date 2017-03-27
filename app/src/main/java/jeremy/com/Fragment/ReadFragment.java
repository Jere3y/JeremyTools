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
 * 文件浏览器界面，也是第二个功能“阅读”的主界面，
 * 包括两个fragment:RecentlyReadFragment和ExplorerFragment.
 * Created by Xin on 2017/3/21 0021,15:14.
 */

public class ReadFragment extends Fragment {
    List<Fragment> fragments;
    private String[] titles = {"最近阅读", "文件浏览"};

    public ReadFragment() {
        super();
        fragments = new ArrayList<>();
        fragments.add(new RecentlyReadFragment());
        fragments.add(new ExplorerFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_read, container, false);
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
