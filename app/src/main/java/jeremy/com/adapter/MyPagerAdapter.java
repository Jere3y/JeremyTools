package jeremy.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Comparator;
import java.util.List;

import jeremy.com.bean.FileInfo;

/**
 * 根据传入的list和title生成相应的adapter
 * Created by Xin on 2017/3/19 0019,12:31.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    private final String[] titles;
    private List<Fragment> fragments;

    /**
     * @param fragmentManager getSupportFragmentManager（）
     * @param fragments       所有fragment
     * @param titles          没有fragment的标题
     */
    public MyPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragments, String[] titles) {
        super(fragmentManager);
        if (fragments.size() != titles.length) {
            throw new IllegalArgumentException("tab数量和fragment数量不等。");
        }
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    //实现对文件列表的排序功能
    private class MyFileComparator implements Comparator<FileInfo> {
        public int compare(FileInfo file1, FileInfo file2) {
            //忽略大小写排序
            if (file1.getName().compareToIgnoreCase(file2.getName()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
