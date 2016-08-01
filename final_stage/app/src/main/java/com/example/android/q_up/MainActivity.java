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

    private RecyclerView allGuestsListView;
    private EditText newGuestNameView;
    private EditText newPartyCountView;
    private GuestListAdapter cursorAdapter;
    private QueueDbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set local attributes to corresponding views
        allGuestsListView = (RecyclerView) this.findViewById(R.id.all_guests_list);
        newGuestNameView = (EditText) this.findViewById(R.id.person_name_text);
        newPartyCountView = (EditText) this.findViewById(R.id.party_count_text);

        // Set layout for the recyclerview, because it's a list we are using the linear layout
        allGuestsListView.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        dbHelper = new QueueDbHelper(this);

        // Keep a reference to the db until paused or killed
        db = dbHelper.getWritableDatabase();

        // Get all guest info from the database and save in a cursor
        Cursor cursor = getAllNames();

        // Create an adapter for that cursor to display the data
        cursorAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the recyclerview
        allGuestsListView.setAdapter(cursorAdapter);

        // Add a touch helper to the recyclerview to handle swiping names off the db
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                long myId = (long) viewHolder.itemView.getTag();
                removePerson(myId);
                cursorAdapter.swapCursor(getAllNames());
            }
        }).attachToRecyclerView(allGuestsListView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Make sure we close the cursor and db when the app is not being used
        cursorAdapter.swapCursor(null);
        if (db != null) {
            db.close();
            db = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get a new db if previous one was closed
        if (db == null)
            db = dbHelper.getWritableDatabase();
        cursorAdapter.swapCursor(getAllNames());
    }

    /**
     * This method is called when user clicks on the Add to queue button
     *
     * @param view The calling view (button)
     */
    public void addToQ(View view) {
        //check for empty values first
        if (newGuestNameView.getText().length() == 0 || newPartyCountView.getText().length() == 0)
            return;

        //default party count to 1
        int party = 1;
        try {
            //newPartyCountView inputType="number", so this should always work
            party = Integer.parseInt(newPartyCountView.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e("addToQ format error", "Failed to parse party text to number" + ex.getMessage());
        }

        // Add guest info to db
        addNewPerson(newGuestNameView.getText().toString(), party);

        // Update the cursor in the adapter to trigger UI to display the new list
        cursorAdapter.swapCursor(getAllNames());

        //clear UI text fields
        newPartyCountView.clearFocus();
        newGuestNameView.getText().clear();
        newPartyCountView.getText().clear();

    }


    /**
     * Query the db and get all geusts from the queue table
     *
     * @return Cursor containing the list of guests
     */
    public Cursor getAllNames() {
        return db.query(
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
     * Adds a new guest to the db including the party count and the current timestamp
     *
     * @param name  Guest's name
     * @param party Number in party
     * @return id of new record added
     */
    public long addNewPerson(String name, int party) {
        ContentValues cv = new ContentValues();
        cv.put(QueueContract.QueueEntry.COLUMN_NAME, name);
        cv.put(QueueContract.QueueEntry.COLUMN_PARTY, party);
        return db.insert(QueueContract.QueueEntry.TABLE_NAME, null, cv);
    }

    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    public boolean removePerson(long id) {
        return db.delete(QueueContract.QueueEntry.TABLE_NAME, QueueContract.QueueEntry._ID + "=" + id, null) > 0;
    }

}
