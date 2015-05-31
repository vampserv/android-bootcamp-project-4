package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
@Table(name = "users")
public class User extends Model implements Parcelable {

    // Define table fields
	@Column(name = "name")
	public String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long uid;
    @Column(name = "screenName")
    public String screenName;
    @Column(name = "tagline")
    public String tagline;
    @Column(name = "profileImageUrl")
    public String profileImageUrl;
    @Column(name = "profileBgkImageUrl")
    public String profileBgkImageUrl;
    @Column(name = "tweetCount")
    public long tweetCount;
    @Column(name = "followingCount")
    public long followingCount;
    @Column(name = "followersCount")
    public long followersCount;


	public User() {
		super();
	}

    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = "@" + json.getString("screen_name");
            user.tagline = json.getString("description");
            user.profileImageUrl = json.getString("profile_image_url");
            user.profileBgkImageUrl = json.getString("profile_background_image_url");
            user.tweetCount = json.getLong("statuses_count");
            user.followingCount = json.getLong("friends_count");
            user.followersCount = json.getLong("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getMockUser() {
        User user = new User();
        user.name = "Mock User";
        user.uid = 23232323;
        user.screenName = "my_mock_name";
        user.tagline = "hello world";
        user.profileImageUrl = "http://abs.twimg.com/sticky/default_profile_images/default_profile_6_normal.png";
        user.profileBgkImageUrl = "http://a0.twimg.com/profile_background_images/495742332/purty_wood.png";
        user.tweetCount = 345;
        user.followingCount = 6161;
        user.followersCount = 3;
        return user;
    }

	// Parse model from JSON
	public User(JSONObject json){
        try {
            this.name = json.getString("name");
            this.uid = json.getLong("id");
            this.screenName = "@" + json.getString("screen_name");
            this.tagline = json.getString("description");
            this.profileImageUrl = json.getString("profile_image_url");
            this.profileBgkImageUrl = json.getString("profile_background_image_url");
            this.tweetCount = json.getLong("statuses_count");
            this.followingCount = json.getLong("friends_count");
            this.followersCount = json.getLong("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	// Record Finders
	public static User byId(long id) {
		return new Select().from(User.class).where("id = ?", id).executeSingle();
	}

	public static List<User> recentItems() {
		return new Select().from(User.class).orderBy("id DESC").limit("300").execute();
	}


    private User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.tagline = in.readString();
        this.profileImageUrl = in.readString();
        this.profileBgkImageUrl = in.readString();
        this.tweetCount = in.readLong();
        this.followingCount = in.readLong();
        this.followersCount = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(uid);
        dest.writeString(screenName);
        dest.writeString(tagline);
        dest.writeString(profileImageUrl);
        dest.writeString(profileBgkImageUrl);
        dest.writeLong(tweetCount);
        dest.writeLong(followingCount);
        dest.writeLong(followersCount);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    
}
