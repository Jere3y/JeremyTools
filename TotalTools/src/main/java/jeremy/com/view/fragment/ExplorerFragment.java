package jeremy.com.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jeremy.com.R;
import jeremy.com.adapter.RecyclerExplorerAdapter;
import jeremy.com.bean.FileInfo;
import jeremy.com.utils.SpUtil;
import jeremy.com.utils.ToastUtil;
import jeremy.com.view.activity.CodeActivity;
import jeremy.com.view.activity.ReadActivity;

/**
 * 这个是文件浏览的Fragment
 * Created by Xin on 2017/3/21 0021,15:36.
 */

public class ExplorerFragment extends Fragment {

    private RecyclerView rv_file_explorer;
    private TextView tv_show_location;
    private List<FileInfo> fileInfoList;
    private File rootFile;
    private String rootPath;

    private String currentPath;
    private RecyclerExplorerAdapter explorerAdapter;
    private Intent intent;
    private Button bn_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_explorer, container, false);
        initView(view);
        intent = new Intent();
        rootFile = Environment.getExternalStorageDirectory();
        if (rootFile != null) {
            rootPath = rootFile.getPath();
            currentPath = SpUtil.getString(getContext(), SpUtil.CURRENT_PATH, rootPath);
        }


        updateFileInfoList();

        explorerAdapter = new RecyclerExplorerAdapter(getContext(), fileInfoList);
        explorerAdapter.setOnItemClickListener(new RecyclerExplorerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FileInfo fileInfo = fileInfoList.get(position);
                String name = fileInfo.getName();
                if (fileInfo.isDir()) {
                    currentPath = fileInfo.getPath();
                    updateFileInfoList();
                    explorerAdapter.notifyDataSetChanged();
                } else if (name.endsWith(".java")) {
                    intent.setData(Uri.parse(fileInfo.getPath()));
                    intent.setClass(getActivity(), CodeActivity.class);
                    startActivity(intent);
                } else if (name.endsWith(".txt")) {
                    intent.setData(Uri.parse(fileInfo.getPath()));
                    intent.setClass(getActivity(), ReadActivity.class);
                    startActivity(intent);
                } else if (name.endsWith(".md")) {

                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        rv_file_explorer.setAdapter(explorerAdapter);
        rv_file_explorer.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_file_explorer.setItemAnimator(new DefaultItemAnimator());
        rv_file_explorer.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        SpUtil.putString(getContext(), SpUtil.CURRENT_PATH, currentPath);
    }

    private void initView(View view) {
        tv_show_location = (TextView) view.findViewById(R.id.tv_show_location);
        rv_file_explorer = (RecyclerView) view.findViewById(R.id.rv_file_explorer);
        view.findViewById(R.id.bn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPath.equals(rootPath)) {
                    ToastUtil.showShort(getContext(), "已经是根目录了，不能再向上了");
                    return;
                }
                currentPath = new File(currentPath).getParentFile().getPath();
                updateFileInfoList();
                explorerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void updateFileInfoList() {

        tv_show_location.setText(currentPath);
        if (currentPath != null) {
            if (fileInfoList == null) {
                fileInfoList = new ArrayList<>();
            } else {
                fileInfoList.clear();
            }
            File currentFile = new File(currentPath);
            File[] files = currentFile.listFiles();
            for (File file :
                    files) {
                String name = file.getName();
                if (!name.startsWith(".")) {
                    boolean isDir = file.isDirectory();
                    String path = file.getPath();
                    fileInfoList.add(new FileInfo(path, name, isDir));
                }
            }
        }
        Collections.sort(fileInfoList, new MyFileComparator());

    }


    private class MyFileComparator implements Comparator<FileInfo> {
        public int compare(FileInfo file1, FileInfo file2) {
            //忽略大小写排序
            if (file1.getName().compareToIgnoreCase(file2.getName()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
