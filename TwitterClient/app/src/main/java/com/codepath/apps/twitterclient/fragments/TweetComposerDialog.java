package com.codepath.apps.twitterclient.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.activities.TimelineActivity;
import com.codepath.apps.twitterclient.models.User;
import com.squareup.picasso.Picasso;

public class TweetComposerDialog extends DialogFragment {

    static Context mContext;
    static TimelineActivity.TweetComposerDialogFragmentListener mListener;

    EditText etTweetBody;
    private TextView tvTweetsLeft;
    private int tweetsTotal = 140;

    public TweetComposerDialog() { }

    public static TweetComposerDialog newInstance(Context context, TimelineActivity.TweetComposerDialogFragmentListener listener, User user) {
        TweetComposerDialog dialog = new TweetComposerDialog();
        mContext = context;
        mListener = listener;
        Bundle args = new Bundle();
        args.putParcelable("User", user);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_composer, container);

        // set dialog title
        getDialog().setTitle("Compose a Tweet");

        // get SearchFilter model
        User currentUser = getArguments().getParcelable("User");

        // initialize views
        TextView tvAccountUsername = (TextView) view.findViewById(R.id.tvAccountUsername);
        TextView tvAccountScreenname = (TextView) view.findViewById(R.id.tvAccountScreenname);
        tvTweetsLeft = (TextView) view.findViewById(R.id.tvTweetsLeft);
        ImageView ivAccountProfileImage = (ImageView) view.findViewById(R.id.ivAccountProfileImage);
        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);

        tvAccountUsername.setText(currentUser.name);
        tvAccountScreenname.setText(currentUser.screenName);
        tvTweetsLeft.setText((tweetsTotal + ""));
        Picasso.with(mContext).load(currentUser.profileImageUrl).into(ivAccountProfileImage);

        Button btSendTweet = (Button) view.findViewById(R.id.btSendTweet);
        Button btCancel = (Button) view.findViewById(R.id.btCancel);

        btSendTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendTweet();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelTweet();
            }
        });

        etTweetBody.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                int tweetsLeft = (tweetsTotal - etTweetBody.length());
                tvTweetsLeft.setText(String.valueOf(tweetsLeft));
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        etTweetBody.requestFocus();

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }

    public void sendTweet() {
        String tweet = etTweetBody.getText().toString();
        if (tweet.length() < 1) {
            Toast.makeText(mContext, "Tweet body is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        mListener.sendTweet(tweet);
        this.dismiss();
    }

    public void cancelTweet() {
        this.dismiss();
    }
}
