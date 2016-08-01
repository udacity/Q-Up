package com.example.android.q_up.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.q_up.data.QueueContract.QueueEntry;

public class QueueDbHelper extends SQLiteOpenHelper {

    // The database name
    public static final String DATABASE_NAME = "queue.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public QueueDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold queue data
        final String SQL_CREATE_QUEUE_TABLE = "CREATE TABLE " + QueueEntry.TABLE_NAME + " (" +
                QueueEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                QueueEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                QueueEntry.COLUMN_PARTY + " INTEGER NOT NULL, " +
                QueueEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_QUEUE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one
        // This should be modified to Alter the table in new versions if old data is to be supported
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + QueueEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
