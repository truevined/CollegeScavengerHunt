package tonyebrown.whoowesme.databases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LOCATION = "location";

    public static final String TAG = NotesDbAdapter.class.getSimpleName();

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "notes.db";
    private static final String TABLE_NAME = "notes";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, "
            + "title text not null, body text not null);";

    private final Context mContext;
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
            Log.d(TAG, "onCreate() database");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db); Log.d(TAG, "onUpdate() database");
        }
    }

    public NotesDbAdapter(Context context) {
        this.mContext = context;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
}

//gotten from: http://mrbool.com/how-to-create-a-notepad-for-android-devices/27015#ixzz3yOhtji8C
