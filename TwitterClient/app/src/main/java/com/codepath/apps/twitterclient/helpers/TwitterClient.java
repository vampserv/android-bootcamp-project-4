package com.codepath.apps.twitterclient.helpers;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "7IkuaavCPxy1vcB0rUI2JUSjD";       // Change this
	public static final String REST_CONSUMER_SECRET = "VzDW2d3bUsHP2EB8qIKF0UEonZ7G5C7NK1k19DU0seUqNoii6d"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptwitterclient"; // Change this (here and in manifest)

    public static final int RESULTS_PER_PAGE = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(long lowestId, long highestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", RESULTS_PER_PAGE);
        if(lowestId > 0) {
            params.put("max_id", lowestId);
        }
        if(highestId > 0) {
            params.put("since_id", highestId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long lowestId, long highestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", RESULTS_PER_PAGE);
        if(lowestId > 0) {
            params.put("max_id", lowestId);
        }
        if(highestId > 0) {
            params.put("since_id", highestId);
        }
        client.get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, long lowestId, long highestId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", RESULTS_PER_PAGE);
        if(lowestId > 0) {
            params.put("max_id", lowestId);
        }
        if(highestId > 0) {
            params.put("since_id", highestId);
        }
        params.put("screen_name", screenName);
        client.get(apiUrl, params, handler);
    }

    public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = new RequestParams();
        client.get(apiUrl, params, handler);
    }

    public void postStatusUpdate(String tweetBody, long replyToId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        if (replyToId != 0) {
            params.put("in_reply_to_status_id", replyToId);
        }
        client.post(apiUrl, params, handler);
    }

}