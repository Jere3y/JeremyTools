package jeremy.com.view.iview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public interface DailyFragmentView {

    void setDataRefresh(Boolean refresh);

    RecyclerView getRecyclerView();

    LinearLayoutManager getLayoutManager();
}
