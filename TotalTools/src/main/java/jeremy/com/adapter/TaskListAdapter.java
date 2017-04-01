package jeremy.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jeremy.com.R;

/**
 * Created by Xin on 2017/4/1 0001,19:28.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyHolder> {
    private List<List<String>> mLists;
    private Context mContext;
    int mSize;

    public TaskListAdapter(Context context, List<List<String>> mapList) {
        mLists = mapList;
        mContext = context;
        mSize = mLists.size();
    }

    @Override
    public TaskListAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_task, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.MyHolder holder, int position) {
        List<String> list = mLists.get(mSize - 1 - position);
        String createTime = list.get(0);
        String listContent = list.get(1);
        holder.tv_task_color.setText(createTime);
        holder.mTv_task1.setText(listContent);
    }

    @Override
    public int getItemCount() {

        return mSize;
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTv_task1;
        TextView mTv_task2;
        TextView mTv_task3;
        TextView mTv_task4;
        TextView mTv_task5;
        TextView mTv_task6;
        TextView tv_task_color;

        public MyHolder(View view) {
            super(view);
            mTv_task1 = (TextView) view.findViewById(R.id.tv_task1);
            mTv_task2 = (TextView) view.findViewById(R.id.tv_task2);
            mTv_task3 = (TextView) view.findViewById(R.id.tv_task3);
            mTv_task4 = (TextView) view.findViewById(R.id.tv_task4);
            mTv_task5 = (TextView) view.findViewById(R.id.tv_task5);
            mTv_task6 = (TextView) view.findViewById(R.id.tv_task6);
            tv_task_color = (TextView) view.findViewById(R.id.tv_task_color);
        }
    }
}
