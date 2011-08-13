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

    private User mPhotoOwner;

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
        mUserName.setText(getContext().getResources().getString(R.string.loading_user_info));

    }

    /**
     * @param owner
     */
    public void setUser(User owner) {
        this.mPhotoOwner = owner;
        mUserName.setText(mPhotoOwner.getUsername());
        GetUserInfoTask task = new GetUserInfoTask(mBuddyIcon, this, null);
        task.execute(owner.getId());
    }

    @Override
    public void onUserInfoFetched(User user) {
        mUserName.setText(user.getUsername());
        ProgressBar pbar = (ProgressBar) findViewById(R.id.progress);
        pbar.setVisibility(INVISIBLE);
    }

	@Override
	public void onClick(View v) {
		String url = "http://www.flickr.com/photos/" + mPhotoOwner.getId(); //$NON-NLS-1$
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		this.getContext().startActivity(intent);
	}

}
