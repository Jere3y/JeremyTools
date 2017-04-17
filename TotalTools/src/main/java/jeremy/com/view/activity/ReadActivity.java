package jeremy.com.view.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import jeremy.com.R;
import jeremy.com.utils.DatabaseUtil;
import jeremy.com.utils.LogUtil;
import jeremy.com.view.widget.ReadView;

/**
 * Created by Xin on 2017/3/22 0022,15:27.
 */

public class ReadActivity extends Activity {
    private final String TAG = "ReadActivity";

    private int[] prePosition = {0,0};

    private String path;
    //保存任务Timer
    private Timer timer;
    //保存进度延迟
    private long period = 45000;
    private ReadView read_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read);
        read_view = (ReadView) findViewById(R.id.read_view);

        File dir = null;
        Uri fileUri = getIntent().getData();
        if (fileUri != null) {
            path = fileUri.getPath();
            prePosition = DatabaseUtil.hasRead(this, path);
        }

        dir = new File(path);

        if (dir != null) {
            read_view.init(path, prePosition);
        } else {
            finish();
        }
        timer = new Timer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每45秒保存下状态
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                saveState();
            }
        }, 1000, period);
        LogUtil.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        LogUtil.i(TAG, "onPause: ");
        saveState();
        timer.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LogUtil.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


    private void saveState() {
        int[] position = read_view.getPosition();
        DatabaseUtil.updatePosition(this, position, path);
    }
}
