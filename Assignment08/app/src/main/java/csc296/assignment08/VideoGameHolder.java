/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import csc296.assignment08.model.VideoGame;

/**
 * Created by JeanMarc on 10/31/15.
 */
public class VideoGameHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "VIDEO_GAME_HOLDER";

    private TextView mTitle;
    private TextView mPublisher;
    private TextView mYear;
    private VideoGame mVideoGame;

    public VideoGameHolder(View itemView) {
        super(itemView);

        mTitle = (TextView)itemView.findViewById(R.id.text_view_title);
        mPublisher = (TextView)itemView.findViewById(R.id.text_view_publisher);
        mYear = (TextView)itemView.findViewById(R.id.text_view_year);
    }

    public void bind(VideoGame game) {
        mVideoGame = game;
        mTitle.setText(game.getTitle());
        mPublisher.setText(game.getPublisher());
        mYear.setText(String.valueOf(game.getYear()));
    }
}
