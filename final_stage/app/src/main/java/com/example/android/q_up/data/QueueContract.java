package com.example.android.q_up.data;

import android.provider.BaseColumns;

public class QueueContract {

    // A projection String[] used to get all column names for a "select all" query
    public static final String[] ALL_GUESTS_LIST_PROJECTION = new String[]{
            QueueEntry._ID,
            QueueEntry.COLUMN_NAME,
            QueueEntry.COLUMN_PARTY_SIZE,
            QueueEntry.COLUMN_TIMESTAMP
    };

    // These indices must match the projection.
    // This is used to access the result of the select all query
    public static final int INDEX_QUEUE_ENTRY_ID = 0;
    public static final int INDEX_QUEUE_ENTRY_NAME = 1;
    public static final int INDEX_QUEUE_ENTRY_PARTY_SIZE = 2;
    public static final int INDEX_QUEUE_ENTRY_TIMESTAMP = 3;


    // Inner class that defines the table contents of the queue table
    // TODO One thing to think about now, queue is a kind of overloaded term in computer science
    // TODO maybe we should call it a line or waitlist instead?
    public static final class QueueEntry implements BaseColumns {
        public static final String TABLE_NAME = "queue";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timeAdded";
    }
}
