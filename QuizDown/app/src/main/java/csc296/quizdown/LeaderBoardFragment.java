/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class LeaderBoardFragment extends Fragment {

    private static final String TAG = "LEADERBOARD_FRAGMENT";

    private RecyclerView mSoundView;
    private ProgressBar mLoadingBar;
    private UserItemClickListener mListener;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    public static LeaderBoardFragment newInstance(String filter){
        return new LeaderBoardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_leaderboard, container, false);
        mLoadingBar = (ProgressBar) view.findViewById(R.id.leaderboard_loading_bar);
        mSoundView = (RecyclerView) view.findViewById(R.id.recycler_view_leaderboard);
        mSoundView.setLayoutManager(new LinearLayoutManager(getContext()));

        //gets all of the Leader board data
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereExists("overall_rank");
        query.addDescendingOrder("overall_experience");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e == null)
                    mSoundView.setAdapter(new LeaderBoardAdapter(list));

                //When the leaderboard is done loading, hide the Loading Bar
                mLoadingBar.setVisibility(View.GONE);
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mListener = (UserItemClickListener) context;
        }catch(ClassCastException e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mListener = (UserItemClickListener) activity;
        }catch(ClassCastException e){
            Log.e(TAG, e.toString());
        }
    }

    public interface UserItemClickListener{
        public void onUserItemClicked(ParseUser user);
    }


    private class LeaderBoardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mUserIcon;
        private TextView mUsername;
        private ImageView mFlairIcon;
        private TextView mRank;
        private ParseUser mUser;

        public LeaderBoardHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.leaderboard_item, container, false));

            mUserIcon = (ImageView) itemView.findViewById(R.id.leaderboard_user_icon);
            mUsername = (TextView)itemView.findViewById(R.id.leaderboard_username);
            mFlairIcon = (ImageView) itemView.findViewById(R.id.leaderboard_flair_icon);
            mRank = (TextView)itemView.findViewById(R.id.leaderboard_rank);

            itemView.setOnClickListener(this);
            itemView.setFocusable(true);
        }

        public void bind(ParseUser user) {
            mUser = user;
            try {
                ParseFile photo = user.getParseFile("profile_photo");
                if (photo == null) {
                    mUserIcon.setBackgroundColor(Color.TRANSPARENT);
                    mUserIcon.setImageResource(R.mipmap.ic_launcher2);
                } else {
                    Log.d(TAG, "profile photo found");
                    byte[] tmp = photo.getData();
                    mUserIcon.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(tmp, 0, tmp.length)));
                    mUserIcon.setImageResource(R.drawable.user_icon);
                }
                mUsername.setText(user.getUsername());
                mFlairIcon.setImageResource(getUserFlairBitmap());
                mRank.setText(String.valueOf("XP: " + getUserRank()));
            }catch(ParseException e){

            }
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "User Clicked");
        }

        public int getUserFlairBitmap(){
            String flairTag = mUser.getString("flair_description");
            switch (flairTag) {
                case "Developer":
                    return R.mipmap.medal_icon;
            }

            return R.drawable.empty;
        }

        public int getUserRank(){
            return mUser.getInt("overall_experience");
        }
    }

    private class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardHolder> {
        private List<ParseUser> mUsers;

        public LeaderBoardAdapter(List<ParseUser> users) {
            mUsers = users;
        }

        @Override
        public LeaderBoardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new LeaderBoardHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(LeaderBoardHolder holder, int position) {
            holder.bind(mUsers.get(position));
        }

        @Override
        public int getItemCount() {
            return mUsers.size();
        }
    }
}
