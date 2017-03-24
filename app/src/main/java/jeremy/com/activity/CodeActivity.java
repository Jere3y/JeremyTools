package jeremy.com.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import jeremy.com.R;
import jeremy.com.view.CodeView;

/**
 * 阅读代码用的
 * Created by Xin on 2017/3/23 0023,15:24.
 */

public class CodeActivity extends AppCompatActivity {
    private static final String TAG = "CodeActivity";
    CodeView codeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_code);
        codeView = (CodeView) findViewById(R.id.mcodeview);

        File dir = null;
        Uri fileUri = getIntent().getData();
        Log.i(TAG, "代码源文件uri： " + fileUri);
        if (fileUri != null) {
            dir = new File(fileUri.getPath());
        }

        if (dir != null) {
            codeView.setDirSource(dir);
        } else {
            Log.e(TAG, "文件为空！");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_code) {
            if (!codeView.isEditable()) {
                item.setTitle("完成");
                codeView.setContentEditable(true);
            } else {
                item.setTitle("编辑");
                codeView.setContentEditable(false);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
