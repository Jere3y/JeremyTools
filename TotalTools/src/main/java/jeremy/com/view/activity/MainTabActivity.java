package jeremy.com.view.activity;

import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.presenter.actual.MainTabPresenter;
import jeremy.com.view.BaseActivity;
import jeremy.com.view.fragment.KnowledgeFragment;
import jeremy.com.view.fragment.NewsFragment;
import jeremy.com.view.fragment.ReadFragment;
import jeremy.com.view.fragment.RouteWayFragment;
import jeremy.com.view.fragment.TaskListFragment;
import jeremy.com.view.iview.MainTabView;

/**
 * Created by Xin on 2017/4/15 0015,20:24.
 */

public class MainTabActivity extends BaseActivity<MainTabView, MainTabPresenter> {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;
    private AppBarLayout mAppBar;
    private Toolbar mToolbar;

    private final Class[] mFragments = {RouteWayFragment.class
            , ReadFragment.class
            , TaskListFragment.class
            , KnowledgeFragment.class
            , NewsFragment.class
    };
    private final String[] mTabTitleString = {"路线"
            , "文件"
            , "清单"
            , "干货"
            , "新闻"
    };
    private final int[] mImageViewArray = {R.mipmap.ic_item_way
            , R.mipmap.ic_nav_item_reader
            , R.mipmap.ic_nav_item_blog
            , R.mipmap.ic_nav_item_blog
            , R.mipmap.ic_nav_item_blog
    };

    @Override
    protected void init() {

        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);

        //得到fragment的个数
        int count = mFragments.length;

        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTabTitleString[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragments[i], null);
            //设置Tab按钮的背景
        }
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null && mAppBar != null) {
            setSupportActionBar(mToolbar); //把Toolbar当做ActionBar给设置
            if (canBack()) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                    actionBar.setDisplayHomeAsUpEnabled(true);//设置ActionBar一个返回箭头，主界面没有，次级界面有
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }


    }

    @Override
    protected MainTabPresenter createPresenter() {
        return new MainTabPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_tab;
    }

    private View getTabItemView(int index) {
        View view = View.inflate(this, R.layout.item_tab_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab_icon);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.tv_tab);
        textView.setText(mTabTitleString[index]);

        return view;
    }

}
