/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import csc296.assignment08.model.VideoGame;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGameAdapter extends RecyclerView.Adapter<VideoGameHolder> {

    private static final String TAG = "VIDEO_GAME_ADAPTER";

    private List<VideoGame> mVideoGames; //Holds the List of Video Games

    public VideoGameAdapter(List<VideoGame> games) {
        mVideoGames = games;
    }

    @Override
    public VideoGameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_video_game, parent, false);

        VideoGameHolder holder = new VideoGameHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoGameHolder holder, int position) {
        holder.bind(mVideoGames.get(position));
    }

    @Override
    public int getItemCount() {
        return mVideoGames.size();
    }

    //Updates the videogames List
    public void setVideoGames(List<VideoGame> videoGames) {
        mVideoGames = videoGames;
        notifyDataSetChanged();
    }
}
