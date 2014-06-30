package chen.simpleTwitterClient.activities;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import chen.simpleTwitterClient.fragments.UserTweetsFragment;
import chen.simpleTwitterClient.models.User;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	private ImageView imgView;
	private TextView name;
	private TextView screeName;
	private TextView numOfFollowers;
	private TextView numOfFollowing;
	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		
		
		ActionBar ab = getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#33CCFF"));
		ab.setBackgroundDrawable(colorDrawable);
		
		user = (User) getIntent().getSerializableExtra("user");
		if (user == null){
			Log.d("DEBUG", "User is Null");
			return;
		}
		
		this.imgView = (ImageView) findViewById(R.id.imgProfileImage);
		this.name = (TextView) findViewById(R.id.tvNameProfile);
		this.screeName = (TextView) findViewById(R.id.tvScreenNameProfile);
		this.numOfFollowers = (TextView) findViewById(R.id.tvNumberOfFollower);
		this.numOfFollowing = (TextView) findViewById(R.id.tvNumberOfFriends);
		
		ImageLoader.getInstance().displayImage(user.getImageProfileUrl(), this.imgView);
		this.name.setText(user.getName());
		this.screeName.setText(user.getScreenName());
		this.numOfFollowers.setText(user.getFollowers());
		this.numOfFollowing.setText(user.getFollowing());
		
		fragmentManger();
		
	}

	public void fragmentManger() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		UserTweetsFragment fragment = new UserTweetsFragment();
		fragment.setUser(this.user);
		ft.replace(R.id.fglProfile, fragment);
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
		Toast.makeText(getApplicationContext(), "Fragment Create",
				Toast.LENGTH_SHORT).show();
		Toast.makeText(getApplicationContext(), "User is " + user,
				Toast.LENGTH_SHORT).show();
	}
	
}
