package jeremy.com.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.presenter.actual.GankFgPresenter;
import jeremy.com.view.BaseFragment;
import jeremy.com.view.iview.IGankFgView;

public class GankFragment extends BaseFragment<IGankFgView, GankFgPresenter> implements IGankFgView {

    private GridLayoutManager gridLayoutManager;

    @BindView(R.id.content_list)
    RecyclerView content_list;

    @Override
    protected GankFgPresenter createPresenter() {
        return new GankFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void init(View rootView) {
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        content_list.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getGankData();
        mPresenter.scrollRecycleView();
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getGankData();
    }

    @Override
    public void setDataRefresh(Boolean refresh) {
        setRefresh(refresh);
    }

    @Override
    public GridLayoutManager getLayoutManager() {
        return gridLayoutManager;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return content_list;
    }
}
