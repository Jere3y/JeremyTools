package jeremy.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jeremy.com.R;

/**
 * Created by Xin on 2017/3/27 0027,23:18.
 */

public class CreateNewListActivity extends AppCompatActivity {
    private final int CREATE_LIST_COMPLETE = 0;
    private Toolbar tb_create_list;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);
        tb_create_list = (Toolbar) findViewById(R.id.tb_create_list);
        tb_create_list.setNavigationIcon(R.drawable.back);
        tb_create_list.setTitle("新建清单");
        tb_create_list.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveList();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        saveList();
        super.onBackPressed();
    }

    private void saveList() {



    }
}
