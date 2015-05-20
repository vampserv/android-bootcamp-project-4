package com.codepath.apps.twitterclient.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.codepath.apps.twitterclient.helpers.TweetUtilities;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "items")
public class Tweet extends Model {
	// Define table fields
	@Column(name = "name")
	private String body;
    private long uid;
    private User user;
    private String createdAt;

    private TweetUtilities utils;

	public Tweet() {
		super();
	}

    public long getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray array) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i = 0; i < array.length(); i++) {
            try {
                tweets.add(new Tweet(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

	public Tweet(JSONObject object){
		try {
            this.body = object.getString("text");
            this.uid = object.getLong("id");
            this.createdAt = utils.getRelativeTimeAgo(object.getString("created_at"));
            this.user = User.fromJSON(object.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Record Finders
	public static Tweet byId(long id) {
		return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
	}

	public static List<Tweet> recentItems() {
		return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
	}
}
