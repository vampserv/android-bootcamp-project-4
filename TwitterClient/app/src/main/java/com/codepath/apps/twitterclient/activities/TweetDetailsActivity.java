package com.codepath.apps.twitterclient.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.MediaAdapter;
import com.codepath.apps.twitterclient.helpers.LinkifiedTextView;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TweetDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        Tweet tweet = getIntent().getParcelableExtra("Tweet");

        TextView tvUser = (TextView) findViewById(R.id.tvUser);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ImageButton ibBackButton = (ImageButton) findViewById(R.id.ibBackButton);

        tvUser.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvCreatedAt.setText(tweet.getCreatedAt());
        ivProfileImage.setImageResource(0);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).placeholder(R.drawable.placeholder).into(ivProfileImage);

        GridView gvMedia = (GridView) findViewById(R.id.gvMedia);
        MediaAdapter aMedia = new MediaAdapter(this, tweet.getMedia());
        gvMedia.setAdapter(aMedia);

        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TweetDetailsActivity.this.finish();
            }
        });

    }

    public void onBackButtonClick() {
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
