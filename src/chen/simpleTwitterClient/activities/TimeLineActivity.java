package chen.simpleTwitterClient.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import chen.simpleTwitterClient.fragments.BaseTweetsFragment;
import chen.simpleTwitterClient.fragments.HomeTimeLineFragment;
import chen.simpleTwitterClient.fragments.MentionsTimeLineFragment;
import chen.simpleTwitterClient.models.User;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.viewpagerindicator.TitlePageIndicator;

public class TimeLineActivity extends FragmentActivity {

	private final int REQUEST_CODE = 200;
	private User currentUser;
	private MyPagerAdapter pagerAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("DEBUG", "Created!");
		setContentView(R.layout.activity_time_line);
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#33CCFF"));
		ab.setBackgroundDrawable(colorDrawable);		
		
		init();
	}

	public void fragmentManger() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fglTweets, new HomeTimeLineFragment());
		ft.commit();
		Toast.makeText(getApplicationContext(), "Fragment Create",
				Toast.LENGTH_SHORT).show();
	}

	public void onCompose(MenuItem v) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	public void onProfile(MenuItem v) {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("user", this.currentUser);
		startActivity(intent);
	}

	public void init() {

		/*
		 * Load current user infor
		 */
		loadingLoginUserInfo();
		/*
		 * Page view
		 */
		ViewPager pager = (ViewPager) findViewById(R.id.vpPager);
		this.pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(this.pagerAdapter);
		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	public void loadingLoginUserInfo() {
		TwitterClientApp.getRestClient().getCurrentUserInfo(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObj) {
						Log.d("DEBUG", jsonObj.toString());
						currentUser = User.fromJSON(jsonObj);
						Toast.makeText(getApplicationContext(),
								currentUser.toString(), Toast.LENGTH_SHORT)
								.show();
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE) {
				String bodyString = data.getStringExtra("body");
				Log.d("DEBUG", "Body is " + bodyString);
				TwitterClientApp.getRestClient().tweet(bodyString,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject jsonObject) {
							    pagerAdapter.refreshAllFragment();
							}

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Log.d("DEBUG", "Json Handler Error");
								
							}

							@Override
							protected void handleFailureMessage(Throwable arg0,
									String arg1) {
								Log.d("DEBUG", "Fail to POST");
								Log.d("DEBUG", arg0.toString() + " " + arg1);
							}

						});
			}
		}
	}

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 2;
		private Fragment homeTimeLine = new HomeTimeLineFragment();
		private Fragment mentions = new MentionsTimeLineFragment();

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show FirstFragment
				return homeTimeLine;
			case 1: // Fragment # 0 - This will show FirstFragment different
					// title
				return mentions;
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "Home";
			} else if (position == 1) {
				return "Mentions";
			} else {
				return null;
			}
		}
		
		public void refreshAllFragment(){
			((BaseTweetsFragment) this.homeTimeLine).manuallyOnRefresh();
			((BaseTweetsFragment) this.mentions).manuallyOnRefresh();
		}

	}

}
