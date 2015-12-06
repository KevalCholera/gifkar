package com.smartsense.gifkar.utill;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_SHOP = "shop";
    public static final String TABLE_PRODUCT = "product";

    private static final String DATABASE_NAME = "gifkar.db";
    private static String DB_PATH = Constants.DB_PATH;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHOP_ID = "shop_id";
    public static final String COLUMN_SHOP_NAME = "shop_name";
    public static final String COLUMN_CUT_OF_TIME = "cut_of_time";
    public static final String COLUMN_MIN_ORDER = "min_order";
    public static final String COLUMN_MID_NIGHT_DEL = "mid_night_delivery";
    public static final String COLUMN_SAME_DAY_DELIVERY = "same_day_delivery";
    public static final String COLUMN_RATING= "rating";
    public static final String COLUMN_OPEN_AT = "open_at";
    public static final String COLUMN_CLOSE_AT = "close_at";
    public static final String COLUMN_DELIVERY_FROM = "delivery_from";
    public static final String COLUMN_DELIVERY_TO = "delivery_to";
    public static final String COLUMN_REMOTE_DELIVERY = "remote_delivery";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final int DATABASE_VERSION = 1;
        // Database creation sql statement
    private static final String TABLE_SHOPLIST = "create table "
            + TABLE_SHOP + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_SHOP_ID + " integer, "
            + COLUMN_SHOP_NAME + " text,  "
            + COLUMN_CUT_OF_TIME + " text,  "
            + COLUMN_MIN_ORDER + " text,  "
            + COLUMN_MID_NIGHT_DEL + " text,  "
            + COLUMN_SAME_DAY_DELIVERY + " text,  "
            + COLUMN_REMOTE_DELIVERY + " text,  "
            + COLUMN_RATING + " text,  "
            + COLUMN_OPEN_AT + " text,  "
            + COLUMN_CLOSE_AT + " text,  "
            + COLUMN_DELIVERY_TO + " text,  "
            + COLUMN_DELIVERY_FROM + " text,  "
            + COLUMN_CATEGORY_ID + " text,  "
            + COLUMN_CATEGORY_NAME + " text,  "
            + ");";

    private SQLiteDatabase myDataBase;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_SHOPLIST);
//        database.execSQL(TABLE_PRODUCTLIST);
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); // OPEN_READONLY
        return myDataBase;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPLIST);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTLIST);
        onCreate(db);
    }
}