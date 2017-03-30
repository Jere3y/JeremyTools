package jeremy.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jeremy.com.R;

/**
 *
 * Created by Xin on 2017/3/24 0024,18:30.
 */

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.MyHolder> {
    private Context mContext;


    public TaskRecyclerAdapter(Context context) {
        mContext = context;

    }

    @Override
    public TaskRecyclerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MyHolder extends RecyclerView.ViewHolder {


        MyHolder(View itemView) {
            super(itemView);

        }

    }
}
