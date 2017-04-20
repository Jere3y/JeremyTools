package jeremy.com.view.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.presenter.actual.MainTabPresenter;
import jeremy.com.view.BaseActivity;
import jeremy.com.view.fragment.KnowledgeFragment;
import jeremy.com.view.fragment.NewsFragment;
import jeremy.com.view.fragment.ReadFragment;
import jeremy.com.view.fragment.RouteWayFragment;
import jeremy.com.view.fragment.TaskListFragment;
import jeremy.com.view.iview.IMainTabView;

/**
 * Created by Xin on 2017/4/15 0015,20:24.
 */

public class MainTabActivity extends BaseActivity<IMainTabView, MainTabPresenter> {


    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;
    private RouteWayFragment mRouteWayFragment;
    private ReadFragment mReadFragment;
    private TaskListFragment mTaskListFragment;
    private KnowledgeFragment mKnowledgeFragment;
    private NewsFragment mNewsFragment;
    private FragmentManager mFm;

    @Override
    protected void init() {

        mFm = getSupportFragmentManager();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.item_way:
                        if (mRouteWayFragment == null) {
                            mRouteWayFragment = new RouteWayFragment();
                        }
                        mFm.beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                                .replace(R.id.content, mRouteWayFragment)
                                .commit();
                        break;
                    case R.id.item_read:
                        if (mReadFragment == null) {
                            mReadFragment = new ReadFragment();
                        }
                        mFm.beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                                .replace(R.id.content, mReadFragment)
                                .commit();
                        break;
                    case R.id.item_list:
                        if (mTaskListFragment == null) {
                            mTaskListFragment = new TaskListFragment();
                        }
                        mFm.beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                                .replace(R.id.content, mTaskListFragment)
                                .commit();
                        break;
                    case R.id.item_knowledge:
                        if (mKnowledgeFragment == null) {
                            mKnowledgeFragment = new KnowledgeFragment();
                        }
                        mFm.beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                                .replace(R.id.content, mKnowledgeFragment)
                                .commit();
                        break;
                    case R.id.item_news:
                        if (mNewsFragment == null) {
                            mNewsFragment = new NewsFragment();
                        }
                        mFm.beginTransaction()
                                .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
                                .replace(R.id.content, mNewsFragment)
                                .commit();
                        break;
                }
                return true;
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.item_way);
    }

    @Override
    protected MainTabPresenter createPresenter() {
        return new MainTabPresenter();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main_tab;
    }
}
