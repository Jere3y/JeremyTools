package jeremy.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jeremy.com.R;
import jeremy.com.activity.CreateNewListActivity;

/**
 * 任务清单
 * Created by Xin on 2017/3/27 0027,22:27.
 */

public class TaskListFragment extends Fragment {
    private static final int REQUEST_NEW_LIST = 1;
    private FloatingActionButton fab_add_list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tast_list_fragment, container, false);
        fab_add_list = (FloatingActionButton) view.findViewById(R.id.fab_add_list);
        fab_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CreateNewListActivity.class), REQUEST_NEW_LIST);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_LIST) {
        }
    }
}
