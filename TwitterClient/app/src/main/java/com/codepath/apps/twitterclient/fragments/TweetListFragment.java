package com.codepath.apps.twitterclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.EndlessScrollListener;
import com.codepath.apps.twitterclient.adapters.TweetResultsAdapter;
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
public class TweetListFragment extends Fragment {

    private String TWEET_TYPE = "home";

    private ArrayList<Tweet> tweets;
    private TweetResultsAdapter aTweets;

    TweetUtilities utils;
    public TwitterClient client;

    ProgressBar pbFooterLoading;
    private ListView lvTweets;
    private OnItemSelectedListener listener;

    public SwipeRefreshLayout swipeContainer;

    public long lowestId = 0;
    public long highestId = 0;

    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        public void onStartReplyClicked(Tweet tweet);
        public void onViewProfileClicked(User user);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    // creation lifecycle event
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
    }

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        // add a progress bar for listview footer
        View footer = inflater.inflate(R.layout.activity_timeline_progress_footer, null);
        pbFooterLoading = (ProgressBar) footer.findViewById(R.id.pbFooterLoading);
        lvTweets.addFooterView(footer);

        // set infinite scroll
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onListViewScroll();
            }
        });

        aTweets = new TweetResultsAdapter(getActivity(), tweets, new TweetResultsAdapter.TweetResultsAdapterListener() {
            @Override
            public void onViewProfile(User user) {
                listener.onViewProfileClicked(user);
            }

            @Override
            public void onReplyTweet(Tweet tweet) {
                listener.onStartReplyClicked(tweet);
            }
        });

        lvTweets.setAdapter(aTweets);

        // setup swipe-to-refresh
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // only get new results (not db cached results)
                populateTimeline(0, highestId);
            }
        });

        return v;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    // append a tweet to top of list
    public void prependTweet(Tweet tweet) {

        // save new tweet async
        ArrayList<Tweet> newTweets = new ArrayList<Tweet>();
        newTweets.add(tweet);
        persistTweets(TWEET_TYPE, newTweets);

        aTweets.insert(tweet, 0);
        highestId = tweet.uid;

    }

    public void onListViewScroll() { }

    public void populateTimelineFromDbOrRest(String tweetType, long lowest) {

        AsyncRetrieveTweetsFromDB asyncTask = new AsyncRetrieveTweetsFromDB(tweetType, new AsyncListResponse() {

            @Override
            public void processFinish(List<Tweet> queryResults) {

                int resultSize = queryResults.size();

                if (resultSize > 0) {
                    Tweet lastTweet = queryResults.get(resultSize - 1);
                    Tweet firstTweet = queryResults.get(0);
                    if (queryResults.size() > 1 || firstTweet.uid != highestId) {
                        if (firstTweet.uid > highestId) {
                            highestId = firstTweet.uid;
                        }
                        if (lastTweet.uid < lowestId || lowestId == 0) {
                            lowestId = lastTweet.uid - 1;
                        }
                        // add cached tweets to adapter
                        addAll(queryResults);
                    }
                }

                // if cached results are less than the results per page, call rest api
                if (resultSize < TwitterClient.RESULTS_PER_PAGE) {
                    populateTimeline(lowestId, 0);
                }
            }

        });

        // start async task
        asyncTask.execute(lowestId);

    }

    public void populateTimeline(long lowest, long highest) {}

    public void showLoading() {
        pbFooterLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        swipeContainer.setRefreshing(false);
        pbFooterLoading.setVisibility(View.GONE);
    }

    public void persistTweets(String tweetType, ArrayList<Tweet> newTweets) {
        new AsyncSaveTweetsToDB(newTweets, tweetType).execute();
    }

}
