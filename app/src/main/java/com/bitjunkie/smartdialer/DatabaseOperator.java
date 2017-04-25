package com.bitjunkie.smartdialer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Patrick on 4/24/2017.
 */

public class DatabaseOperator extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB_LISTEDNUMBERS.db";
    protected static final String FIRST_TABLE_NAME = "LISTEDNUMBERS";

    public static final String CREATE_FIRST_TABLE = "create table if not exists "
            + FIRST_TABLE_NAME
            + " ( _id integer primary key autoincrement, number TEXT, name TEXT, address TEXT, city TEXT, zip TEXT, state TEXt, country TEXT);";

    public DatabaseOperator (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FIRST_TABLE);
        //db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DO NOTHING ATM CAUSE IDK WHAT THIS DOES
    }
}
