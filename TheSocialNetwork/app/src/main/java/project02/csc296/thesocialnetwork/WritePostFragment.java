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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

import project02.csc296.thesocialnetwork.model.FeedPost;
import project02.csc296.thesocialnetwork.model.FeedPostCollection;
import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;

public class WritePostFragment extends Fragment {

    private OnChildFragmentInteractionListener mListener;
    private static final String TAG = "WRITE_POST_FRAGMENT";
    public static final int RC_ATTACH_PHOTO = 2;
    private static final String KEY_PHOTO = "csc296.project02.ATTACH_PHOTO";

    private EditText mContent;
    private Uri mPicture;
    private Button mTakePicture;
    private Button mPost;
    private ImageView mPictureView;

    public WritePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        mContent = (EditText) view.findViewById(R.id.write_post_content);
        mTakePicture = (Button) view.findViewById(R.id.write_post_attach_photo);
        mPost = (Button) view.findViewById(R.id.write_post_post);
        mPictureView = (ImageView) view.findViewById(R.id.write_post_photo);
        mPicture = null;

        //Responds to Attach Photo Requests. Adds Picture to Feed Post
        mTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Attach Photo Button Pressed");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                    File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File mImage = new File(picturesDir, filename);

                    mPicture = Uri.fromFile(mImage);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPicture);

                    Log.d(TAG, getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());

                    getActivity().startActivityForResult(takePictureIntent, RC_ATTACH_PHOTO);
                }
            }
        });

        //Responds to Submit Button Press. Adds Post to Collection and Database
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User author = UserCollection.getCurrentUser();
                String content = mContent.getText().toString();
                String authorPicture = UserCollection.getCurrentUser().getProfilePhoto();
                String picturePath;
                if(mPicture == null)
                    picturePath = null;
                else
                    picturePath = mPicture.getPath();
                FeedPost feedPost = new FeedPost(author.getId().toString(), author, authorPicture, content, picturePath, null);
                FeedPostCollection.get(getContext()).addfeedPost(feedPost);
                ((HomeFragment)getParentFragment()).onChildFragmentInteraction(HomeFragment.FEED_FRAG, null);
            }
        });

        if(savedInstanceState != null){
            Uri path = (Uri) savedInstanceState.getParcelable(KEY_PHOTO);
            if(path != null)
                mPictureView.setImageBitmap(BitmapFactory.decodeFile(path.getPath()));
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int fragment, Bundle args) {
        if (mListener != null) {
            mListener.onChildFragmentInteraction(fragment, args);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChildFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChildFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onChildFragmentInteraction(int fragment, Bundle args);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "ACTIVITY RETURNED");
        if (requestCode == RC_ATTACH_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "New Photo Attached");
            mPictureView.setImageBitmap(BitmapFactory.decodeFile(mPicture.getPath()));
            mPictureView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(KEY_PHOTO, mPicture);
        super.onSaveInstanceState(savedInstanceState);
    }

}
