package com.example.rachael.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    static final String API_REQUEST_URL = "https://content.guardianapis.com/search?";
    private static final int NEWS_ITEM_LOADER_ID = 1;
    private NewsItemAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsItemListView = findViewById(R.id.list);
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
        newsItemListView.setAdapter(mAdapter);

        newsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsItem currentNewsItem = mAdapter.getItem(position);
                Uri newsItemUri = Uri.parse(currentNewsItem.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);
                startActivity(websiteIntent);
            }
        });

        mEmptyStateTextView = findViewById(R.id.empty_message);
        newsItemListView.setEmptyView(mEmptyStateTextView);
        
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_ITEM_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.connection_error);
        }
    }

    @Override
    public android.content.Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String getSubject = sharedPrefs.getString(
                getString(R.string.settings_subject_key),
                getString(R.string.settings_subject_default));

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(API_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("q", getSubject);
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        // Return the completed uri
        return new NewsItemLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished
            (android.content.Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        mEmptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();
        if (newsItems != null && !newsItems.isEmpty()) {
            mAdapter.addAll(newsItems);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<NewsItem>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


