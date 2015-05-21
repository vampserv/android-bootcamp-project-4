package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;
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
public class Tweet extends Model implements Parcelable {
	// Define table fields
	@Column(name = "name")
	private String body;
    private long uid;
    private User user;
    private String createdAt;
    private ArrayList<String> hashtags;
    private ArrayList<String> userMentions;
    private ArrayList<String> urls;
    private ArrayList<String> media;

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

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public ArrayList<String> getUserMentions() {
        return userMentions;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public ArrayList<String> getMedia() {
        return media;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
            JSONObject entities = json.getJSONObject("entities");
            tweet.hashtags = getListByProperty(entities.getJSONArray("hashtags"), "text");
            tweet.userMentions = getListByProperty(entities.getJSONArray("user_mentions"), "screen_name");
            tweet.urls = getListByProperty(entities.getJSONArray("urls"), "url");
            tweet.media = getListByProperty(entities.getJSONArray("media"), "media_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public Tweet getMockTweet() {
        Tweet tweet = new Tweet();
        tweet.body = "This is a mock body http://www.google.com";
        tweet.uid = 38488738;
        tweet.createdAt = utils.getRelativeTimeAgo("Thu May 21 05:38:31 +0000 2015");
        User user = new User().getMockUser();
        tweet.user = user;
        ArrayList<String> hashtags = new ArrayList<String>();
        hashtags.add("foo");
        hashtags.add("bar");
        hashtags.add("baz");
        tweet.hashtags = hashtags;
        ArrayList<String> userMentions = new ArrayList<String>();
        hashtags.add("@user1");
        hashtags.add("@user2");
        hashtags.add("@user3");
        tweet.userMentions = userMentions;
        ArrayList<String> urls = new ArrayList<String>();
        hashtags.add("http://bit.ly/oauth-dancer");
        hashtags.add("http://www.github.com");
        hashtags.add("http://www.google.com");
        tweet.urls = urls;
        ArrayList<String> media = new ArrayList<String>();
        hashtags.add("http://pbs.twimg.com/media/CFcN1TcW8AApMFI.png");
        hashtags.add("http://profiler.nethosting.com/assets/images/screenshots/www.github.com.png");
        hashtags.add("https://camo.githubusercontent.com/59e62574be2bca1ff331ae85ee1c0b392310c599/687474703a2f2f692e696d6775722e636f6d2f39414150344d782e706e67");
        tweet.media = media;
        return tweet;
    }

    private static ArrayList<String> getListByProperty(JSONArray array, String propertyName) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = array.getJSONObject(i);
                String val = json.getString(propertyName);
                arrayList.add(val);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
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

	public Tweet(JSONObject json){
		try {
            this.body = json.getString("text");
            this.uid = json.getLong("id");
            this.createdAt = utils.getRelativeTimeAgo(json.getString("created_at"));
            this.user = User.fromJSON(json.getJSONObject("user"));
            JSONObject entities = json.getJSONObject("entities");
            this.hashtags = getListByProperty(entities.getJSONArray("hashtags"), "text");
            this.userMentions = getListByProperty(entities.getJSONArray("user_mentions"), "screen_name");
            this.urls = getListByProperty(entities.getJSONArray("urls"), "url");
            this.media = getListByProperty(entities.getJSONArray("media"), "media_url");
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

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(getClass().getClassLoader());
        this.hashtags = in.readArrayList(String.class.getClassLoader());
        this.userMentions = in.readArrayList(String.class.getClassLoader());
        this.urls = in.readArrayList(String.class.getClassLoader());
        this.media = in.readArrayList(String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeLong(uid);
        dest.writeString(createdAt);
        dest.writeParcelable(user, flags);
        dest.writeList(hashtags);
        dest.writeList(userMentions);
        dest.writeList(urls);
        dest.writeList(media);
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

}
