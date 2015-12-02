/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project02.csc296.thesocialnetwork.database.Utilities;
import project02.csc296.thesocialnetwork.model.UserCollection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "LOGIN_FRAGMENT";
    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mUsernameField;
    private EditText mPasswordField;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_login, container, false);
        //Setting References To Widgets
        mLoginButton = (Button) view.findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin(v);
            }
        });
        mSignUpButton = (Button) view.findViewById(R.id.button_signup);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSignUp(v);
            }
        });
        mUsernameField = (EditText) view.findViewById(R.id.username_field);
        mPasswordField = (EditText) view.findViewById(R.id.password_field);

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

    //Starts Sign-up fragment transaction
    public void requestSignUp(View view) {
        Log.d(TAG, "User Pressed SignUp Button");
        onButtonPressed(1, null);
    }

    //Tests to see if Login Credentials are correct.
    public void requestLogin(View view) {
        Log.d(TAG, "User Pressed Login Button");
        Log.d(TAG, "This is not secure... Username: " + mUsernameField.getText().toString() + " ; Password: " + mPasswordField.getText().toString());
        if(UserCollection.get(getContext()).requestLogin(mUsernameField.getText().toString(), mPasswordField.getText().toString())){
            Bundle args = new Bundle();
            UserCollection.setCurrentUser(UserCollection.get(getContext()).getUserByUsername(mUsernameField.getText().toString()));
            args.putSerializable(SignUpFragment.KEY_USER, UserCollection.get(getContext()).getUserByUsername(mUsernameField.getText().toString()));
            onButtonPressed(2, args);
        }else{
            Toast.makeText(getContext(), "Incorrect Username/Password Entered", Toast.LENGTH_SHORT).show();
        }

    }

}
