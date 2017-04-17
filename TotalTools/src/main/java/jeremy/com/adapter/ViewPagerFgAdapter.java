package jeremy.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import jeremy.com.view.BaseFragment;

public class ViewPagerFgAdapter extends FragmentPagerAdapter {

    private String tag;

    private BaseFragment[] fragments;


    public ViewPagerFgAdapter(FragmentManager supportFragmentManager, BaseFragment[] fragments, String tag) {
        super(supportFragmentManager);
        this.fragments = fragments;
        this.tag = tag;
    }

    @Override
    public Fragment getItem(int position) {

        return fragments[position];
    }


    @Override
    public int getCount() {
        return fragments.length;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tag.equals("main_view_pager")) {
            switch (position) {
                case 0:
                    return "知乎";
                case 1:
                    return "干货";
                case 2:
                    return "满足你的好奇心";
            }
        }
        return null;
    }
}
