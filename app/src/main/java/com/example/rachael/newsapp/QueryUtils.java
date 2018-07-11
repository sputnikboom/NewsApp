package com.example.rachael.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Helper methods for requesting and retrieving information from the Guardian API
 */
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    /**
     * Queries the Guardian API and return a list of {@link NewsItem} objects
     */
    public static List<NewsItem> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem encountered making the HTTP request.", e);
        }

        List<NewsItem> newsItems = extractFeaturesFromJson(jsonResponse);

        return newsItems;
    }

    /**
     * returns a new URL object from the given string url
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem encountered when building the URL", e);
        }
        return url;
    }

    /**
     * Make a HTTPS request to the given URL and return the response as a String
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem encountered when retrieving JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * converts the input stream into a String which contains the whole JSON response
     */

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * returns a list of {@link NewsItem} objects that has been created from parsing the JSON response
     */
    public static List<NewsItem> extractFeaturesFromJson(String newsItemJSON) {
        if (TextUtils.isEmpty(newsItemJSON)) {
            return null;
        }

        ArrayList<NewsItem> newsItems = new ArrayList<>();

        try {
            JSONObject rootJson = new JSONObject(newsItemJSON);
            JSONObject responseJson = rootJson.getJSONObject("response");
            JSONArray newsItemArray = responseJson.getJSONArray("results");

            for (int i = 0; i < newsItemArray.length(); i++) {

                JSONObject currentNewsItem = newsItemArray.getJSONObject(i);

                String section = currentNewsItem.getString("sectionName");
                String storyTitle = currentNewsItem.getString("webTitle");
                String url = currentNewsItem.getString("webUrl");
                String date = currentNewsItem.getString("webPublicationDate");
                String authorName;

                JSONArray tags = currentNewsItem.getJSONArray("tags");

                if (tags.length() > 0) {
                    JSONObject authorTags = tags.getJSONObject(0);
                    authorName = authorTags.getString("webTitle");
                    Log.e(LOG_TAG, "authors name is" + authorName);
                } else {
                    authorName = "No author details available";
                }

                // TODO return date in a better format

                NewsItem newsItem = new NewsItem(section, storyTitle, authorName, date, url);
                newsItems.add(newsItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem encountered when parsing the API results", e);
        }

        return newsItems;
    }


}
