package com.alinturbut.restauranter.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alinturbut.restauranter.helper.SQLHelper;
import com.alinturbut.restauranter.helper.contracts.RestaurantFactsContract;

/**
 * @author alinturbut.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Restauranter.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLHelper.createRestaurantFactsTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQLHelper.dropTableIfExists(RestaurantFactsContract.RestaurantFactsEntry.TABLE_NAME));
        onCreate(db);
    }
}
