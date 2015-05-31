package com.codepath.apps.twitterclient.fragments;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.adapters.EndlessScrollListener;
import com.codepath.apps.twitterclient.adapters.TweetResultsAdapter;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;
import com.codepath.apps.twitterclient.helpers.TwitterApplication;
import com.codepath.apps.twitterclient.helpers.TwitterClient;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by edwardyang on 5/30/15.
 */
public class UserHeaderFragment extends Fragment {

    public RelativeLayout rlHeading;
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    public static UserHeaderFragment newInstance(User user) {
        UserHeaderFragment f = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putParcelable("User", user);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_userdetail_header, container, false);

        User user = getArguments().getParcelable("User");

        TextView tvUser = (TextView) v.findViewById(R.id.tvUser);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);

        TextView tvFollowingCount = (TextView) v.findViewById(R.id.tvFollowingCount);
        TextView tvFollowersCount = (TextView) v.findViewById(R.id.tvFollowersCount);
        TextView tvTweetCount = (TextView) v.findViewById(R.id.tvTweetCount);

        rlHeading = (RelativeLayout) v.findViewById(R.id.rlHeading);

        tvUser.setText(user.name);
        tvScreenName.setText(user.screenName);
        tvFollowingCount.setText(String.valueOf(user.followingCount));
        tvFollowersCount.setText(String.valueOf(user.followersCount));
        tvTweetCount.setText(String.valueOf(user.tweetCount));

        ivProfileImage.setImageResource(0);
        Picasso.with(getActivity()).load(user.profileImageUrl).placeholder(R.drawable.placeholder).into(ivProfileImage);
//
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(user.profileBgkImageUrl).getContent());
//            rlHeading.setBackground(new BitmapDrawable(getResources(),bitmap));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        Picasso.with(getActivity()).load(user.profileBgkImageUrl).placeholder(R.drawable.placeholder).into(new Target(){

            @Override
            @TargetApi(16)
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlHeading.setBackgroundDrawable(new BitmapDrawable(bitmap));
                } else {
                    rlHeading.setBackground(new BitmapDrawable(getResources(), bitmap));
                }
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.i("DEBUG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.i("DEBUG", "PREPARE LOAD");
            }
        });


        return v;
    }

}
