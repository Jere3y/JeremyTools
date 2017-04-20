package jeremy.com.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.presenter.actual.DailyFgPresenter;
import jeremy.com.view.BaseFragment;
import jeremy.com.view.iview.IDailyFragmentView;

public class IDailyFragment extends BaseFragment<IDailyFragmentView, DailyFgPresenter> implements IDailyFragmentView {

    private LinearLayoutManager mLayoutManager;
    @BindView(R.id.content_list)
    RecyclerView content_list;

    @Override
    protected DailyFgPresenter createPresenter() {
        return new DailyFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void init(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        content_list.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getDailyTimeLine("0");
        mPresenter.scrollRecycleView();
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getDailyTimeLine("0");
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return content_list;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }
}
