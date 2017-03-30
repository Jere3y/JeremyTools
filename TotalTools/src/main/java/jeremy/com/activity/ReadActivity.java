package jeremy.com.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import jeremy.com.utils.DatabaseUtil;
import jeremy.com.view.ReadView;

/**
 * Created by Xin on 2017/3/22 0022,15:27.
 */

public class ReadActivity extends Activity {
    private final String TAG = "ReadActivity";

    private ReadView readView;
    private int[] prePosition;


    private String path;
    //保存任务Timer
    private Timer timer;
    //保存进度延迟
    private long period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        File dir = null;
        Uri fileUri = getIntent().getData();
        if (fileUri != null) {
            path = fileUri.getPath();
            prePosition = DatabaseUtil.hasRead(this, path);
        }

        dir = new File(path);
        readView = null;
        if (dir != null) {
            readView = new ReadView(this, path, prePosition);
        } else {
            finish();
        }
        setContentView(readView);
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
        }, 1000, 45 * 1000);
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        saveState();
        timer.cancel();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }


    private void saveState() {
        int[] position = readView.getPosition();
        DatabaseUtil.updatePosition(this, position, path);
    }
}
