package jeremy.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jeremy.com.R;
import jeremy.com.utils.DatabaseUtil;
import jeremy.com.utils.LogUtil;

/**
 * Created by Xin on 2017/3/27 0027,23:18.
 */

public class CreateNewListActivity extends AppCompatActivity {
    private final int CREATE_LIST_COMPLETE = 0;
    private final static String TAG = "CreateNewListActivity";
    private Toolbar tb_create_list;
    private Intent mIntent;
    private EditText et_new_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);
        findId();
        initToolbar();
    }

    private void findId() {
        tb_create_list = (Toolbar) findViewById(R.id.tb_create_list);
        et_new_list = (EditText) findViewById(R.id.et_new_list);
    }

    private void initToolbar() {

        tb_create_list.setNavigationIcon(R.drawable.back);
        tb_create_list.setTitle("新建清单");
        tb_create_list.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveList();
        closeDatabaseFinishThis();
    }


    private void saveList() {
        String content = et_new_list.getText().toString();
        if (!content.isEmpty()) {
            setResult(RESULT_OK);
            LogUtil.i(TAG, "不是空的内容，插入数据库");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            DatabaseUtil.insertNewTaskList(this, dateFormat.format(date), content);
        }
    }

    private void closeDatabaseFinishThis() {
        DatabaseUtil.closeTaskListDatabase();
        finish();
    }
}
