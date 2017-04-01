package jeremy.com.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xin on 2017/2/27 0027.
 */

public class TaskListDbOpenHelper extends SQLiteOpenHelper {
    public TaskListDbOpenHelper(Context context) {
        super(context, "tasklist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseUtil.CREATE_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
