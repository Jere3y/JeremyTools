package jeremy.com.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

import jeremy.com.R;

/**
 * BookView的配置项
 * Created by Xin on 2017/3/31 0031,22:11.
 */

public class BookViewOption {

    private int mHeight, mWidth;
    private int mVisibleHeight, mVisibleWidth;

    private int mPageLineCount;

    private int mLineSpace = 2;
    private int mMarginHeight = 30;
    private int mMarginWeight = 30;

    private int mEndPos = 0;
    private int mBeginPos = 0;

    private Paint mPageNumPaint;
    private Paint mTextPaint;
    private Paint mTimePaint;

    private int mTextFontSize = 60;
    private int mPageNumFontSize = 45;
    private int mTimeFontSize = 20;

    private Bitmap mBackGroundBitmap;

    private String mCharsetStr;

    private int mPeriod;
    private Typeface mTypeface;
    private Context mContext;

    public BookViewOption(Context context) {
        mContext = context;
        init();
    }

    private void init() {

        mLineSpace = 2;
        mMarginHeight = 30;
        mMarginWeight = 30;
        mTextFontSize = 60;
        mPageNumFontSize = 45;
        mTimeFontSize = 30;
        mEndPos = 0;
        mBeginPos = 0;
        mCharsetStr = "GBK";
        mPeriod = 4500;

        WindowManager windowService;
        windowService = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowService.getDefaultDisplay();
        mHeight = display.getHeight();
        mWidth = display.getWidth();

        mVisibleHeight = mHeight - mMarginHeight * 2
                - (mTimeFontSize > mPageNumFontSize ? mTimeFontSize : mPageNumFontSize);
        mVisibleWidth = mWidth - mMarginWeight * 2;

        mPageLineCount = mVisibleHeight / (mTextFontSize + mLineSpace);

        mPageNumPaint = new Paint();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimePaint = new Paint();

        mBackGroundBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bg);

        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FZBYSK.TTF");
    }

    public BookViewOption setHeight(int height) {
        mHeight = height;
        return this;
    }

    public BookViewOption setWidth(int width) {
        mWidth = width;
        return this;
    }

    public BookViewOption setVisibleHeight(int visibleHeight) {
        mVisibleHeight = visibleHeight;
        return this;
    }

    public BookViewOption setVisibleWidth(int visibleWidth) {
        mVisibleWidth = visibleWidth;
        return this;
    }

    public BookViewOption setPageLineCount(int pageLineCount) {
        mPageLineCount = pageLineCount;
        return this;
    }

    public BookViewOption setLineSpace(int lineSpace) {
        mLineSpace = lineSpace;
        return this;
    }

    public BookViewOption setMarginHeight(int marginHeight) {
        mMarginHeight = marginHeight;
        return this;
    }

    public BookViewOption setMarginWeight(int marginWeight) {
        mMarginWeight = marginWeight;
        return this;
    }

    public BookViewOption setEndPos(int endPos) {
        mEndPos = endPos;
        return this;
    }

    public BookViewOption setBeginPos(int beginPos) {
        mBeginPos = beginPos;
        return this;
    }

    public BookViewOption setPageNumPaint(Paint pageNumPaint) {
        mPageNumPaint = pageNumPaint;
        return this;
    }

    public BookViewOption setTextPaint(Paint textPaint) {
        mTextPaint = textPaint;
        return this;
    }

    public BookViewOption setTextFontSize(int textFontSize) {
        mTextFontSize = textFontSize;
        return this;
    }

    public BookViewOption setPageNumFontSize(int pageNumFontSize) {
        mPageNumFontSize = pageNumFontSize;
        return this;
    }

    public BookViewOption setTimeFontSize(int timeFontSize) {
        mTimeFontSize = timeFontSize;
        return this;
    }

    public BookViewOption setBackGroundBitmap(Bitmap backGroundBitmap) {
        mBackGroundBitmap = backGroundBitmap;
        return this;
    }


    public BookViewOption setCharsetStr(String charsetStr) {
        mCharsetStr = charsetStr;
        return this;
    }

    public BookViewOption setPeriod(int period) {
        mPeriod = period;
        return this;
    }

    public BookViewOption setTypeface(Typeface typeface) {
        mTypeface = typeface;
        return this;
    }
}
