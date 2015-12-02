/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import project02.csc296.thesocialnetwork.model.FeedPost;
import project02.csc296.thesocialnetwork.model.UserCollection;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "VIDEO_GAME_HOLDER";

    private TextView mAuthor;
    private ImageView mAuthorPicture;
    private TextView mContent;
    private ImageView mPicture;
    private TextView mTimeStamp;

    public FeedPostHolder(final View itemView) {
        super(itemView);

        mAuthor = (TextView)itemView.findViewById(R.id.feed_post_author);
        mAuthorPicture = (ImageView) itemView.findViewById(R.id.feed_post_author_picture);
        mContent = (TextView)itemView.findViewById(R.id.feed_post_content);
        mTimeStamp = (TextView)itemView.findViewById(R.id.feed_post_timestamp);
        mPicture = (ImageView)itemView.findViewById(R.id.feed_post_picture);

    }

    public void bind(FeedPost feedPost) {
        mAuthor.setText(feedPost.getAuthor().getFullName());
        if(mAuthorPicture != null){
            mAuthorPicture.setImageBitmap(BitmapFactory.decodeFile(feedPost.getAuthorPicture()));
            mAuthorPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if(feedPost.getContent() != null)
            mContent.setText(feedPost.getContent());
        if(feedPost.getPicture() != null)
            mPicture.setImageBitmap(BitmapFactory.decodeFile(feedPost.getPicture()));
        mTimeStamp.setText(feedPost.getTimeStamp());
    }
}
