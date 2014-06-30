package chen.simpleTwitterClient.fragments;

import chen.simpleTwitterClient.activities.TwitterClientApp;

import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimeLineFragment extends BaseTweetsFragment {

	@Override
	public void asynLoadingData(long since, long max, long count,
			JsonHttpResponseHandler handler) {
		// TODO Auto-generated method stub
		TwitterClientApp.getRestClient().getMentions(since, max - 1, count, handler);
	}

}
