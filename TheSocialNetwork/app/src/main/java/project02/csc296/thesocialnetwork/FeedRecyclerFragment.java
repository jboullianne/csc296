/*
Jean-Marc Boullianne
CSC 296: Fall 2015
Project 02
 */

package project02.csc296.thesocialnetwork;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project02.csc296.thesocialnetwork.database.Utilities;
import project02.csc296.thesocialnetwork.model.FeedPost;
import project02.csc296.thesocialnetwork.model.FeedPostCollection;
import project02.csc296.thesocialnetwork.model.User;
import project02.csc296.thesocialnetwork.model.UserCollection;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedRecyclerFragment extends Fragment {

    private static final String TAG = "FEED_FRAGMENT";

    private RecyclerView mFeed;
    private RecyclerView.Adapter mAdapter;
    private User baseUser;
    private String where;
    private String feedType;


    public FeedRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_recycler, container, false);


        mFeed = (RecyclerView) view.findViewById(R.id.recycler_feed);
        mFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        if(getArguments() != null){
            where = getArguments().getString(Utilities.KEY_WHERE);
            baseUser = (User) getArguments().getSerializable(SignUpFragment.KEY_USER);
            switch(where){
                case "user":
                    where = "id=\'" + baseUser.getId() + "\'";
                    //Sets the dataset on the RecyclerView
                    List<FeedPost> posts = FeedPostCollection.get(getContext()).getFeedPosts(where, null);
                    mAdapter = new FeedPostAdapter(posts);
                    mFeed.setAdapter(mAdapter);
                    mFeed.getAdapter().notifyDataSetChanged();
                    feedType = "feed_post";
                    break;
                case "favorites":
                    where = "id=\'" + baseUser.getId() + "\'";
                    //Sets the dataset on the RecyclerView
                    List<User> favorites = UserCollection.get(getContext()).getFavorites(baseUser);
                    mAdapter = new UserAdapter(favorites);
                    mFeed.setAdapter(mAdapter);
                    mFeed.getAdapter().notifyDataSetChanged();
                    feedType = "favorites";
                    break;
                case "favorites_feed":
                    where = "id=\'" + baseUser.getId() + "\'";
                    //Sets the dataset on the RecyclerView
                    List<FeedPost> favoritesPosts = FeedPostCollection.get(getContext()).getFavoritesPosts(baseUser);
                    mAdapter = new FeedPostAdapter(favoritesPosts);
                    mFeed.setAdapter(mAdapter);
                    mFeed.getAdapter().notifyDataSetChanged();
                    feedType = "favorites_feed";
                    break;
                case "all_users":
                    List<User> allUsers = UserCollection.get(getContext()).getUsers();
                    mAdapter = new UserAdapter(allUsers);
                    mFeed.setAdapter(mAdapter);
                    mFeed.getAdapter().notifyDataSetChanged();
                    feedType = "all_users";
                    break;
            }
        }else {
            Log.d(TAG, "Arguments Null");
            where = null;
            //Sets the dataset on the RecyclerView
            List<FeedPost> posts = FeedPostCollection.get(getContext()).getFeedPosts(null, null);
            mAdapter = new FeedPostAdapter(posts);
            mFeed.setAdapter(mAdapter);
            mFeed.getAdapter().notifyDataSetChanged();
        }

        return view;
    }

    //Refreshes the RecyclerView on Resume
    @Override
    public void onResume() {
        super.onResume();

        //Depending on what the recyclerView is displaying, it sets a certain adapter, and data set.
        switch(feedType){
            case "feed_post":
                List<FeedPost> feedPosts = FeedPostCollection.get(getContext()).getFeedPosts(where, null);
                if(mAdapter == null) {
                    mAdapter = new FeedPostAdapter(feedPosts);
                    mFeed.setAdapter(mAdapter);
                }
                else {
                    ((FeedPostAdapter)mAdapter).setFeedPosts(feedPosts);
                }
                break;
            case "favorite":
                List<User> favorites = UserCollection.get(getContext()).getFavorites(baseUser);
                if(mAdapter == null) {
                    mAdapter = new UserAdapter(favorites);
                    mFeed.setAdapter(mAdapter);
                }
                else {
                    ((UserAdapter)mAdapter).setFavorites(favorites);
                }
                break;
            case "favorites_feed":
                List<FeedPost> favoritesPosts = FeedPostCollection.get(getContext()).getFavoritesPosts(baseUser);
                if(mAdapter == null) {
                    mAdapter = new FeedPostAdapter(favoritesPosts);
                    mFeed.setAdapter(mAdapter);
                }
                else {
                    ((FeedPostAdapter)mAdapter).setFeedPosts(favoritesPosts);
                }
                break;
            case "all_users":
                List<User> allUsers = UserCollection.get(getContext()).getUsers();
                if(mAdapter == null) {
                    mAdapter = new UserAdapter(allUsers);
                    mFeed.setAdapter(mAdapter);
                }
                else {
                    ((UserAdapter)mAdapter).setFavorites(allUsers);
                }
                break;
        }

    }


}
