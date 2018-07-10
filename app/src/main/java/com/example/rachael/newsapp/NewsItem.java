package com.example.rachael.newsapp;

/**
 * {@link NewsItem} is an object that contains all the relevant information for a news story
 */

public class NewsItem {

    private String mStoryTitle;
    private String mSection;
    private String mAuthorName;
    private String mDate;
    private String mUrl;

    /**
     * Constructor to make a new {@link NewsItem} object
     * @param storyTitle is the title of the article
     * @param section is the section that the story is in on the guardian website
     * @param authorName is the name of the article's author
     * @param date is the date the article was published.
     * @param url is the url link to the article on the guardian's website
     */

    public NewsItem(String storyTitle, String section, String authorName, String date, String url) {
        mStoryTitle = storyTitle;
        mSection = section;
        mAuthorName = authorName;
        mDate = date;
        mUrl = url;
    }

    public String getStoryTitle() {
        return mStoryTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

}
