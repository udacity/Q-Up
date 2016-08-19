package com.example.android.q_up;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.android.q_up.data.QueueContract;
import com.example.android.q_up.data.QueueDbHelper;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mAllGuestsListView;
    private EditText mNewGuestNameEditText;
    private EditText mNewPartyCountEditText;
    private GuestListAdapter mCursorAdapter;
    private QueueDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set local attributes to corresponding views
        mAllGuestsListView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);
        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartyCountEditText = (EditText) this.findViewById(R.id.party_count_edit_text);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        mAllGuestsListView.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        mDbHelper = new QueueDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writeable database
        // because you will be adding restaurant customers
        mDb = mDbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllNames();

        // Create an adapter for that cursor to display the data
        mCursorAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        mAllGuestsListView.setAdapter(mCursorAdapter);

        // Add a touch helper to the RecyclerView to handle swiping names off the mDb
        // TODO Since they're not explicitly being taught this (at least I don't think?) add
        // more comments here for the curious student or links to docs
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long myId = (long) viewHolder.itemView.getTag();
                removePerson(myId);
                mCursorAdapter.swapCursor(getAllNames());
            }
        }).attachToRecyclerView(mAllGuestsListView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we close the cursor and mDb when the app is not being used
        mCursorAdapter.swapCursor(null);
        if (mDb != null) {
            mDb.close();
            mDb = null;
        }
    }

    /**
     * This method is called when user clicks on the Add to queue button
     *
     * @param view The calling view (button)
     */
    public void addToQ(View view) {
        //check for empty values first
        if (mNewGuestNameEditText.getText().length() == 0 ||
            mNewPartyCountEditText.getText().length() == 0) {
            return;
        }
        //default party count to 1
        int partySize = 1;
        try {
            //mNewPartyCountEditText inputType="number", so this should always work
            partySize = Integer.parseInt(mNewPartyCountEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse party size text to number" + ex.getMessage());
        }

        // Add guest info to mDb
        addNewPerson(mNewGuestNameEditText.getText().toString(), partySize);

        // Update the cursor in the adapter to trigger UI to display the new list
        mCursorAdapter.swapCursor(getAllNames());

        //clear UI text fields
        mNewPartyCountEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartyCountEditText.getText().clear();

    }


    /**
     * Query the mDb and get all geusts from the queue table
     *
     * @return Cursor containing the list of guests
     */
    public Cursor getAllNames() {
        return mDb.query(
                QueueContract.QueueEntry.TABLE_NAME,
                QueueContract.ALL_GUESTS_LIST_PROJECTION,
                null,
                null,
                null,
                null,
                QueueContract.QueueEntry.COLUMN_TIMESTAMP
        );
    }

    /**
     * Adds a new guest to the mDb including the party count and the current timestamp
     *
     * @param name  Guest's name
     * @param partySize Number in party
     * @return id of new record added
     */
    public long addNewPerson(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(QueueContract.QueueEntry.COLUMN_NAME, name);
        cv.put(QueueContract.QueueEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(QueueContract.QueueEntry.TABLE_NAME, null, cv);
    }

    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    public boolean removePerson(long id) {
        return mDb.delete(QueueContract.QueueEntry.TABLE_NAME, QueueContract.QueueEntry._ID + "=" + id, null) > 0;
    }

}
