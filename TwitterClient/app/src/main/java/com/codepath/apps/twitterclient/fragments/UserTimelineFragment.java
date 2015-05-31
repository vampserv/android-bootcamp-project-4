package com.codepath.apps.twitterclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.twitterclient.helpers.AsyncRetrieveTweetsFromDB;
import com.codepath.apps.twitterclient.helpers.AsyncSaveTweetsToDB;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.interfaces.AsyncListResponse;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardyang on 5/30/15.
 */
public class UserTimelineFragment extends TweetListFragment {

    private String TWEET_TYPE = "user";

    TweetUtilities utils;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment f = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("User", user);
        f.setArguments(args);
        return f;
    }

    @Override
    public void populateTimeline(long lowest, long highest) {

        User user = getArguments().getParcelable("User");

        if(!utils.isNetworkAvailable(getActivity())) {
//            Toast.makeText(getActivity(), "No internet connection, please try again later", Toast.LENGTH_SHORT).show();
            swipeContainer.setRefreshing(false);
            return;
        }

        showLoading();

        client.getUserTimeline(user.screenName, lowest, highest, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                ArrayList<Tweet> currentTweets = Tweet.fromJSONArray(response);

                // some logic to paginate results more efficiently
                if (currentTweets.size() > 0) {
                    Tweet lastTweet = currentTweets.get(currentTweets.size() - 1);
                    Tweet firstTweet = currentTweets.get(0);
                    if (currentTweets.size() > 1 || firstTweet.uid != highestId) {
                        if (firstTweet.uid > highestId) {
                            highestId = firstTweet.uid;
                        }
                        if (lastTweet.uid < lowestId || lowestId == 0) {
                            lowestId = lastTweet.uid - 1;
                        }

                        addAll(currentTweets);

                        // dont cache tweets from other users
//                        persistTweets(TWEET_TYPE, currentTweets);
                    }
                }

                // update views
                hideLoading();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                utils.displayTwitterError(getActivity(), "retrieving timeline", error);
//                aTweets.add(new Tweet().getMockTweet("home"));
                hideLoading();
            }

        });
    }

    // dont cache timeline belonging to other users
    @Override
    public void onListViewScroll() {
        populateTimeline(lowestId, 0);
    }
}
