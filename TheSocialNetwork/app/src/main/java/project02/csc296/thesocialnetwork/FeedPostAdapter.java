/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project02.csc296.thesocialnetwork.model.FeedPost;

/**
 * Created by JeanMarc on 11/2/15.
 */
public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostHolder> {

    private static final String TAG = "FEED_POST_ADAPTER";

    private List<FeedPost> mFeedPosts; //Holds the List of FeedPosts

    public FeedPostAdapter(List<FeedPost> feedPosts) {
        mFeedPosts = feedPosts;
    }

    @Override
    public FeedPostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflates the layout used for displaying one Feed Post
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_feed_post, parent, false);

        FeedPostHolder holder = new FeedPostHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedPostHolder holder, int position) {
        holder.bind(mFeedPosts.get(position));
    }

    @Override
    public int getItemCount() {
        return mFeedPosts.size();
    }

    //Updates the FeedPosts List
    public void setFeedPosts(List<FeedPost> feedPosts) {
        mFeedPosts = feedPosts;
        notifyDataSetChanged();
    }
}
