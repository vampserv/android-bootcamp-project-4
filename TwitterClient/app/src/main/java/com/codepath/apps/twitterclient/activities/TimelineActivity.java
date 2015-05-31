package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.TweetsPagerAdapter;
import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.TweetComposerDialog;
import com.codepath.apps.twitterclient.fragments.TweetListFragment;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements TweetListFragment.OnItemSelectedListener {

    private TweetUtilities utils;
    private TwitterClient client;

    public TweetsPagerAdapter tweetsPagerAdapter;
    Toolbar toolbar;

    private int TWEET_DETAILS_REQUEST_CODE = 100;

    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();

        // enable actionbar icon
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // get current user information
        if (currentUser == null) {
            getCurrentUser();
        }

        // get viewpager]
        ViewPager vpTimeline = (ViewPager) findViewById(R.id.vpTimeline);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        // set the viewpager adapter
        vpTimeline.setAdapter(tweetsPagerAdapter);
        // find the sliding tabstrip
        PagerSlidingTabStrip tabTimeline = (PagerSlidingTabStrip) findViewById(R.id.tabTimeline);
        // attach the tabstrip to viewpager
        tabTimeline.setViewPager(vpTimeline);
    }

    private void getCurrentUser() {

        if(!utils.isNetworkAvailable(this)) {
//            Toast.makeText(this, "No internet connection, please try again later", Toast.LENGTH_SHORT).show();
            return;
        }

        client.getCurrentUserInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                currentUser = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                utils.displayTwitterError(TimelineActivity.this, "retrieving current user info", error);
            }

        });
    }

    // fires when start reply clicked in fragment listview
    @Override
    public void onStartReplyClicked(Tweet tweet) {
        Intent i = new Intent(this, TweetDetailsActivity.class);
        // get tweet from adapter and pass into intent
        i.putExtra("Tweet", tweet);
        // launch new activity for result
        startActivityForResult(i, TWEET_DETAILS_REQUEST_CODE);
    }

    // fires when view profile is clicked in fragment listview
    @Override
    public void onViewProfileClicked(User user) {
        Intent i = new Intent(this, UserDetailsActivity.class);
        i.putExtra("User", user);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.miComposeTweet) {
            launchTweetComposerDialog();
            return true;
        }

        if (id == R.id.miProfile) {
            Intent i = new Intent(this, UserDetailsActivity.class);
            i.putExtra("User", currentUser);
            // launch new activity for result
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    // launch tweet composer in a dialog
    private void launchTweetComposerDialog() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetComposerDialog esd = TweetComposerDialog.newInstance(this, new TweetComposerDialogFragmentListener() {
            public void sendTweet(String tweetBody){
                sendTweetAsync(tweetBody, 0);
            }
        }, currentUser);
        esd.show(fm, "fragment_tweet_composer");
    }

    public interface TweetComposerDialogFragmentListener {
        public void sendTweet(String tweetBody);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == TWEET_DETAILS_REQUEST_CODE) {
            // get tweet reply and send async
            String tweetReply = data.getStringExtra("tweetReply");
            long replyToId = data.getLongExtra("replyToId", 0);
            sendTweetAsync(tweetReply, replyToId);
        }
    }

    private void sendTweetAsync(final String tweetBody, long replyToId) {

        if(!utils.isNetworkAvailable(this)) {
//            Toast.makeText(this, "No internet connection, please try again later", Toast.LENGTH_SHORT).show();
            return;
        }

        client.postStatusUpdate(tweetBody, replyToId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // only get new results (not db cached results)
//                populateTimeline(0, highestId);
                Tweet newTweet = new Tweet(response);
                HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) tweetsPagerAdapter.getRegisteredFragment(0);
                homeTimelineFragment.prependTweet(newTweet);
                Toast.makeText(TimelineActivity.this, "Your status has been updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                utils.displayTwitterError(TimelineActivity.this, "composing tweet", error);
            }

        });
    }

}
