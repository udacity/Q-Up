package com.example.android.q_up;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private RecyclerView allGuestsListView;
    private EditText newGuestNameView;
    private EditText newPartyCountView;

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

        //clear UI text fields
        newPartyCountView.clearFocus();
        newGuestNameView.getText().clear();
        newPartyCountView.getText().clear();

    }

}
