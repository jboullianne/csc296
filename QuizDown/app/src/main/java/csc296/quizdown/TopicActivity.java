/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import csc296.quizdown.model.DataManager;

public class TopicActivity extends AppCompatActivity {

    private static final String TAG = "TOPIC_ACTIVITY";
    public static String KEY_TOPIC = "csc296.QuizDown.TOPIC_TITLE";
    private static int RC_TOPIC = 1;

    private ImageView mIcon;
    private TextView mTotalGames;
    private TextView mRank;
    private TextView mLevel;
    private ImageView mLevelIcon;
    private LineChart mChart;
    private JSONArray mGameHistory;
    private Button mPlay;

    private String statsClass;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mIcon = (ImageView) findViewById(R.id.topic_icon);
        mChart = (LineChart) findViewById(R.id.topic_stat_chart);
        mTotalGames = (TextView) findViewById(R.id.textview_total_games);
        mRank = (TextView) findViewById(R.id.topic_rank);
        mLevel = (TextView) findViewById(R.id.topic_level_text);
        mLevelIcon = (ImageView) findViewById(R.id.topic_level_icon);

        mPlay = (Button) findViewById(R.id.button_topic_play);


        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        Log.d("TopicActivity", "Started the Topic Activity");
        //gets topic data and populates the activity with the data
        if(intent != null) {

            topicName = intent.getStringExtra(TopicsFragment.KEY_TOPIC);
            actionBar.setTitle(topicName);
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Topics");
            query.whereEqualTo("Title", topicName);
            try{
                final ParseObject topic = query.find().get(0);
                statsClass = topic.getString("data_source");
                byte[] tmp = topic.getParseFile("Icon").getData();
                mIcon.setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));

                //gets the user stats for this specific topic
                ParseQuery<ParseObject> stats = new ParseQuery<ParseObject>(statsClass);
                stats.whereEqualTo("username", DataManager.getCurrentUser().getUsername());
                stats.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            if (list.isEmpty()) {
                                Log.d(TAG, "Did Not Find User Data For This Topic");
                                //Add new row for this user in the database
                                mChart.setNoDataText("No Game History To Display");

                                ParseObject data = new ParseObject(statsClass);
                                data.put("username", DataManager.getCurrentUser().getUsername());
                                List tmp2 = new LinkedList();
                                tmp2.add(0);
                                JSONArray history = new JSONArray(tmp2);
                                mGameHistory = history;
                                data.put("game_history", history);
                                data.put("level", 1);
                                data.put("experience", 0);
                                data.put("total_games", 0);

                                int topicRank = topic.getInt("num_users");
                                mRank.setText(String.valueOf(topicRank + 1));

                                data.put("topic_rank", topicRank + 1);
                                data.saveInBackground();

                                topic.put("num_users", topicRank + 1);
                                topic.saveInBackground();

                            } else {
                                Log.d(TAG, "Found User Data For This Topic");
                                mTotalGames.setText(String.valueOf(list.get(0).getInt("total_games")));
                                mRank.setText(String.valueOf(list.get(0).getInt("topic_rank")));
                                mLevel.setText(String.valueOf(list.get(0).getInt("level")));
                                setLevelIcon(list.get(0).getInt("level"));
                                mGameHistory = list.get(0).getJSONArray("game_history");
                                Log.d(TAG, mGameHistory.toString());
                            }
                            setChartData();
                        } else {
                            //If there was an error with the query
                        }
                    }
                });
            }catch (ParseException e){

            }


        }

        //Disables touch response on the graph
        mChart.setTouchEnabled(false);
        mChart.setDescriptionColor(Color.WHITE);

        //starts a new game in this topic
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra(KEY_TOPIC, topicName);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        TopicActivity.this,
                        new Pair<View, String>(mIcon,
                                getString(R.string.transition_name_topic))
                );
                ActivityCompat.startActivityForResult(TopicActivity.this, intent, RC_TOPIC,options.toBundle());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case android.R.id.home:
                super.onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }

    //Updates the topic activity after the game has finished
    @Override
    public void onActivityResult(int requestCode, int resultcode, Intent data){
        if(resultcode == RESULT_OK){
            try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(data.getStringExtra(GameActivity.KEY_DATA_CLASS));
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                ParseObject topicData = query.getFirst();
                mTotalGames.setText(String.valueOf(topicData.getInt("total_games")));
                mRank.setText(String.valueOf(topicData.getInt("topic_rank")));
                mLevel.setText(String.valueOf(topicData.getInt("level")));
                setLevelIcon(topicData.getInt("level"));
                mGameHistory = topicData.getJSONArray("game_history");
                setChartData();
            }catch (ParseException e){

            }
        }else{

        }
    }
    public void setChartData(){

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < mGameHistory.length(); i++) {

            xVals.add(i + "");
        }
        for (int i = 0; i < mGameHistory.length(); i++) {
            try{
                //yVals1.add((Integer.parseInt(String.valueOf(mGameHistory.get(i)))) + "");
                yVals1.add(new Entry(mGameHistory.getInt(i), i));
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
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.GREEN);
        data.setValueTextSize(9f);


        // set data
        mChart.setData(data);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setGridBackgroundColor(Color.TRANSPARENT);
        mChart.setDescription("");
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getAxisLeft().setTextColor(Color.WHITE);
        mChart.getAxisRight().setTextColor(Color.WHITE);


    }

    public void setLevelIcon(int level){
        switch (level){
            case 1:
            case 2:
            case 3:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set1);
                break;
            case 4:
            case 5:
            case 6:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set2);
                break;
            case 7:
            case 8:
            case 9:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set3);
                break;
            case 10:
            case 11:
            case 12:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set4);
                break;
            case 13:
            case 14:
            case 15:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set5);
                break;
            case 16:
            case 17:
            case 18:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set6);
                break;
            case 19:
            case 20:
            case 21:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set7);
                break;
            case 22:
            case 23:
            case 24:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set8);
                break;
            case 25:
            case 26:
            case 27:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set9);
                break;
            case 28:
            case 29:
            case 30:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set10);
                break;
            case 31:
            case 32:
            case 33:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set11);
                break;
            case 34:
            case 35:
            case 36:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set12);
                break;
            case 37:
            case 38:
            case 39:
                mLevelIcon.setBackgroundResource(R.mipmap.rank_set13);
                break;
            case 40:

        }
    }


}
