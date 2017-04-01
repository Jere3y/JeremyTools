package jeremy.com.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Xin on 2017/2/27 0027.
 * 包含所有操作数据库的封装,以及数据库的列名
 */

public class DatabaseUtil {
    private static final String TAG = "DatabaseUtil";


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

    //第三个表
    public static final String TASK_LIST_TBL = "task_list_tbl";
    //第二个表列名
    public static final String CREATE_LIST_TIME = "create_time";
    public static final String LIST_CONTENT = "list_content";

    public static final String CREATE_TABLE_1 = "create table book_info_tbl\n" +
            "(\n" +
            "_id integer primary key autoincrement,\n" +
            "book_name varchar(60) unique,\n" +
            "pre_start_position int,\n" +
            "pre_end_position int\n" +
            ");";
    public static final String CREATE_TABLE_2 = "create table book_mark_tbl\n" +
            "(\n" +
            "_id integer primary key autoincrement,\n" +
            "book_id int,\n" +
            "data1 int,\n" +
            "data2 int\n" +
            ");";
    public static final String CREATE_LIST_TABLE = "create table task_list_tbl\n" +
            "(\n" +
            "_id integer primary key autoincrement,\n" +
            "create_time varchar(30),\n" +
            "list_content text\n" +
            ");";
    private static SQLiteDatabase taskListDatabase;
    private static SQLiteDatabase bookMarkDatabase;

    /**
     * SQLiteDatabase实例获取
     *
     * @param context
     * @return
     */
    private static SQLiteDatabase obtainBookMarkDatabase(Context context) {
        if (bookMarkDatabase == null) {
            bookMarkDatabase = new BookMarkDbOpenHelper(context).getWritableDatabase();
            LogUtil.i(TAG, "获取书签数据库: ");
        }

        return bookMarkDatabase;
    }

    private static SQLiteDatabase obtainTaskListDatabase(Context context) {
        if (taskListDatabase == null) {
            taskListDatabase = new TaskListDbOpenHelper(context).getWritableDatabase();
            LogUtil.i(TAG, "获取任务清单数据库: ");
        }

        return taskListDatabase;
    }

    public static void insertNewTaskList(Context context, String time, String content) {
        ContentValues newList = new ContentValues();
        newList.put(DatabaseUtil.CREATE_LIST_TIME, time);
        newList.put(DatabaseUtil.LIST_CONTENT, content);
        DatabaseUtil.obtainTaskListDatabase(context)
                .insert(DatabaseUtil.TASK_LIST_TBL, null, newList);
        LogUtil.i(TAG, "插入新清单完成！");
    }

    public static Cursor queryAllList(Context context) {
        Cursor result;
        SQLiteDatabase sqLiteDatabase = obtainTaskListDatabase(context);
        result = sqLiteDatabase.query(DatabaseUtil.TASK_LIST_TBL, null, null, null, null, null, null);
        if (result.getCount() <= 0) {
            result = null;
        }
        LogUtil.i(TAG, "数据库是不是空的：" + result);
        return result;
    }

    public static void closeTaskListDatabase() {
        if (taskListDatabase != null) {
            taskListDatabase.close();
            taskListDatabase = null;
            LogUtil.i(TAG, "closeTaskListDatabase");
        }
    }
    /**
     * @param context
     * @param bookName 书名
     * @return 如果有阅读记录, 返回上次记录.如果没有, 返回{0,0}
     */
    public static int[] hasRead(Context context, String bookName) {
        int[] result = {0, 0};
        Cursor cursor = DatabaseUtil.obtainBookMarkDatabase(context).query(
                DatabaseUtil.BOOK_INFO_TBL,
                new String[]{DatabaseUtil.PRE_START_POSITION, DatabaseUtil.PRE_END_POSITION},
                DatabaseUtil.BOOK_NAME + "=?",
                new String[]{bookName},
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                result[0] = cursor.getInt(0);
                result[1] = cursor.getInt(1);
                LogUtil.i(TAG, "onCreate:取到上次阅读记录:" + result[0] + "----" + result[1]);
            }
        } else {
            ContentValues newBook = new ContentValues();
            newBook.put(DatabaseUtil.BOOK_NAME, bookName);
            insertNewBook(context, bookName);
            LogUtil.i(TAG, "hasRead: 没有找到阅读记录,在数据库中新建记录");
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
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
        DatabaseUtil.obtainBookMarkDatabase(context).insert(
                DatabaseUtil.BOOK_INFO_TBL, null, newBook);
    }


    public static void updatePosition(Context context, int[] position, String bookName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.PRE_START_POSITION, position[0]);
        values.put(DatabaseUtil.PRE_END_POSITION, position[1]);
        DatabaseUtil.obtainBookMarkDatabase(context).update(DatabaseUtil.BOOK_INFO_TBL,
                values, DatabaseUtil.BOOK_NAME + "=?",
                new String[]{bookName});
        LogUtil.i(TAG, "updatePosition----start:" + position[0] + "----end:" + position[1]);
    }
}
