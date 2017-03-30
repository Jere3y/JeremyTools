package jeremy.com.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xin on 2017/2/27 0027.
 */

public class BookMarkDbOpenHelper extends SQLiteOpenHelper {
    public BookMarkDbOpenHelper(Context context) {
        super(context, "bookmark.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseUtil.CREATE_TABLE_1);
        db.execSQL(DatabaseUtil.CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
