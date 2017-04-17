package jeremy.com.presenter.actual;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jeremy.com.R;
import jeremy.com.adapter.GankListAdapter;
import jeremy.com.model.gank.Gank;
import jeremy.com.model.gank.Meizhi;
import jeremy.com.model.gank.Video;
import jeremy.com.presenter.BasePresenter;
import jeremy.com.utils.DateUtils;
import jeremy.com.view.iview.IGankFgView;


public class GankFgPresenter extends BasePresenter<IGankFgView> {

    private Context context;
    private IGankFgView gankFgView;
    private RecyclerView mRecyclerView;
    private GridLayoutManager layoutManager;
    private GankListAdapter adapter;
    private List<Gank> list;
    private int page = 1;
    private int lastVisibleItem;
    private boolean isLoadMore = false; // 是否加载过更多

    public GankFgPresenter(Context context) {
        this.context = context;
    }

    public void getGankData() {
        gankFgView = getView();
        if (gankFgView != null) {
            mRecyclerView = gankFgView.getRecyclerView();
            layoutManager = gankFgView.getLayoutManager();

            if (isLoadMore) {
                page = page + 1;
            }

            Observable.zip(gankApi.getMeizhiData(page)
                    , gankApi.getVideoData(page)
                    , new BiFunction<Meizhi, Video, Meizhi>() {
                        @Override
                        public Meizhi apply(@NonNull Meizhi meizhi, @NonNull Video video) throws Exception {
                            for (Gank gankmeizhi : meizhi.getResults()) {
                                gankmeizhi.desc = gankmeizhi.desc + " " +
                                        getVideoDesc(gankmeizhi.getPublishedAt(), video.getResults());
                            }
                            return meizhi;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Meizhi>() {
                        @Override
                        public void accept(@NonNull Meizhi meizhi) throws Exception {
                            displayMeizhi(context, meizhi.getResults(), gankFgView, mRecyclerView);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            gankFgView.setDataRefresh(false);
                            Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }

    private void displayMeizhi(Context context, List<Gank> meiZhiList, IGankFgView gankFgView, RecyclerView recyclerView) {
        if (isLoadMore) {
            if (meiZhiList == null) {
                gankFgView.setDataRefresh(false);
                return;
            } else {
                list.addAll(meiZhiList);
            }
            adapter.notifyDataSetChanged();
        } else {
            list = meiZhiList;
            adapter = new GankListAdapter(context, list);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        gankFgView.setDataRefresh(false);
    }

    public void scrollRecycleView() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = layoutManager
                            .findLastVisibleItemPosition();
                    if (layoutManager.getItemCount() == 1) {
                        return;
                    }
                    if (lastVisibleItem + 1 == layoutManager
                            .getItemCount()) {
                        gankFgView.setDataRefresh(true);
                        isLoadMore = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getGankData();
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }


    //匹配同一天的福利描述和视频描述
    private String getVideoDesc(Date publishedAt, List<Gank> results) {
        String videoDesc = "";
        for (int i = 0; i < results.size(); i++) {
            Gank video = results.get(i);
            if (video.getPublishedAt() == null) video.setPublishedAt(video.getCreatedAt());
            if (DateUtils.isSameDate(publishedAt, video.getPublishedAt())) {
                videoDesc = video.getDesc();
                break;
            }
        }
        return videoDesc;
    }
}
