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

public class AsyncSaveToDB extends AsyncTask<ArrayList, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(ArrayList... params) {

        ArrayList<Tweet> currentTweets = params[0];

        // use a transaction for bulk inserts for better performance
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < currentTweets.size(); i++) {
                Tweet tweet = currentTweets.get(i);
                // save user first because it is the parent
                User user = tweet.user;
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
