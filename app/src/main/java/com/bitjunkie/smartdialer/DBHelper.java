package com.bitjunkie.smartdialer;

/**
 * Created by Cameron on 4/21/2017.
 */

public class DBHelper {
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_HOME = "home";
    public static final String KEY_ROW_ID = "_id";
    public static final String PROJECTION[] = {
            KEY_ROW_ID,
            KEY_NAME,
            KEY_ADDRESS,
            KEY_MOBILE,
            KEY_HOME
    };
    /* The table and database names */
    private static final String TABLE_NAME = "mycontacts";
    private static final String DATABASE_NAME = "contactDB";

        /*SQL code for creating the table*/

    private static final String TABLE_CREATE=
            "create table "+ TABLE_NAME + "("+ KEY_ROW_ID
                    +" integer primary key autoincrement,"+
                    KEY_NAME +" text not null,"+
                    KEY_ADDRESS + " text not null,"+
                    KEY_MOBILE + " text,"+
                    KEY_HOME + " text)";


}
