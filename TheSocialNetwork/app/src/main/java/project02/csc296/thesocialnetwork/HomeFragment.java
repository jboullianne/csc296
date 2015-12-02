/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.LinkedList;
import java.util.UUID;

import project02.csc296.thesocialnetwork.database.Utilities;
import project02.csc296.thesocialnetwork.model.FeedPostCollection;
import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;

public class HomeFragment extends Fragment{

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "HOME_FRAGMENT";
    public static final int RC_CHANGE_PROFILE_PHOTO = 3;

    public static final int FEED_FRAG = 1;
    public static final int SEE_FAVORITES_FRAG = 2;
    public static final int SEE_MORE_INFO = 3;

    private ImageView mProfilePictureView;
    private TextView mFullName;
    private TextView mUsername;

    private ImageButton mWritePost;
    private ImageButton mUserFeed;
    private ImageButton mFavorite;
    private ImageButton mSeeFavorites;
    private ImageButton mUserInfo;

    private RelativeLayout mChangeBackground;
    private RelativeLayout mChangeProfilePicture;
    private User mPageUser;
    private String mTempProfilePhoto;


    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_home);
        setHasOptionsMenu(true);

        mProfilePictureView = (ImageView) view.findViewById(R.id.home_profile_picture_view);
        mFullName = (TextView) view.findViewById(R.id.home_fullname);
        mUsername = (TextView) view.findViewById(R.id.home_username);
        mWritePost = (ImageButton) view.findViewById(R.id.home_write_post);
        mUserFeed = (ImageButton) view.findViewById(R.id.home_user_feed);
        mFavorite = (ImageButton) view.findViewById(R.id.home_favorite);
        mSeeFavorites = (ImageButton) view.findViewById(R.id.home_see_favorites);
        mUserInfo = (ImageButton) view.findViewById(R.id.home_more_info);

        mChangeBackground = (RelativeLayout) view.findViewById(R.id.home_change_background);
        mChangeProfilePicture = (RelativeLayout) view.findViewById(R.id.home_change_profile_picture);

        //If Coming from the signup fragment, it should have a new user as an argument
        if(getArguments() != null){
            User user = (User) getArguments().getSerializable(SignUpFragment.KEY_USER);
            Log.d(TAG, "Welcome " + user.getFullName() + "!");
            mPageUser = user;

            mProfilePictureView.setImageBitmap(BitmapFactory.decodeFile(user.getProfilePhoto()));
            mProfilePictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mFullName.setText(user.getFullName());
            mUsername.setText(user.getUsername());

            if(!UserCollection.getCurrentUser().getUsername().equals(mPageUser.getUsername())){
                mChangeProfilePicture.setVisibility(View.GONE);
                mChangeBackground.setVisibility(View.GONE);
                mWritePost.setVisibility(View.GONE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mPageUser.getFullName());
                if(UserCollection.getCurrentUser().getFavorites().contains(mPageUser.getId())){
                    mFavorite.setImageResource(R.mipmap.ic_favorite_true);
                }

            }else{
                mFavorite.setVisibility(View.GONE);
            }

        }
        //If fragment was reloaded, it should have an User stored
        else if(savedInstanceState != null){
            User user = (User) savedInstanceState.getSerializable(SignUpFragment.KEY_USER);
            Log.d(TAG, "Welcome " + user.getFullName() + "!");
            mPageUser = user;

            mProfilePictureView.setImageBitmap(BitmapFactory.decodeFile(user.getProfilePhoto()));
            mProfilePictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mFullName.setText(user.getFullName());
            mUsername.setText(user.getUsername());

            if(!UserCollection.getCurrentUser().getUsername().equals(mPageUser.getUsername())){
                mChangeProfilePicture.setVisibility(View.GONE);
                mChangeBackground.setVisibility(View.GONE);
                mWritePost.setVisibility(View.GONE);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mPageUser.getFullName());
                if(UserCollection.getCurrentUser().getFavorites().contains(mPageUser.getId())){
                    mFavorite.setImageResource(R.mipmap.ic_favorite_true);
                }

            }else{
                mFavorite.setVisibility(View.GONE);
            }

        }else{
            mChangeProfilePicture.setVisibility(View.INVISIBLE);
            mChangeBackground.setVisibility(View.INVISIBLE);
        }

        mWritePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChildFragmentManager().beginTransaction().replace(R.id.home_feed_frame, new WritePostFragment(), "Write Post").commit();
            }
        });

        mUserFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChildFragmentInteraction(FEED_FRAG, null);
            }
        });

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User tmp = UserCollection.getCurrentUser();
                LinkedList<String> list = tmp.getFavorites();
                if(tmp.getFavorites().contains(mPageUser.getId())){ //If already a favorite
                    list.remove(mPageUser.getId());
                    mFavorite.setImageResource(R.drawable.ic_favorite);
                }else{
                    list.add(mPageUser.getId());
                    mFavorite.setImageResource(R.mipmap.ic_favorite_true);
                }
                tmp.setFavorites(list);
                UserCollection.get(getContext()).updateUser(tmp);
            }
        });

        mSeeFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChildFragmentInteraction(SEE_FAVORITES_FRAG, null);
            }
        });

        mUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChildFragmentInteraction(SEE_MORE_INFO, null);
            }
        });

        mProfilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new ProfilePictureDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ProfilePictureDialog.KEY_PROFILEPICTURE, mPageUser.getProfilePhoto());
                dialog.setArguments(bundle);
                dialog.show(getChildFragmentManager(), "ProfilePictureDialog");
            }
        });

        mChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "New Profile Photo Button Pressed");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                    File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File mImage = new File(picturesDir, filename);

                    mTempProfilePhoto = Uri.fromFile(mImage).getPath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImage));

                    Log.d(TAG, getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());

                    getActivity().startActivityForResult(takePictureIntent,RC_CHANGE_PROFILE_PHOTO );
                }
            }
        });

        //Finally, if no saved instance state, then load default fragment (Feed Fragment)
        if(savedInstanceState == null) {
            FeedRecyclerFragment feedRecyclerFragment = new FeedRecyclerFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(SignUpFragment.KEY_USER, mPageUser);
            arguments.putString(Utilities.KEY_WHERE, "user");
            feedRecyclerFragment.setArguments(arguments);
            getChildFragmentManager().beginTransaction().add(R.id.home_feed_frame, feedRecyclerFragment).commit();
        }

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int fragment, Bundle args) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragment, args);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int fragment, Bundle args);
    }

    //If one of the button on the fragment are pressed. Process Child Fragment Transaction.
    public void onChildFragmentInteraction(int fragment, Bundle args){
        Log.d(TAG, "Fragment: " + fragment);

        FeedRecyclerFragment feedRecyclerFragment = new FeedRecyclerFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(SignUpFragment.KEY_USER, mPageUser);

        switch(fragment){
            case FEED_FRAG:
                arguments.putString(Utilities.KEY_WHERE, "user");
                feedRecyclerFragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction().replace(R.id.home_feed_frame, feedRecyclerFragment).commit();
                break;
            case SEE_FAVORITES_FRAG:
                arguments.putString(Utilities.KEY_WHERE, "favorites");
                feedRecyclerFragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction().replace(R.id.home_feed_frame, feedRecyclerFragment).commit();
                break;
            case SEE_MORE_INFO:
                UserInfoFragment userInfoFragment = new UserInfoFragment();
                userInfoFragment.setArguments(arguments);
                getChildFragmentManager().beginTransaction().replace(R.id.home_feed_frame, userInfoFragment).commit();
        }
    }

    public void writePostResult(int requestCode, int resultCode, Intent data) {
        getChildFragmentManager().findFragmentByTag("Write Post").onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //Coming back from the User changing their profile photo
        if(requestCode == RC_CHANGE_PROFILE_PHOTO){
            mProfilePictureView.setImageBitmap(BitmapFactory.decodeFile(mTempProfilePhoto));

            String oldPath = mPageUser.getProfilePhoto();
            Log.d(TAG, "OLD PATH: " + oldPath);

            User current = UserCollection.getCurrentUser();
            current.setProfilePhoto(mTempProfilePhoto);
            mPageUser.setProfilePhoto(mTempProfilePhoto);
            UserCollection.get(getContext()).updateUser(current);

            FeedPostCollection.get(getContext()).updateAuthorPictures(oldPath, mTempProfilePhoto);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(SignUpFragment.KEY_USER, mPageUser);
        super.onSaveInstanceState(savedInstanceState);
    }

}
