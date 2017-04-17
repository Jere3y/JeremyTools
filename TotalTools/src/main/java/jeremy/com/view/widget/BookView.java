package jeremy.com.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 继承SurfaceView，完成电子书view
 * Created by Xin on 2017/3/31 0031,22:01.
 */

public class BookView extends SurfaceView {
    //holder
    private SurfaceHolder mHolder;
    //设置项
    private BookViewOption mOption;

    public BookView(Context context) {
        super(context);
        init();
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHolder = getHolder();
    }

    public BookView setOption(BookViewOption option) {
        mOption = option;
        return this;
    }

}
