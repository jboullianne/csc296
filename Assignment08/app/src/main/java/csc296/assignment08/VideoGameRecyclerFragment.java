/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import csc296.assignment08.model.Collection;
import csc296.assignment08.model.VideoGame;

public class VideoGameRecyclerFragment extends Fragment {

    private static final String TAG = "VG_RECYCLER_FRAGMENT";

    private RecyclerView mRecyclerView;
    private VideoGameAdapter mAdapter;

    public VideoGameRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_game_recycler, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_video_games);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Sets the dataset on the RecyclerView
        List<VideoGame> videoGames = Collection.get(getContext()).getVideoGames();
        mAdapter = new VideoGameAdapter(videoGames);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();

        return view;
    }

    //Updates the UI after the dataset changes
    @Override
    public void onResume() {
        super.onResume();

        List<VideoGame> videoGames = Collection.get(getContext()).getVideoGames();
        Log.d(TAG, videoGames.toString());
        if(mAdapter == null) {
            mAdapter = new VideoGameAdapter(videoGames);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setVideoGames(videoGames);
        }
    }

}
