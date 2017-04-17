package jeremy.com.view.iview;

import android.support.v7.widget.RecyclerView;

public interface IGankView {

    RecyclerView getRecyclerView();

    void setDataRefresh(boolean refresh);
}
