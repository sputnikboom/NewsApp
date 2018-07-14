package com.example.rachael.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {

    private static final String LOG_TAG = NewsItemLoader.class.getName();
    private String mUrl;

    /**
     * Constructs a new {@link NewsItemLoader}
     * @param context is the context of the activity
     *                @param url is the url to access data from
     */

    private NewsItemLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsItem> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return QueryUtils.fetchNewsData(mUrl);
    }
}

