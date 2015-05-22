package com.codepath.apps.twitterclient.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.activities.TimelineActivity;
import com.codepath.apps.twitterclient.interfaces.AsyncListResponse;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardyang on 5/21/15.
 */
public class AsyncRetrieveFromDB extends AsyncTask<Long, Void, List> {

    public AsyncListResponse delegate = null;

    public AsyncRetrieveFromDB(AsyncListResponse asyncResponse) {
        delegate = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Tweet> doInBackground(Long... params) {

        long lowestId = params[0];
        List<Tweet> results;

        // query for cached results
        if (lowestId > 0) {
            results = new Select().from(Tweet.class).where("uid < ?", lowestId).orderBy("uid DESC").limit(TwitterClient.RESULTS_PER_PAGE).execute();
        } else {
            results = new Select().from(Tweet.class).orderBy("uid DESC").limit(TwitterClient.RESULTS_PER_PAGE).execute();
        }

        return results;
    }

    @Override
    protected void onPostExecute(List list) {
        delegate.processFinish(list);
    }
}