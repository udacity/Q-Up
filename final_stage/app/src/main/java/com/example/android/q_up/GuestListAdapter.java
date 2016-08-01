package com.example.android.q_up;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.q_up.data.QueueContract;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    // Holds on to the query result to display the guest list
    private Cursor cursor;

    private Context context;

    // Constructor
    public GuestListAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the recyclerview item layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // Move the cursor to the position of the item to be displayed
        if (!cursor.moveToPosition(position))
            return; // bail if returned null
        // Update the view holder with the information needed to display
        long id = cursor.getLong(QueueContract.INDEX_QUEUE_ENTRY_ID);
        String name = cursor.getString(QueueContract.INDEX_QUEUE_ENTRY_NAME);
        int party = cursor.getInt(QueueContract.INDEX_QUEUE_ENTRY_PARTY);
        // Set the tag to be the DB id so we can delete
        holder.itemView.setTag(id);
        // Display the guest name
        holder.nameTextView.setText(name);
        // Display the party count
        holder.partyTextView.setText(context.getString(R.string.party_display, party));
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    /**
     * Swaps the cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous cursor first
        if (cursor != null)
            cursor.close();
        cursor = newCursor;
        if(newCursor != null) {
            // Force the recyclerview to refresh
            this.notifyDataSetChanged();
        }
    }


    /**
     * Inner class to hold the views needed to display a single item in the recyvlerview
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party number
        TextView partyTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link GuestListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partyTextView = (TextView) itemView.findViewById(R.id.party_text_view);
        }

    }
}
