package com.example.nohaosama.movieappproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.nohaosama.movieappproject.R;
import com.example.nohaosama.movieappproject.fragments.DetailsFragment;

/**
 * Created by aya on 09/11/2016.
 */

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putSerializable(DetailsFragment.ARG_ITEM_ID,
                    getIntent().getSerializableExtra(DetailsFragment.ARG_ITEM_ID));
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment)
                    .commit();
        }
    }
}
