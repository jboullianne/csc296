/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;


public class ProfilePictureDialog extends DialogFragment {

    private ImageView mProfilePicture;
    public static String KEY_PROFILEPICTURE = "csc296.project02.PROFILE_PICTURE";

    public ProfilePictureDialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog ( Bundle savedInstanceState )  {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Inflates layout used for displaying profile photo
        View view = inflater.inflate(R.layout.dialog_profile_picture, null);
        mProfilePicture = (ImageView) view.findViewById(R.id.dialog_profile_picture);

        if(getArguments() != null){
            String profilePicturePath = getArguments().getString(KEY_PROFILEPICTURE);
            mProfilePicture.setImageBitmap(BitmapFactory.decodeFile(profilePicturePath));
        }

        builder.setView(view);

        return  builder.create();
    }


}
