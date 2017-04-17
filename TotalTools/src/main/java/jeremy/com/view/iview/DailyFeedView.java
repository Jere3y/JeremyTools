package jeremy.com.view.iview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public interface DailyFeedView {

    void setDataRefresh(Boolean refresh);

    RecyclerView getRecyclerView();

    GridLayoutManager getLayoutManager();
}