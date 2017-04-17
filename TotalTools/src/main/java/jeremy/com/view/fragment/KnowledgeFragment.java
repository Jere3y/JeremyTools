package jeremy.com.view.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.adapter.ViewPagerFgAdapter;
import jeremy.com.presenter.actual.KnowledgePresenter;
import jeremy.com.view.BaseFragment;
import jeremy.com.view.iview.KnowledgeFragmentView;

/**
 * Created by Xin on 2017/4/16 0016,14:37.
 */

public class KnowledgeFragment extends BaseFragment<KnowledgeFragmentView, KnowledgePresenter> {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.content_viewPager)
    ViewPager mContentViewPager;
    private BaseFragment[] mFragments = new BaseFragment[3];

    @Override
    protected void init(View view) {
        mFragments[0] = new ZhihuFragment();
        mFragments[1] = new GankFragment();
        mFragments[2] = new DailyFragment();
        mContentViewPager.setOffscreenPageLimit(3);//设置至少3个fragment，防止重复创建和销毁，造成内存溢出
        mContentViewPager.setAdapter(new ViewPagerFgAdapter(getChildFragmentManager(), mFragments, "main_view_pager"));//给ViewPager设置适配器

    }

    @Override
    protected KnowledgePresenter createPresenter() {
        return new KnowledgePresenter();
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_knoledge;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
