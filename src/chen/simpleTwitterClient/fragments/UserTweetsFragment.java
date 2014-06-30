package chen.simpleTwitterClient.fragments;

import android.util.Log;
import chen.simpleTwitterClient.activities.TwitterClientApp;
import chen.simpleTwitterClient.models.User;

import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTweetsFragment extends BaseTweetsFragment {

	private User user;
	
	@Override
	public void asynLoadingData(long since, long max, long count,
			JsonHttpResponseHandler handler) {
		if(this.user == null){
			Log.d("ERROR", "Set user!");
			return;
		}
		TwitterClientApp.getRestClient().getUserTweets(user.getScreenName(), since, max - 1, count, handler);
	}
	
	public void setUser(final User user){
		this.user = user;
	}

}
