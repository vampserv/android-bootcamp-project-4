package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.fragments.TweetListFragment;
import com.codepath.apps.twitterclient.fragments.UserHeaderFragment;
import com.codepath.apps.twitterclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;

public class UserDetailsActivity extends ActionBarActivity implements TweetListFragment.OnItemSelectedListener {

    private int TWEET_DETAILS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // get param when activity launches
        User user = getIntent().getParcelableExtra("User");
        // if there is no saved instance yet,
        if (savedInstanceState == null) {
            // create fragment and pass param
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(user);
            UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(user);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // display user fragment within activity dynamically
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.replace(R.id.flHeader, fragmentUserHeader);
            ft.commit();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == TWEET_DETAILS_REQUEST_CODE) {
            // get tweet reply and send async
            String tweetReply = data.getStringExtra("tweetReply");
            long replyToId = data.getLongExtra("replyToId", 0);
//            sendTweetAsync(tweetReply, replyToId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
