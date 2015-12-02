package csc296.assignment09;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SoundDetailFragment extends Fragment {

    private static final String TAG = "SoundDetailFragment";
    private String mSoundName;
    private TextView mName;

    public static final String KEY_SOUND_NAME = "csc296.assignment09.KEY_SOUND_NAME";

    public SoundDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sound_detail, container, false);

        if(getArguments() != null){
            Bundle args = getArguments();
            mSoundName = args.getString(KEY_SOUND_NAME);
            mName = (TextView) view.findViewById(R.id.sound_detail_name);
            mName.setText(mSoundName);
        }else if(savedInstanceState != null){
            mSoundName = savedInstanceState.getString(KEY_SOUND_NAME);
            mName = (TextView) view.findViewById(R.id.sound_detail_name);
            mName.setText(mSoundName);
        }

        return view;

    }

    public static SoundDetailFragment newFragment(String soundName){
        Log.d(TAG, "Creating new SoundDetailFragment");

        Bundle args = new Bundle();
        args.putString(KEY_SOUND_NAME, soundName);
        SoundDetailFragment fragment = new SoundDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
