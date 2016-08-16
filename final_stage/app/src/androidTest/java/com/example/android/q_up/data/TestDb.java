package com.example.android.q_up.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    void deleteTheDatabase() {
        mContext.deleteDatabase(QueueContract.QueueEntry.TABLE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        //start with a clean slate
        mContext.deleteDatabase(QueueDbHelper.DATABASE_NAME);

        //try to create the db
        SQLiteDatabase db = new QueueDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        //get all tables names that exist
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // have we created the tables we want?
        boolean tableFound = false;
        do {
            if (QueueContract.QueueEntry.TABLE_NAME.equals(c.getString(0)))
                tableFound = true;
        } while (c.moveToNext());
        // if this fails, it means that your database doesn't contain the table
        assertTrue("Error: Your database was created without the queue table",
                tableFound);

        // now, does our table contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + QueueContract.QueueEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(QueueContract.QueueEntry._ID);
        columnHashSet.add(QueueContract.QueueEntry.COLUMN_NAME);
        columnHashSet.add(QueueContract.QueueEntry.COLUMN_PARTY_SIZE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required columns
        assertTrue("Error: The database doesn't contain all of the required columns",
                columnHashSet.isEmpty());
        c.close();
        db.close();
    }

}
