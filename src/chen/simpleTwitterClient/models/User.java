package chen.simpleTwitterClient.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8669213463685035423L;

	@Column(name = "name")
	private String name;
	
	@Column(name = "uid")
	private long uid;
	
	@Column(name = "screenName")
	private String screenName;
	
	@Column(name = "imageProfileUrl")
	private String imageProfileUrl;
	
	@Column(name = "followers")
	private String followers;
	
	@Column(name = "following")
	private String following;
	
	public User(){
		super();
	}
	
	public static User fromJSON(JSONObject jsonObject){
		try {
			User user = new User();
			user.name = jsonObject.getString("name");
			Log.d("DEBUG", "User name is " + user.name);
			user.uid = jsonObject.getLong("id");
			Log.d("DEBUG", "User uid is " + user.uid);
			user.screenName = jsonObject.getString("screen_name");
			Log.d("DEBUG", "User screen name is " + user.screenName);
			user.imageProfileUrl = jsonObject.getString("profile_image_url");
			Log.d("DEBUG", "User imageProfileUrl is " + user.imageProfileUrl);
			user.followers = String.valueOf(jsonObject.getInt("followers_count"));
			Log.d("DEBUG", "User #followers is " + user.followers);
			user.following = jsonObject.getString("friends_count");
			Log.d("DEBUG", "User #following is " + user.following);
			user.save();
			return user;
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("DEBUG", "Fail to convert one json object to User : " + jsonObject.toString());
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getImageProfileUrl() {
		return imageProfileUrl;
	}

	public String getFollowers() {
		return followers;
	}

	public String getFollowing() {
		return following;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", uid=" + uid + ", screenName="
				+ screenName + ", imageProfileUrl=" + imageProfileUrl
				+ ", followers=" + followers + ", following=" + following + "]";
	}
	
}
