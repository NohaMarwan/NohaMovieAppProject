package com.example.nohaosama.movieappproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.nohaosama.movieappproject.R;
import com.example.nohaosama.movieappproject.fragments.ListItemsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{

    public static boolean mTwoPane;
    @BindView(R.id.toolbar)Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // The detail container view will be present only in the
        // large-screen layouts
        // If this view is present, then the
        // activity should be in two-pane mode.
        if (findViewById(R.id.detail_container) != null)
        {
            mTwoPane = true;

        }


        ListItemsFragment listitemsfragment=new ListItemsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.item_container, listitemsfragment).commit();


    }

}
