package com.android.test.freecourse.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.test.freecourse.model.User;

/**
 * DatabaseHelper
 * <p></p>
 * @author ToanNDD
 * @version 1.0.0
 * created 2016/29/05
 * company bonsey
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user.db";
    private final String TABLE_NAME = "user";
    private final String COLUMN_ID = "id";
    private final String COLUMN_USERNAME = "username";
    private final String COLUMN_PASSWORD = "password";
    SQLiteDatabase db;
    private final String TABLE_CREATE = "create table if not exists user (id integer primary key not null , " +
            " username text not null , password text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertUser(User user) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);
        int count = cursor.getCount();

        values.put(COLUMN_ID , count);
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String searchPassword(String username)
    {
        db = this.getReadableDatabase();
        String query = "select username, password from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query , null);
        String user, pass;
        pass = "not found";
        if(cursor.moveToFirst())
        {
            do{
                user = cursor.getString(0);

                if(user.equals(username))
                {
                    pass = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }

        return pass;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}
