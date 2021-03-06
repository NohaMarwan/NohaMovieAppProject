package com.example.nohaosama.movieappproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nohaosama.movieappproject.R;
import com.example.nohaosama.movieappproject.Store.FavoriteStore;
import com.example.nohaosama.movieappproject.adapter.ItemsAdapter;
import com.example.nohaosama.movieappproject.app.AppController;
import com.example.nohaosama.movieappproject.json.Parser;
import com.example.nohaosama.movieappproject.models.modelMovie;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aya on 04/11/2016.
 */

public class ListItemsFragment extends Fragment  {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private Menu menu;
    protected ItemsAdapter itemAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<modelMovie> dataSet;
    protected List<modelMovie> favoriteDataset;

    public ListItemsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        dataSet = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.list_item, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        itemAdapter = new ItemsAdapter(getActivity(),dataSet);
        mRecyclerView.setAdapter(itemAdapter);

        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        //noinspection ResourceAsColor
        mSwipeRefreshLayout.setColorScheme(
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorAccent, R.color.colorPrimaryDark);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (!mSwipeRefreshLayout.isRefreshing())
        {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        initiateRefresh(0);
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        // handel swipe refresh listener
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh(0);

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);
        this.menu =  menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.top_rated:
                initiateRefresh(1);
                return true;

            case R.id.favorite:
                showFavorite();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showFavorite()
    {
        favoriteDataset = new ArrayList<>();

        for (String id : new FavoriteStore(getActivity()).findAllFavoriteMovies()) {
            for (int j = 0; j < dataSet.size(); j++) {
                if (dataSet.get(j).getID().equalsIgnoreCase(id))
                    favoriteDataset.add(dataSet.get(j));
            }

        }


        clearDataSet();
        dataSet.addAll(0, favoriteDataset);
        itemAdapter.notifyDataSetChanged();
    }

    public  void initiateRefresh(int i)
    {
       String Url;
        if(i!=0)
            Url="http://api.themoviedb.org/3/movie/top_rated?api_key=f78be50ecdcaa8f00e595d90e6281fc8";
        else
            Url="http://api.themoviedb.org/3/movie/popular?api_key=f78be50ecdcaa8f00e595d90e6281fc8";
        /**
         * Execute the background task, which uses {@link AsyncTask} to load the data.
         */
        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                clearDataSet();
                Iterator iterator = Parser.parseStringToJson(data).iterator();
                while (iterator.hasNext()){
                    modelMovie movie = (modelMovie)iterator.next();
                    dataSet.add(movie);
                    itemAdapter.notifyItemInserted(dataSet.size() - 1);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        /////////////connection//////////
        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("response", response);
                clearDataSet();
                Iterator iterator = Parser.parseStringToJson(response).iterator();
                while (iterator.hasNext()){
                    modelMovie movie = (modelMovie)iterator.next();
                    dataSet.add(movie);
                    itemAdapter.notifyItemInserted(dataSet.size() - 1);
                }
                onRefreshComplete();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Stop the refreshing indicator
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d("response", error.toString());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void clearDataSet()
    {
        if (dataSet != null){
            dataSet.clear();
            itemAdapter.notifyDataSetChanged();
        }
    }
    private void onRefreshComplete()
    {
        mSwipeRefreshLayout.setRefreshing(false);

    }

}
