package chen.simpleTwitterClient.activities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import chen.simpleTwitterClient.models.Tweet;
import chen.simpleTwitterClient.models.User;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter<Tweet> {

	
	
	public TweetAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_layout, tweets);
	}

	// Translate method. Get data and convert it to view.
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.tweet_layout, parent, false);
		}
		// Lookup view for data population
		TextView tvScreenName = (TextView) convertView
				.findViewById(R.id.tvScreenName);
		TextView tvCreateTime = (TextView) convertView
				.findViewById(R.id.tvCreateTime);
		TextView tvUserid = (TextView) convertView.findViewById(R.id.tvUserid);
		TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
		ImageView ivProfile = (ImageView) convertView
				.findViewById(R.id.ivProfile);

		setOnClickImageListener(ivProfile, tweet.getUser());
		
		tvUserid.setText("@" + tweet.getUser().getScreenName());
		tvText.setText(tweet.getBody());
		tvScreenName.setText(tweet.getUser().getName());
		ImageLoader.getInstance().displayImage(
				tweet.getUser().getImageProfileUrl(), ivProfile);
		tvCreateTime.setText(getRelativeTimeAgo(tweet.getCreateAt()));
		// int height_in_pixels = tvText.getLineCount() *
		// tvText.getLineHeight(); //approx height text
		// Log.d("DEBUG", "Line is " + tvText.getLineCount());

		// tvText.setHeight(height_in_pixels);

		// tvCreateTime.setText(tweet.getCreateAt());

		// Return the completed view to render on screen
		return convertView;
	}
	
	public void setOnClickImageListener(ImageView imView, User user){
		imView.setOnClickListener(new ImageViewOnClick(user));
	}
	
	private class ImageViewOnClick implements OnClickListener{

		private User localUser;
		
		public ImageViewOnClick(User user){
			this.localUser = user;
		}
		
		@Override
		public void onClick(View v) {
			Toast.makeText(getContext(), "Click the image: " + localUser, Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(getContext(), ProfileActivity.class);
			intent.putExtra("user", localUser);
			getContext().startActivity(intent);
		}
		
	}
	
	private String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			Log.d("DEBUG", "Parse " + rawJsonDate);
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}
	
}
