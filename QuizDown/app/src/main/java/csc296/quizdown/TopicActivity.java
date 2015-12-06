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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    private static final String TAG = "TopicActivity";
    public static String KEY_TOPIC = "csc296.QuizDown.TOPIC_TITLE";

    private ImageView mIcon;
    private TextView mTotalGames;
    private TextView mTotalWins;
    private TextView mTotalLosses;
    private LineChart mChart;
    private JSONArray mGameHistory;
    private Button mPlay;
    private Button mLeaderboard;

    private String statsClass;
    private String topicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mIcon = (ImageView) findViewById(R.id.topic_icon);
        mChart = (LineChart) findViewById(R.id.topic_stat_chart);
        mTotalGames = (TextView) findViewById(R.id.textview_total_games);
        mTotalWins = (TextView) findViewById(R.id.textview_total_wins);
        mTotalLosses = (TextView) findViewById(R.id.textview_total_losses);

        mPlay = (Button) findViewById(R.id.button_topic_play);
        mLeaderboard = (Button) findViewById(R.id.button_topic_leaderboard);

        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        Log.d("TopicActivity", "Started the Topic Activity");
        if(intent != null) {

            topicName = intent.getStringExtra(TopicsFragment.KEY_TOPIC);
            actionBar.setTitle(topicName);
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Topics");
            query.whereEqualTo("Title", topicName);
            try{
                ParseObject topic = query.find().get(0);
                statsClass = topic.getString("data_source");
                byte[] tmp = topic.getParseFile("Icon").getData();
                mIcon.setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));
            }catch (ParseException e){

            }

            ParseQuery<ParseObject> stats = new ParseQuery<ParseObject>(statsClass);
            stats.whereEqualTo("username", DataManager.getCurrentUser().getUsername());
            stats.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(e == null){
                        if(list.isEmpty()) {
                            Log.d(TAG, "Did Not Find User Data For This Topic");
                            //Add new row for this user in the database
                            mChart.setNoDataText("No Game History To Display");

                            ParseObject data = new ParseObject(statsClass);
                            data.put("username", DataManager.getCurrentUser().getUsername());
                            List tmp = new LinkedList();
                            tmp.add(0);
                            tmp.add(0);
                            tmp.add(0);
                            JSONArray record = new JSONArray(tmp);
                            data.put("stats", record);
                            List tmp2 = new LinkedList();
                            JSONArray history = new JSONArray(tmp2);
                            mGameHistory = history;
                            data.put("game_history", history);
                            data.put("level", 1);
                            data.put("experience", 0);
                            data.saveInBackground();

                        }
                        else {
                            Log.d(TAG, "Found User Data For This Topic");
                            try {
                                JSONArray record = list.get(0).getJSONArray("stats");
                                mTotalGames.setText(String.valueOf(record.get(0)));
                                mTotalWins.setText(String.valueOf(record.get(1)));
                                mTotalLosses.setText(String.valueOf(record.get(2)));

                                mGameHistory = list.get(0).getJSONArray("game_history");


                            }catch (JSONException exp) {

                            }
                        }
                        setChartData();
                    }else{
                        //If there was an error with the query
                    }
                }
            });
        }

        mChart.setTouchEnabled(false);
        mChart.setDescriptionColor(Color.WHITE);

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
                ActivityCompat.startActivity(TopicActivity.this, intent, options.toBundle());
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

    public void setChartData(){
        float range = 100;
        int count = 5;
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


}
