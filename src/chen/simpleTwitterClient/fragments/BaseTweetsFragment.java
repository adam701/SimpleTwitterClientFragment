package chen.simpleTwitterClient.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import chen.simpleTwitterClient.activities.R;
import chen.simpleTwitterClient.activities.TweetAdapter;
import chen.simpleTwitterClient.models.Tweet;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import eu.erikw.PullToRefreshListView;

public abstract class BaseTweetsFragment extends Fragment {

	private ArrayList<Tweet> tweets;
	private TweetAdapter tweetsAdapter;
	private PullToRefreshListView timeLinesListView;
	private TweetsScrollListener onScrollListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("DEBUG", "Fragment is on create view");
		View view = inflater.inflate(R.layout.fragment_tweet_time_line, container,
				false);
		this.timeLinesListView = (PullToRefreshListView) view.findViewById(R.id.fgTimeLine);
		initTimeLineView(this.timeLinesListView);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initImageLoad();
		Log.d("DEBUG", "Created!");
	}
	
	public abstract void asynLoadingData(long since, long max, long count, JsonHttpResponseHandler handler);

	public void initTimeLineView(final PullToRefreshListView timeLinesListViewLocal) {
		Log.d("DEBUG", "initTimeLineView");
		
		this.tweets = new ArrayList<Tweet>();
		this.tweetsAdapter = new TweetAdapter(getActivity(), this.tweets);
		timeLinesListViewLocal.setAdapter(tweetsAdapter);
		
		if (isNetworkAvailable()) {
			Log.d("DEBUG", "Online");
		} else {
			Log.d("DEBUG", "Offline");
			for (Tweet tweet : Tweet.recentTweets()) {
				Log.d("DEBUG", tweet.toString());
				tweetsAdapter.add(tweet);
			}
			return;
		}
		// Setup Scroll listener
		onScrollListener = new TweetsScrollListener(this.tweetsAdapter, timeLinesListViewLocal) {

			@Override
			public void loadingMoreData(long since, long max, long count,
					JsonHttpResponseHandler jsonHandler) {
				asynLoadingData(since, max, count, jsonHandler);
			}
			
		};
		onScrollListener.onRefresh();
		timeLinesListViewLocal.setOnRefreshListener(onScrollListener);
		timeLinesListViewLocal.setOnScrollListener(onScrollListener);
	}
	
	private void initImageLoad(){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity())
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.threadPoolSize(3)
				// default
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.build();
		ImageLoader.getInstance().init(config);
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	public void manuallyOnRefresh(){
		onScrollListener.onRefresh();
	}
	

	
	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { if (resultCode == android.app.Activity.RESULT_OK) { if
	 * (requestCode == REQUEST_CODE) { String bodyString =
	 * data.getStringExtra("body"); Log.d("DEBUG", "Body is " + bodyString);
	 * TwitterClientApp.getRestClient().tweet(bodyString, new
	 * JsonHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(JSONObject jsonObject) {
	 * //if(onRefreshListenerWithStatus == null){ //Log.d("DEBUG",
	 * "onRefreshListenerWithStatus is null"); //}
	 * onRefreshListenerWithStatus.onRefresh(); }
	 * 
	 * @Override public void onFailure(Throwable arg0, String arg1) {
	 * Log.d("DEBUG", "Json Handler Error"); Log.d("DEBUG", arg0.toString());
	 * Log.d("DEBUG", arg1); }
	 * 
	 * @Override protected void handleFailureMessage(Throwable arg0, String
	 * arg1) { Log.d("DEBUG", "Fail to POST"); Log.d("DEBUG", arg0.toString());
	 * Log.d("DEBUG", arg1); }
	 * 
	 * }); } } }
	 */

}
