package com.codepath.apps.twitterclient.helpers;

import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.interfaces.AsyncListResponse;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

import java.util.ArrayList;

/**
 * Created by edwardyang on 5/21/15.
 */

public class AsyncSaveTweetsToDB extends AsyncTask<Void, Void, Void> {

    ArrayList<Tweet> tweets;
    String tweetType;

    public AsyncSaveTweetsToDB(ArrayList<Tweet> tweets, String tweetType) {
        this.tweets = tweets;
        this.tweetType = tweetType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        // use a transaction for bulk inserts for better performance
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < tweets.size(); i++) {
                Tweet tweet = tweets.get(i);
                // save user first because it is the parent
                User user = tweet.user;
                tweet.tweetType = tweetType;
                User existingUser = new Select().from(User.class).where("uid = ?", user.uid).executeSingle();

                if (existingUser != null) {
                    tweet.user = existingUser;
                } else {
                    user.save();
                    tweet.user = user;
                }

                tweet.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return null;
    }

}
