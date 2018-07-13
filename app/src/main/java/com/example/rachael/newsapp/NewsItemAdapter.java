package com.example.rachael.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * {@link NewsItemAdapter} creates a list item layout for each news story in the list of
 * {@link NewsItem} objects
 */

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    /**
     * Constructs a new {@link NewsItem}
     *
     * @param context   is the context of the app
     * @param newsItems is the list of news stories, which provide data for the adapter
     */

    public NewsItemAdapter(Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View newsItemView = convertView;
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        NewsItem currentNewsItem;
        currentNewsItem = getItem(position);

        TextView sectionTextView = newsItemView.findViewById(R.id.section);
        sectionTextView.setText(currentNewsItem.getSection());

        TextView storyTitleTextView = newsItemView.findViewById(R.id.headline);
        storyTitleTextView.setText(currentNewsItem.getStoryTitle());

        TextView authorNameTextView = newsItemView.findViewById(R.id.author);
        authorNameTextView.setText(currentNewsItem.getAuthorName());

        TextView dateTextView = newsItemView.findViewById(R.id.date);
        dateTextView.setText(currentNewsItem.getDate());



        return newsItemView;
    }
}

