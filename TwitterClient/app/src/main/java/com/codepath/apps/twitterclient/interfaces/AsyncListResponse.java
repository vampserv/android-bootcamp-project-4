package com.codepath.apps.twitterclient.interfaces;

import com.codepath.apps.twitterclient.models.Tweet;

import java.util.List;

/**
 * Created by edwardyang on 5/21/15.
 */

public interface AsyncListResponse {
    void processFinish(List<Tweet> queryResults);
}
