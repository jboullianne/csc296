/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import csc296.quizdown.model.DataManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String TAG = "HOME_FRAGMENT";
    private static final String KEY_PROFILE_PHOTO = "PROFILE_PHOTO";

    private TextView mTotalGames;
    private TextView mPlayerSince;
    private TextView mTotalRank;
    private LineChart mChart;
    private ImageView mProfilePhoto;
    private Button mHighLevel;
    private TextView mHighScore;
    private TextView mBestTopic;
    private ProgressBar mProgressSymbol;
    private TextView mUsername;
    private TextView mUserFlair;
    private ImageButton mSettings;
    private ImageView mFlairIcon;

    private File updatedProfilePhoto;
    private Uri updatedPhotoUri;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    public static UserFragment newInstance() {
        return new UserFragment();
    }

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mTotalGames = (TextView) rootView.findViewById(R.id.textview_total_games);
        mPlayerSince = (TextView) rootView.findViewById(R.id.textview_player_since);
        mTotalRank = (TextView) rootView.findViewById(R.id.textview_total_rank);
        mChart = (LineChart) rootView.findViewById(R.id.home_stat_chart);
        mProfilePhoto = (ImageView) rootView.findViewById(R.id.user_icon);
        mHighLevel = (Button) rootView.findViewById(R.id.highest_level_icon);
        mHighScore = (TextView) rootView.findViewById(R.id.highest_score);
        mBestTopic = (TextView) rootView.findViewById(R.id.game_history_best_topic);
        mProgressSymbol = (ProgressBar) rootView.findViewById(R.id.loading_symbol);
        mUsername = (TextView) rootView.findViewById(R.id.textview_username);
        mUserFlair = (TextView) rootView.findViewById(R.id.user_flair);
        mFlairIcon = (ImageView) rootView.findViewById(R.id.user_flair_icon);
        mSettings = (ImageButton) rootView.findViewById(R.id.button_settings);

        DataManager.setCurrentUser(ParseUser.getCurrentUser());

        if(savedInstanceState != null){
            updatedPhotoUri = savedInstanceState.getParcelable(KEY_PROFILE_PHOTO);
        }

        //gets and displays all user info for the current logged in user
        Bundle args = getArguments();
        if(args != null){
            ParseQuery<ParseUser> query = new ParseUser().getQuery();
            query.whereEqualTo("username", "Jboullianne");
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if(e == null){
                        for(ParseUser user : list) {
                            setUserData(user);
                        }
                    }else{
                        Log.d(TAG, e.toString() + " : POOOP");
                    }
                }
            });
        }else{
            setUserData(ParseUser.getCurrentUser());
        }

        //Allows the user to take a profile picture
        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                //make a random filename
                String filename = "IMG_" + UUID.randomUUID().toString() + ".jpg";
                //make a file in the external photos directory
                File picturesDir =
                        getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                updatedProfilePhoto = new File(picturesDir, filename);

                updatedPhotoUri = Uri.fromFile(updatedProfilePhoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, updatedPhotoUri);

                Log.d(TAG, "photo location: " + updatedProfilePhoto.toString());

                startActivityForResult(intent, 0);
            }
        });



        return rootView;
    }

    //Changes the profile photo when the user is done taking a photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult()");
        if(resultCode == Activity.RESULT_OK) {
            mProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(updatedPhotoUri.getPath()));
            File photo = new File(updatedPhotoUri.getPath());
            ParseUser.getCurrentUser().put("profile_photo", photo);
            ParseUser.getCurrentUser().saveInBackground();
        }
    }

    //Sets user's overall game history data
    public void setChartData(JSONArray data){
        float range = 100;
        int count = 5;
        ArrayList<String> xVals = new ArrayList<String>();


        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < data.length(); i++) {

            xVals.add(i + "");
        }
        for (int i = 0; i < data.length(); i++) {
            try{
                yVals1.add(new Entry(data.getInt(i), i));
            }catch (JSONException e){

            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "Game history");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.parseColor("#6fed5b")); //Line Color
        set1.setCircleColor(Color.WHITE); //Circle Color
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);



        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData points = new LineData(xVals, dataSets);
        points.setValueTextColor(Color.GREEN);
        points.setValueTextSize(9f);


        // set data
        mChart.setData(points);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setGridBackgroundColor(Color.TRANSPARENT);
        mChart.setDescription("");
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setTextColor(Color.WHITE);
        mChart.getAxisRight().setTextColor(Color.WHITE);

        mChart.setTouchEnabled(false);
        mChart.setDescriptionColor(Color.WHITE);


    }

    //Sets the user's overall best level icon
    public void setLevelIcon(int level){
        switch (level){
            case 1:
            case 2:
            case 3:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set1);
                break;
            case 4:
            case 5:
            case 6:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set2);
                break;
            case 7:
            case 8:
            case 9:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set3);
                break;
            case 10:
            case 11:
            case 12:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set4);
                break;
            case 13:
            case 14:
            case 15:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set5);
                break;
            case 16:
            case 17:
            case 18:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set6);
                break;
            case 19:
            case 20:
            case 21:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set7);
                break;
            case 22:
            case 23:
            case 24:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set8);
                break;
            case 25:
            case 26:
            case 27:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set9);
                break;
            case 28:
            case 29:
            case 30:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set10);
                break;
            case 31:
            case 32:
            case 33:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set11);
                break;
            case 34:
            case 35:
            case 36:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set12);
                break;
            case 37:
            case 38:
            case 39:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set13);
                break;
            case 40:
                mHighLevel.setBackgroundResource(R.mipmap.rank_set14);
                break;
        }
    }

    //Sets the rest of the user's data that is displayed on the fragment
    public void setUserData(ParseUser user){
        try {
            mTotalGames.setText(String.valueOf(user.getInt("total_games")));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            mPlayerSince.setText(dateFormat.format(user.getCreatedAt()));
            mTotalRank.setText(String.valueOf(user.getInt("overall_rank")));
            mHighLevel.setText(String.valueOf(user.getInt("highest_level")));
            setLevelIcon(user.getInt("highest_level"));
            mHighScore.setText(String.valueOf(user.getInt("highest_score")));
            mBestTopic.setText(String.valueOf(user.getString("best_topic")));
            mUsername.setText(user.getUsername());
            mUserFlair.setText(String.valueOf(user.getString("flair_description")));
            setUserFlairIcon(String.valueOf(user.getString("flair_description")));

            JSONArray data = user.getJSONArray("game_history");
            setChartData(data);
            ParseFile photo = user.getParseFile("profile_photo");
            if(photo == null){
                mProfilePhoto.setImageResource(R.mipmap.ic_launcher2);
            }else{
                Log.d(TAG, "profile photo found");
                byte[] tmp = photo.getData();
                mProfilePhoto.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(tmp, 0, tmp.length)));
            }
        }catch (ParseException exp2) {
            Log.d(TAG, exp2.toString() + " : EXCEPTION");
        }
        mProgressSymbol.setVisibility(View.GONE);
    }

    public void setUserFlairIcon(String icon){
        Log.d(TAG, "FlairResourceTAG: " + icon);
        switch(icon){
            case "Developer":
                mFlairIcon.setImageResource(R.mipmap.medal_icon);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(KEY_PROFILE_PHOTO, updatedPhotoUri);
        super.onSaveInstanceState(savedInstanceState);
    }
}
