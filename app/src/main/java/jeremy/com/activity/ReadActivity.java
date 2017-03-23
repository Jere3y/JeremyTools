package jeremy.com.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;

import jeremy.com.view.ReadView;

/**
 * Created by Xin on 2017/3/22 0022,15:27.
 */

public class ReadActivity extends Activity {
    private ReadView readView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        File dir = null;
        Uri fileUri = getIntent().getData();
        if (fileUri != null) {
            dir = new File(fileUri.getPath());
        }
        readView = null;
        if (dir != null) {
            readView = new ReadView(this, dir.getPath());
        } else
            finish();
        setContentView(readView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        readView.setOnPause();
    }
}
