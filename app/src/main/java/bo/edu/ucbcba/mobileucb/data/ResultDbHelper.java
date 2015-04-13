package bo.edu.ucbcba.mobileucb.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static bo.edu.ucbcba.mobileucb.data.ResultContract.ResultEntry;

public class ResultDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9;

    static final String DATABASE_NAME = "mobile.db";

    public ResultDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + ResultEntry.TABLE_NAME_NOTIF + " (" +
                ResultEntry._ID + " INTEGER PRIMARY KEY," +
                ResultEntry.COLUMN_DESC_NOTIF + " TEXT NOT NULL," +
                ResultEntry.COLUMN_AVISO + " TEXT NOT NULL," +
                ResultEntry.COLUMN_DETALLE + " EXT NOT NULL;";
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ResultEntry.TABLE_NAME_NOTIF);
        onCreate(db);
    }
}
