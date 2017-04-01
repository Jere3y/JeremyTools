package jeremy.com.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jeremy.com.R;
import jeremy.com.activity.CreateNewListActivity;
import jeremy.com.adapter.TaskListAdapter;
import jeremy.com.utils.DatabaseUtil;
import jeremy.com.utils.LogUtil;

/**
 * 任务清单
 * Created by Xin on 2017/3/27 0027,22:27.
 */

public class TaskListFragment extends Fragment {
    private static final String TAG = "TaskListFragment";
    private static final int REQUEST_NEW_LIST = 1;
    private FloatingActionButton fab_add_list;
    private TextView mTv_task_tip1;
    private TextView mTv_task_tip2;
    private RecyclerView rv_task_list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tast_list_fragment, container, false);
        fab_add_list = (FloatingActionButton) view.findViewById(R.id.fab_add_list);
        mTv_task_tip2 = (TextView) view.findViewById(R.id.tv_task_tip2);
        mTv_task_tip1 = (TextView) view.findViewById(R.id.tv_task_tip1);
        rv_task_list = (RecyclerView) view.findViewById(R.id.rv_task_list);
        rv_task_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_task_list.setItemAnimator(new DefaultItemAnimator());

        fab_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateNewListActivity.class);
                startActivityForResult(intent, REQUEST_NEW_LIST);
            }
        });
        updateFromSQL();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_LIST) {
            if (resultCode == CreateNewListActivity.RESULT_OK) {
                updateFromSQL();
            }
        }
    }

    private void updateFromSQL() {
        LogUtil.d(TAG, "在数据库读取清单数据");
        List<List<String>> lists = new ArrayList<>();
        Cursor cursor = DatabaseUtil.queryAllList(getContext());
        if (cursor != null) {
            mTv_task_tip1.setVisibility(View.GONE);
            mTv_task_tip2.setVisibility(View.GONE);
            rv_task_list.setVisibility(View.VISIBLE);
            while (cursor.moveToNext()) {
                List<String> list = new ArrayList<>();
                String createTime = cursor.getString(1);
                String listContent = cursor.getString(2);

                list.add(createTime);
                list.add(listContent);
                lists.add(list);
            }

            TaskListAdapter adapter = new TaskListAdapter(getContext(), lists);
            rv_task_list.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
            rv_task_list.setAdapter(adapter);
        }
    }
}
