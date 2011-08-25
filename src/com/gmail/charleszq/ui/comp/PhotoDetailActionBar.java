/**
 * 
 */

package com.gmail.charleszq.ui.comp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.Photo;
import com.gmail.charleszq.PhotoLocationActivity;
import com.gmail.charleszq.R;
import com.gmail.charleszq.event.IUserInfoFetchedListener;
import com.gmail.charleszq.task.GetUserInfoTask;

/**
 * Represents the UI component that provides a list of actions which can be
 * performed on a photo, for example, when showing my contact list, for each
 * contact item, we can place this UI component somewhere, with this action bar,
 * user can view this user's detail information, see his/her public photos, etc.
 * 
 * @author charles
 */
public class PhotoDetailActionBar extends FrameLayout implements
		IUserInfoFetchedListener, OnClickListener {

	private ImageView mBuddyIcon;
	private TextView mUserName;

	private Photo mCurrentPhoto;
	private ImageView mLocationButton;

	public PhotoDetailActionBar(Context context) {
		super(context);
		buildLayout();
	}

	public PhotoDetailActionBar(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		buildLayout();
	}

	public PhotoDetailActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		buildLayout();
	}

	/**
	 * Builds the layout.
	 */
	protected void buildLayout() {
		LayoutInflater li = LayoutInflater.from(getContext());
		li.inflate(R.layout.photo_detail_action_bar, this, true);

		mBuddyIcon = (ImageView) this.findViewById(R.id.user_icon);
		mBuddyIcon.setOnClickListener(this);

		mUserName = (TextView) findViewById(R.id.user_name);
		mUserName.setText(getContext().getResources().getString(
				R.string.loading_user_info));
		mLocationButton = (ImageView) findViewById(R.id.btn_show_on_map);
		mLocationButton.setOnClickListener(this);
	}

	/**
	 * @param owner
	 */
	public void setPhoto(Photo photo) {
		this.mCurrentPhoto = photo;
		if (mCurrentPhoto.getGeoData() != null) {
			mLocationButton.setVisibility(View.VISIBLE);
		} else {
			mLocationButton.setVisibility(View.INVISIBLE);
		}
		GetUserInfoTask task = new GetUserInfoTask(mBuddyIcon, this, null);
		task.execute(mCurrentPhoto.getOwner().getId());
	}

	@Override
	public void onUserInfoFetched(User user) {
		mUserName.setText(user.getUsername());
		ProgressBar pbar = (ProgressBar) findViewById(R.id.progress);
		pbar.setVisibility(INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		if (v == mBuddyIcon) {
			String url = "http://www.flickr.com/photos/" + mCurrentPhoto.getOwner().getId(); //$NON-NLS-1$
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			this.getContext().startActivity(intent);
		} else if (v == mLocationButton) {
			int lat = (int) (mCurrentPhoto.getGeoData().getLatitude() * 1E6);
			int lng = (int) (mCurrentPhoto.getGeoData().getLongitude() * 1E6);
			Intent intent = new Intent(getContext(),
					PhotoLocationActivity.class);
			intent.putExtra(PhotoLocationActivity.LAT_VAL, lat);
			intent.putExtra(PhotoLocationActivity.LONG_VAL, lng);
			intent.putExtra(PhotoLocationActivity.PHOTO_ID, mCurrentPhoto.getId());
			getContext().startActivity(intent);
		}
	}

}
