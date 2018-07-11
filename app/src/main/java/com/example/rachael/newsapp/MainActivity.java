package com.example.rachael.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String API_KEY = <YOUR_API_KEY_HERE>;
    private static final String API_REQUEST_URL = "https://content.guardianapis.com/search?q=leeds&show-tags=contributor&api-key=" + API_KEY;
    private static final int NEWS_ITEM_LOADER_ID = 1;
    private NewsItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsItemListView = findViewById(R.id.list);
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
        newsItemListView.setAdapter(mAdapter);

        //TODO: set onclick listener to push intent to open url in browser
        newsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsItem currentNewsItem = mAdapter.getItem(position);
                Uri newsItemUri = Uri.parse(currentNewsItem.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);
                startActivity(websiteIntent);
            }
        });


        //TODO: empty state message
        //TODO: progress bar/spinner
        //TODO: connectivity message

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_ITEM_LOADER_ID, null, this);
    }

    @Override
    public android.content.Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        return new NewsItemLoader(this, API_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        mAdapter.clear();
        if (newsItems != null && !newsItems.isEmpty()) {
            mAdapter.addAll(newsItems);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<NewsItem>> loader) {
        mAdapter.clear();
    }

}
