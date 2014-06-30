package chen.simpleTwitterClient.fragments;

import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import chen.simpleTwitterClient.activities.TweetAdapter;
import chen.simpleTwitterClient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsScrollListener implements OnScrollListener,
		OnRefreshListener {

	private int visibleThreshold = 6;
	private AtomicBoolean loading = new AtomicBoolean(false);
	private long since;
	private long max;
	private final long COUNT = 12;
	private AtomicBoolean isInitialized = new AtomicBoolean(false);
	private TweetAdapter tweetsAdapter;
	private PullToRefreshListView timeLinesListView;

	public TweetsScrollListener(final TweetAdapter tweetsAdapter,
			final PullToRefreshListView timeLinesListView) {
		this.tweetsAdapter = tweetsAdapter;
		this.timeLinesListView = timeLinesListView;
	}

	// This happens many times a second during a scroll, so be wary of the code
	// you place here.
	// We are given a few useful parameters to help us work out if we need to
	// load some more data,
	// but first we check if we are waiting for the previous load to finish.
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// If the total item count is zero and the previous isn't, assume the
		// list is invalidated and should be reset back to initial state
		if (!isInitialized.get() || tweetsAdapter.getCount() <= 0) {
			return;
		}
		long currentMaxId = tweetsAdapter.getItem(tweetsAdapter.getCount() - 1)
				.getUid();
		Log.d("DEBUG", "Current Max id is" + currentMaxId);
		Log.d("DEBUG", "Previous Max id is" + max);
		if (currentMaxId > max) {
			max = currentMaxId;
		}
		if (loading.get() && (currentMaxId < max)) {
			loading.set(false);
			max = currentMaxId;
		}
		Log.d("DEBUG", "Laoding status is " + loading.get());
		if (!loading.get()
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			Log.d("DEBUG", "Begin to load ");
			loadingMoreData(-1, max, this.COUNT, new TweetScrollDownLoadingJsonHttpResponseHandler());
			loading.set(true);
		}
	}

	public abstract void loadingMoreData(long since, long max, long count, JsonHttpResponseHandler jsonHandler);
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Don't take any action on changed
	}

	public void setInitializedStatus(boolean status) {
		this.isInitialized.set(status);
	}

	/**
	 * Pull Down Handler
	 */

	@Override
	public void onRefresh() {
		Log.d("DEBUG", "Since is " + since);
		if (loading.getAndSet(true)) {
			return;
		}
		if (!isInitialized.get()) {
			loadingMoreData(-1, -1, 12, new PullDownJsonHandler());
		} else {
			loadingMoreData(since, -1, 12, new PullDownJsonHandler());
		}
	}
	
	/**
	 * JsonHandler for PullDown
	 * @author adam701
	 *
	 */
	
	private class PullDownJsonHandler extends JsonHttpResponseHandler {
		@Override
		public void onFailure(Throwable arg0, String arg1) {
			Log.d("DEBUG", "Json Handler Error");
			Log.d("DEBUG", arg0.toString());
			Log.d("DEBUG", arg1);
			loading.set(false);
			timeLinesListView.onRefreshComplete();
		}

		@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
			Log.d("DEBUG", "Fail to GET");
			Log.d("DEBUG", arg0.toString());
			Log.d("DEBUG", arg1);
			loading.set(false);
			timeLinesListView.onRefreshComplete();
		}

		@Override
		public void onSuccess(JSONArray jsonArray) {
			Log.d("DEBUG", "Json Loading On Success");
			Log.d("DEBUG", jsonArray.toString());
			for (int i = jsonArray.length() - 1; i >= 0; i--) {
				JSONObject jsonObject;
				try {
					jsonObject = jsonArray.getJSONObject(i);
					Log.d("DEBUG", jsonObject.toString());
					Tweet tweet = Tweet.fromJSON(jsonObject);
					Log.d("DEBUG", "Final tweet is " + tweet.toString());
					tweetsAdapter.insert(tweet, 0);
				} catch (JSONException e) {
					Log.d("DEBUG", "Fail to get json Object!");
					e.printStackTrace();
				}
			}
			if (tweetsAdapter.getCount() == 0){
				return;
			}
			if(!isInitialized.get()){
				since = tweetsAdapter.getItem(0).getUid();
				max = tweetsAdapter.getItem(tweetsAdapter.getCount() - 1).getUid();
				isInitialized.set(true);
			}
			since = tweetsAdapter.getItem(0).getUid();
			loading.set(false);
			timeLinesListView.onRefreshComplete();
		}
	}

	
	/**
	 * Json Handler for scroll down
	 * 
	 * @author adam701
	 *
	 */
	private class TweetScrollDownLoadingJsonHttpResponseHandler extends
			JsonHttpResponseHandler {

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			// TODO Auto-generated method stub
			Log.d("DEBUG", "Json Handler Error");
			Log.d("DEBUG", arg0.toString());
			Log.d("DEBUG", arg1);
			loading.set(false);
		}

		@Override
		protected void handleFailureMessage(Throwable arg0, String arg1) {
			Log.d("DEBUG", "Fail to load");
			Log.d("DEBUG", arg0.toString());
			Log.d("DEBUG", arg1);
			loading.set(false);
		}

		@Override
		public void onSuccess(JSONArray jsonArray) {
			Log.d("DEBUG", "Json Loading On Success");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject;
				try {
					jsonObject = jsonArray.getJSONObject(i);
					Log.d("DEBUG", jsonObject.toString());
					Tweet tweet = Tweet.fromJSON(jsonObject);
					Log.d("DEBUG", "Final tweet is " + tweet.toString());
					tweetsAdapter.add(tweet);
				} catch (JSONException e) {
					Log.d("DEBUG", "Fail to get json Object!");
					e.printStackTrace();
				}
			}
			loading.set(false);
		}
	}

}