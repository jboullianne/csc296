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
import project02.csc296.thesocialnetwork.model.User;

/**
 * Created by JeanMarc on 11/3/15.
 */
public class UserAdapter extends RecyclerView.Adapter<UserHolder>{

    private static final String TAG = "FEED_POST_ADAPTER";

    private List<User> mUsers; //Holds the List ofUsers

    public UserAdapter(List<User> users) {
        mUsers = users;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_user, parent, false);

        UserHolder holder = new UserHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.bind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    //Updates the Favorites List
    public void setFavorites(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }
}
