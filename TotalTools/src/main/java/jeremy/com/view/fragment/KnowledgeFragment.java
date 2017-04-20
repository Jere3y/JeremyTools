package jeremy.com.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.content_viewPager)
    ViewPager mContentViewPager;
    private BaseFragment[] mFragments = new BaseFragment[3];

    public KnowledgeFragment() {
        mFragments[0] = new ZhihuFragment();
        mFragments[1] = new GankFragment();
        mFragments[2] = new IDailyFragment();
    }

    @Override
    protected void init(View view) {
        if (mFragments[0] == null) {
            mFragments[0] = new ZhihuFragment();
        }
        if (mFragments[1] == null) {
            mFragments[1] = new GankFragment();
        }
        if (mFragments[2] == null) {
            mFragments[2] = new IDailyFragment();
        }
        mContentViewPager.setOffscreenPageLimit(3);//设置至少3个fragment，防止重复创建和销毁，造成内存溢出
        mContentViewPager.setAdapter(new ViewPagerFgAdapter(getChildFragmentManager(), mFragments, "main_view_pager"));//给ViewPager设置适配器

    }

    @Override
    protected KnowledgePresenter createPresenter() {
        return mPresenter != null ? mPresenter : new KnowledgePresenter();
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
