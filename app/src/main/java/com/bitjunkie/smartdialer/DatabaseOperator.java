package com.bitjunkie.smartdialer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 *  
 * FILE NAME: DatabaseOperator.java
 * 
 * DESCRIPTION: This java file handles the Database operations
 * for the Smart Dialer application, including storing the number,
 * name, address, city, zip code, state, and country for a given searched number.
 *
 *   DATE       BY      DESCRIPTION
 * ======== ========== =============
 * 4/24/2017 Patrick R. Created the class
 * 
 */

public class DatabaseOperator extends SQLiteOpenHelper 
{
    // Gives the version number and names for the Database and tables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB_LISTEDNUMBERS.db";
    protected static final String FIRST_TABLE_NAME = "LISTEDNUMBERS";

    public static final String CREATE_FIRST_TABLE = "create table if not exists "
            + FIRST_TABLE_NAME
            + " ( _id integer primary key autoincrement, number TEXT, name TEXT, address TEXT, city TEXT, zip TEXT, state TEXt, country TEXT);";


    public DatabaseOperator (Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        db.execSQL(CREATE_FIRST_TABLE);
        //db.close();
    }


    @Override
    //Empty method for now, shouldn't need to be used
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        
    }
}
