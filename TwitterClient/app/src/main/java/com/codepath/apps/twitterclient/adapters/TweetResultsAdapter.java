package com.codepath.apps.twitterclient.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.helpers.LinkifiedTextView;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by edwardyang on 5/19/15.
 */
public class TweetResultsAdapter extends ArrayAdapter<Tweet> {

    private final TweetResultsAdapterListener listener;

    public TweetResultsAdapter(Context context, List<Tweet> objects, TweetResultsAdapterListener listener) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.listener = listener;
    }

    private static class ViewHolder {
        TextView tvUser;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvCreatedAt;
        ImageView ivProfileImage;
        ImageView ivReplyImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_result, parent, false);
            viewHolder.tvUser = (TextView) convertView.findViewById(R.id.tvUser);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
            viewHolder.tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.ivReplyImage = (ImageView) convertView.findViewById(R.id.ivReplyImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUser.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvScreenName.setText(tweet.user.screenName);
        viewHolder.tvCreatedAt.setText(tweet.createdAt);
        viewHolder.ivProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.user.profileImageUrl).placeholder(R.drawable.placeholder).into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onViewProfile(tweet.user);
                }
            }
        });

        viewHolder.ivReplyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onReplyTweet(tweet);
                }
            }
        });

        return convertView;
    }

    public interface TweetResultsAdapterListener {
        void onViewProfile(User user);
        void onReplyTweet(Tweet tweet);
    }

}
