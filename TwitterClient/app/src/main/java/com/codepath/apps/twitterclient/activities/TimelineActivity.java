package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.EndlessScrollListener;
import com.codepath.apps.twitterclient.adapters.TweetResultsAdapter;
import com.codepath.apps.twitterclient.fragments.TweetComposerDialog;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetResultsAdapter aTweets;

    ProgressBar pbFooterLoading;
    private ListView lvTweets;

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        setupViews();

        tweets = new ArrayList<>();
        aTweets = new TweetResultsAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(1);
            }
        });

        client = TwitterApplication.getRestClient();
        populateTimeline(1);
    }

    private void setupViews() {
        lvTweets = (ListView) findViewById(R.id.lvTimeline);
//        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent i = new Intent(GoogleImageSearchActivity.this, ImageDisplayActivity.class);
////                // get image result
////                ImageResult result = imageResults.get(position);
////                // pass image result into intent
////                i.putExtra("ImageResult", (ImageResult) aImageResults.getItem(position));
////                // launch new activity
////                startActivity(i);
//            }
//        });

        // add a progress bar for listview footer
        View footer = getLayoutInflater().inflate(R.layout.activity_timeline_progress_footer, null);
        pbFooterLoading = (ProgressBar) footer.findViewById(R.id.pbFooterLoading);
        lvTweets.addFooterView(footer);

        // set infinite scroll
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                populateTimeline(page);
            }
        });

    }

    private void populateTimeline(int page) {

        pbFooterLoading.setVisibility(View.VISIBLE);

        client.getHomeTimeline(page, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                aTweets.addAll(Tweet.fromJSONArray(response));
                swipeContainer.setRefreshing(false);
                pbFooterLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                Toast.makeText(TimelineActivity.this, "Error retrieving results, please try again later", Toast.LENGTH_SHORT).show();
                pbFooterLoading.setVisibility(View.GONE);
            }

        });
    }

    // launch tweet composer in a dialog
    private void launchTweetComposerDialog() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        TweetComposerDialog esd = TweetComposerDialog.newInstance(this, new TweetComposerDialogFragmentListener(){
            public void updateSettings(){
                aTweets.clear();
                populateTimeline(1);
            }
        });
        esd.show(fm, "fragment_edit_settings");
    }

    public interface TweetComposerDialogFragmentListener {
        public void updateSettings();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
