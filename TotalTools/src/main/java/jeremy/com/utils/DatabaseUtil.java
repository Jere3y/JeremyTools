package jeremy.com.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Xin on 2017/2/27 0027.
 * 包含所有操作数据库的封装,以及数据库的列名
 */

public class DatabaseUtil {
    private static final String TAG = "DatabaseUtil";

    private static SQLiteDatabase database;
    //第一个表
    public static final String BOOK_INFO_TBL = "book_info_tbl";
    //第一个表列名
    //_id唯一标识
    public static final String ID = "_id";
    public static final String BOOK_NAME = "book_name";
    public static final String PRE_START_POSITION = "pre_start_position";
    public static final String PRE_END_POSITION = "pre_end_position";

    //第二个表名
    public static final String BOOK_MARK_TBL = "book_mark_tbl";
    //第二个表列名
    public static final String BOOK_ID = "book_id";
    public static final String DATA1 = "data1";
    public static final String DATA2 = "data2";

    static final String CREATE_TABLE_1 = "create table book_info_tbl\n" +
            "(\n" +
            "_id integer primary key autoincrement,\n" +
            "book_name varchar(60) unique,\n" +
            "pre_start_position int,\n" +
            "pre_end_position int\n" +
            ");";
    static final String CREATE_TABLE_2 = "create table book_mark_tbl\n" +
            "(\n" +
            "_id integer primary key autoincrement,\n" +
            "book_id int,\n" +
            "data1 int,\n" +
            "data2 int\n" +
            ");";

    /**
     * SQLiteDatabase实例获取
     *
     * @param context
     * @return
     */
    private static SQLiteDatabase obtainDatabase(Context context) {
        if (database == null) {
            database = new BookMarkDbOpenHelper(context).getWritableDatabase();
            Log.i(TAG, "obtainDatabase: ");
        }
        return database;
    }

    /**
     * 在数据库中插入新书
     *
     * @param context
     * @param bookName
     */
    private static void insertNewBook(Context context, String bookName) {
        ContentValues newBook = new ContentValues();
        newBook.put(DatabaseUtil.BOOK_NAME, bookName);
        DatabaseUtil.obtainDatabase(context).insert(
                DatabaseUtil.BOOK_INFO_TBL, null, newBook);
    }

    /**
     * @param context
     * @param bookName 书名
     * @return 如果有阅读记录, 返回上次记录.如果没有, 返回{0,0}
     */
    public static int[] hasRead(Context context, String bookName) {
        int[] result = {0, 0};
        Cursor cursor = DatabaseUtil.obtainDatabase(context).query(
                DatabaseUtil.BOOK_INFO_TBL,
                new String[]{DatabaseUtil.PRE_START_POSITION, DatabaseUtil.PRE_END_POSITION},
                DatabaseUtil.BOOK_NAME + "=?",
                new String[]{bookName},
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                result[0] = cursor.getInt(0);
                result[1] = cursor.getInt(1);
                Log.i(TAG, "onCreate:取到上次阅读记录:" + result[0] + "----" + result[1]);
            }
        } else {
            ContentValues newBook = new ContentValues();
            newBook.put(DatabaseUtil.BOOK_NAME, bookName);
            insertNewBook(context, bookName);
            Log.i(TAG, "hasRead: 没有找到阅读记录,在数据库中新建记录");
        }
        cursor.close();
        return result;
    }

    public static void updatePosition(Context context, int[] position, String bookName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.PRE_START_POSITION, position[0]);
        values.put(DatabaseUtil.PRE_END_POSITION, position[1]);
        DatabaseUtil.obtainDatabase(context).update(DatabaseUtil.BOOK_INFO_TBL,
                values, DatabaseUtil.BOOK_NAME + "=?",
                new String[]{bookName});
        Log.i(TAG, "updatePosition----start:" + position[0] + "----end:" + position[1]);
    }
}
