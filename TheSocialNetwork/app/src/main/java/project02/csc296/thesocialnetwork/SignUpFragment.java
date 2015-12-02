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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignUpFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "SIGNUP_FRAGMENT";
    public static final String KEY_USER = "csc296.project02.NEW_USER";
    public static final int RC_NEW_PROFILE_PHOTO = 1;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mFullName;
    private EditText mHomeTown;
    private EditText mBirthDate;
    private EditText mBio;
    private Button mTakeProfilePhoto;
    private Button mCancel;
    private Button mSubmit;
    private ImageView mProfilePhotoView;
    private Uri mProfilePhotoUri;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_sign_up);
        mUsername = (EditText) view.findViewById(R.id.signup_username);
        mPassword = (EditText) view.findViewById(R.id.signup_password);
        mFullName = (EditText) view.findViewById(R.id.signup_fullname);
        mHomeTown = (EditText) view.findViewById(R.id.signup_hometown);
        mBirthDate = (EditText) view.findViewById(R.id.signup_birthdate);
        mBio = (EditText) view.findViewById(R.id.signup_bio);
        mCancel = (Button) view.findViewById(R.id.signup_cancel);
        mSubmit = (Button) view.findViewById(R.id.signup_submit);
        mTakeProfilePhoto = (Button) view.findViewById(R.id.signup_take_profile_photo);
        mProfilePhotoView = (ImageView) view.findViewById(R.id.signup_profile_photo);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(0, null);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Did the user specify a profile picture
                String profilePicture;
                if(mProfilePhotoUri == null)
                    profilePicture = null;
                else
                    profilePicture = mProfilePhotoUri.getPath();

                //Checks if form is filled out correctly
                if(!validateSubmissionInfo()){
                    return;
                }

                //Creates the new User
                User user = new User(
                        null,
                        mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mFullName.getText().toString(),
                        mBirthDate.getText().toString(),
                        mHomeTown.getText().toString(),
                        mBio.getText().toString(),
                        null,
                        profilePicture);

                //Adds user to collection and database. Starts Home Fragment Exchange
                Bundle arguments = new Bundle();
                arguments.putSerializable(KEY_USER, user);
                UserCollection.get(getContext()).addUser(user);
                UserCollection.setCurrentUser(user);
                onButtonPressed(2, arguments);
            }
        });

        mTakeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "New Profile Photo Button Pressed");
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                    String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                    File picturesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File mImage = new File(picturesDir, filename);

                    mProfilePhotoUri = Uri.fromFile(mImage);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mProfilePhotoUri);

                    Log.d(TAG, getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString());

                    getActivity().startActivityForResult(takePictureIntent, RC_NEW_PROFILE_PHOTO);
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int fragment, Bundle args) {
        if (mListener != null) {
            if(args == null)
                Log.d(TAG, "bundle is NULL!!!");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int fragment, Bundle args);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_NEW_PROFILE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "New Profile Photo Selected");
            mProfilePhotoView.setImageBitmap(BitmapFactory.decodeFile(mProfilePhotoUri.getPath()));
            mProfilePhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }


    private boolean validateSubmissionInfo(){

        if(mUsername.getText().toString().equals("")){
            Toast.makeText(getContext(), "No Username input",Toast.LENGTH_SHORT).show();
            return false;
        }if(UserCollection.get(getContext()).getUserByUsername(mUsername.getText().toString()) != null){
            Toast.makeText(getContext(), "User Already Signed Up",Toast.LENGTH_SHORT).show();
            return false;
        }if(mPassword.getText().toString().length() < 6){
            Toast.makeText(getContext(), "Password Not Strong Enough",Toast.LENGTH_SHORT).show();
            return false;
        }if(mFullName.getText().toString().equals("")){
            Toast.makeText(getContext(), "Full Name Not Input",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
