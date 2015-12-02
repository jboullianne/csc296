package csc296.assignment09;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class SoundListFragment extends Fragment {

    private static final String TAG = "SoundList_Fragment";

    private RecyclerView mSoundView;
    private SoundSystem mSoundSystem;
    private DetailItemClickListener mListener;

    public SoundListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(TAG, "Creating SoundSystem");
        mSoundSystem = new SoundSystem(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_track_list, container, false);

        mSoundView = (RecyclerView) view.findViewById(R.id.recycler_view_sound_list);
        mSoundView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSoundView.setAdapter(new SoundAdapter(mSoundSystem.getSounds()));

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (DetailItemClickListener) context;
        }catch(ClassCastException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (DetailItemClickListener) activity;
        }catch(ClassCastException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() called");
        mSoundSystem.release();
    }

    public interface DetailItemClickListener {
        public void OnDetailItemClicked(Sound sound);
    }


    private class SongHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTrackName;
        private TextView mAlbumName;
        private TextView mArtistName;
        private Sound mSound;

        public SongHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.view_sound, container, false));

            mTrackName = (TextView)itemView.findViewById(R.id.text_view_sound_name);
            mAlbumName = (TextView)itemView.findViewById(R.id.text_view_album);
            mArtistName = (TextView)itemView.findViewById(R.id.text_view_artist);

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);
        }

        public void bind(Sound sound) {
            mSound = sound;
            mTrackName.setText(sound.getName());
            mAlbumName.setText(sound.getAlbum());
            mArtistName.setText(sound.getArtist());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "Sound Clicked");
            mSoundSystem.play(mSound);
            mListener.OnDetailItemClicked(mSound);
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SongHolder> {
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @Override
        public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new SongHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(SongHolder holder, int position) {
            holder.bind(mSounds.get(position));
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }
}
