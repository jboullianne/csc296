/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project02.csc296.thesocialnetwork.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {


    private TextView mHometown;
    private TextView mBirthdate;
    private TextView mBio;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        mHometown = (TextView) view.findViewById(R.id.user_info_hometown);
        mBirthdate = (TextView) view.findViewById(R.id.user_info_birthdate);
        mBio = (TextView) view.findViewById(R.id.user_info_bio);

        if(getArguments() != null) {
            User user = (User) getArguments().getSerializable(SignUpFragment.KEY_USER);

            mHometown.setText(user.getHomeTown());
            mBirthdate.setText(user.getBirthDate());
            mBio.setText(user.getBio());
        }

        return view;
    }


}
