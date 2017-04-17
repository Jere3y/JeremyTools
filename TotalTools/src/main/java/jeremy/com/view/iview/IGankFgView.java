package jeremy.com.view.iview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public interface IGankFgView {

    void setDataRefresh(Boolean refresh);

    GridLayoutManager getLayoutManager();

    RecyclerView getRecyclerView();
}
