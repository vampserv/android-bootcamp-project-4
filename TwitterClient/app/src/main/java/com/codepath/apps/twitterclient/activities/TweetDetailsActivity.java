package com.codepath.apps.twitterclient.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.MediaAdapter;
import com.codepath.apps.twitterclient.helpers.LinkifiedTextView;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;
import com.codepath.apps.twitterclient.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TweetDetailsActivity extends ActionBarActivity {

    Tweet tweet;
    TweetUtilities utils;

    EditText etTweetReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tweet = getIntent().getParcelableExtra("Tweet");

        TextView tvUser = (TextView) findViewById(R.id.tvUser);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ImageButton ibBackButton = (ImageButton) findViewById(R.id.ibBackButton);
        etTweetReply = (EditText) findViewById(R.id.etTweetReply);
        Button btSendReply = (Button) findViewById(R.id.btSendReply);
        TextView tvHashtags = (TextView) findViewById(R.id.tvHashtags);

        tvUser.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvScreenName.setText(tweet.user.screenName);
        tvCreatedAt.setText(tweet.createdAt);
        ivProfileImage.setImageResource(0);
        Picasso.with(this).load(tweet.user.profileImageUrl).placeholder(R.drawable.placeholder).into(ivProfileImage);

        GridView gvMedia = (GridView) findViewById(R.id.gvMedia);
        ArrayList<String> media = tweet.media;
        MediaAdapter aMedia =  new MediaAdapter(this, media);

        if (media == null) {
            gvMedia.setAdapter(null);
        } else {
            gvMedia.setAdapter(aMedia);
        }

        ibBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                TweetDetailsActivity.this.finish();
            }
        });

        String hashtags = utils.ArrayListToString(tweet.hashtags, "#", " ");
        if (hashtags == "#"){
            hashtags = "";
        }
        tvHashtags.setText(hashtags);

        btSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("tweetReply", etTweetReply.getText().toString());
                data.putExtra("replyToId", tweet.uid);
                setResult(RESULT_OK, data);
                TweetDetailsActivity.this.finish();
            }
        });

        etTweetReply.requestFocus();

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
