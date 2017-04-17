package jeremy.com.presenter.actual;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jeremy.com.R;
import jeremy.com.adapter.GankActivityAdapter;
import jeremy.com.model.gank.Gank;
import jeremy.com.model.gank.GankData;
import jeremy.com.presenter.BasePresenter;
import jeremy.com.view.iview.IGankView;

public class GankPresenter extends BasePresenter<IGankView> {

    private Context context;
    private IGankView gankView;
    private RecyclerView recyclerView;

    public GankPresenter(Context context) {
        this.context = context;
    }

    public void getGankList(int year, int month, int day) {
        gankView = getView();
        if (gankView != null) {
            recyclerView = gankView.getRecyclerView();

            gankApi.getGankData(year, month, day)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<GankData>() {
                        @Override
                        public void accept(@NonNull GankData gankData) throws Exception {
                            displayGankList(context, gankData.results.getAllResults(), gankView, recyclerView);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            Toast.makeText(context, R.string.gank_load_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void displayGankList(Context context, List<Gank> gankList, IGankView gankView, RecyclerView recyclerView) {
        GankActivityAdapter adapter = new GankActivityAdapter(context, gankList);
        recyclerView.setAdapter(adapter);
        gankView.setDataRefresh(false);
    }
}
