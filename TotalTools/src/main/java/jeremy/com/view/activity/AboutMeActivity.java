package jeremy.com.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import jeremy.com.R;
import jeremy.com.presenter.BasePresenter;
import jeremy.com.view.BaseActivity;


public class AboutMeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_github)
    TextView tv_github;

    public CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();

        tv_github.setOnClickListener(this);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about_me;
    }

    /**
     * 初始化ToolBar
     */
    private void initToolbar() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("很高兴你能看到这里");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_github:
                Intent it1 = new Intent(Intent.ACTION_VIEW, Uri.parse(tv_github.getText().toString()));
                it1.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(it1);
                break;
        }
    }
}
