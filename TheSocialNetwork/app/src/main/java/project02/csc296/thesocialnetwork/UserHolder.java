/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import project02.csc296.thesocialnetwork.model.FeedPost;
import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;

/**
 * Created by JeanMarc on 11/3/15.
 */
public class UserHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "USER_HOLDER";

    private TextView mFullName;
    private ImageView mProfilePicture;
    private ImageView mIsFavorite;
    private User mUser;

    public UserHolder(final View itemView) {
        super(itemView);

        //Grabs references to widgets used in the holder. Sets onClickListeners too.
        mFullName = (TextView)itemView.findViewById(R.id.favorite_fullname);
        mFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(SignUpFragment.KEY_USER, mUser);
                ((MainActivity)v.getContext()).onFragmentInteraction(MainActivity.FRAGMENT_HOME, args);
            }
        });
        mProfilePicture = (ImageView) itemView.findViewById(R.id.favorite_profile_picture);
        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(SignUpFragment.KEY_USER, mUser);
                ((MainActivity)v.getContext()).onFragmentInteraction(MainActivity.FRAGMENT_HOME, args);
            }
        });
        mIsFavorite = (ImageView)itemView.findViewById(R.id.favorite_is_favorite);
        mIsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Unimplemented as of right now. "Extra Credit"
            }
        });

    }

    public void bind(User user) {
        //Sets widgets based on User that is being bound
        mFullName.setText(user.getFullName());
        mProfilePicture.setImageBitmap(BitmapFactory.decodeFile(user.getProfilePhoto()));
        mProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (UserCollection.getCurrentUser().getFavorites().contains(user.getId())){
            mIsFavorite.setImageResource(R.mipmap.ic_favorite_true);
        }else if (UserCollection.getCurrentUser().getId().equalsIgnoreCase(user.getId())){
            //do nothing
        }else{
            mIsFavorite.setImageResource(R.drawable.ic_favorite);
        }
        this.mUser = user;
    }


}
