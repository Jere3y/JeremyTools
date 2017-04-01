package jeremy.com.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import jeremy.com.R;
import jeremy.com.utils.LogUtil;
import jeremy.com.utils.SpUtil;


public class ReadView extends View {
    private static final String TAG = "ReadView";
    private Bitmap mCurrentPageBitmap;
    private Canvas mCurrentPageCanvas;
    private PageFactory pagefactory;
    private int font_size = 60;
    private SharedPreferences sp;
    private int[] position = new int[]{0, 0};
    private int width;
    private int height;
    private Context context;

    // TODO: 2017/3/31 0031 view 需要重写满足目录需求
    public ReadView(Context context) {
        super(context);
        this.context = context;
    }

    public ReadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ReadView(Context context, String path, int[] position) {
        super(context);
        init(path, position);
    }

    public void init(String path, int[] position) {
        int font_size = SpUtil.getInt(context, SpUtil.FONT_SIZE, 60);
        LogUtil.i(TAG, "font_size" + font_size);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
        LogUtil.i(width + ":宽", height + "：高");
        mCurrentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
        pagefactory = new PageFactory(context, width, height, font_size);
        try {
            LogUtil.i("start" + position[0], "end" + position[1]);
            pagefactory.setBgBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg));
            pagefactory.openBook(path, position);
            pagefactory.onDraw(mCurrentPageCanvas);
        } catch (Exception e) {
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawBitmap(mCurrentPageBitmap, 0, 0, null);
        canvas.restore();
    }

    public void setBackGround(Bitmap bitmap) {
        pagefactory.setBgBitmap(bitmap);
    }

    public void setDrawBitMap(Bitmap bitmap) {
        mCurrentPageBitmap = bitmap;
    }

    public void setOnPause() {
        position = pagefactory.getPosition();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("begin", position[0]);
        editor.putInt("end", position[1]);
        editor.apply();
        int fontSize = pagefactory.getTextFont();
        SharedPreferences.Editor editor2 = sp.edit();
        editor2.putInt("font_size", fontSize);
        editor2.apply();
    }

    //获得当前位置的方法
    public int[] getPosition() {
        return pagefactory.getPosition();
    }
    public void setFont_size(int font_size) {
        pagefactory.setTextFont(font_size);
    }

    public void setPresent(int present) {
        pagefactory.setPercent(present);
    }

    public void refresh() {
        pagefactory.onDraw(mCurrentPageCanvas);
        setDrawBitMap(mCurrentPageBitmap);
        invalidate();
    }

    //&& event.getY() > height * 2 / 3
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (event.getX() > width / 2) {
                pagefactory.nextPage();
                pagefactory.onDraw(mCurrentPageCanvas);
                setDrawBitMap(mCurrentPageBitmap);
            } else {
                pagefactory.prePage();
                pagefactory.onDraw(mCurrentPageCanvas);
                setDrawBitMap(mCurrentPageBitmap);
            }
            invalidate();
            return true;
        }
        return false;
    }

}
