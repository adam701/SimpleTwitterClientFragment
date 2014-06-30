package chen.simpleTwitterClient.fragments;

import chen.simpleTwitterClient.activities.TwitterClientApp;

import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimeLineFragment extends BaseTweetsFragment {

	@Override
	public void asynLoadingData(final long since, final long max, final long count,
			final JsonHttpResponseHandler handler) {
		TwitterClientApp.getRestClient().getHomeTimeLine(since, max - 1, count, handler);
	}
	
}
