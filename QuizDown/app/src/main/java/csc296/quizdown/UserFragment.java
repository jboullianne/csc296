package csc296.quizdown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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

    private TextView mTotalGames;
    private TextView mTotalWins;
    private TextView mTotalLosses;
    private LineChart mChart;
    private ImageView mProfilePhoto;
    private Button mHighLevel;

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
        mTotalWins = (TextView) rootView.findViewById(R.id.textview_total_wins);
        mTotalLosses = (TextView) rootView.findViewById(R.id.textview_total_losses);
        mChart = (LineChart) rootView.findViewById(R.id.home_stat_chart);
        mProfilePhoto = (ImageView) rootView.findViewById(R.id.user_icon);
        mHighLevel = (Button) rootView.findViewById(R.id.highest_level_icon);

        try{
            ParseUser.logIn("jboullianne@gmail.com", "password");
            Log.d(TAG, ParseUser.getCurrentUser().getUsername());
            DataManager.setCurrentUser(ParseUser.getCurrentUser());

            List<ParseUser> list = ParseUser.getQuery().whereEqualTo("username", "jboullianne@gmail.com").find();
            for(ParseUser user : list){
                JSONArray userStats = user.getJSONArray("Game_Stats");
                Log.d(TAG, String.valueOf(userStats));
                mTotalGames.setText(String.valueOf(userStats.get(0)));
                mTotalWins.setText(String.valueOf(userStats.get(1)));
                mTotalLosses.setText(String.valueOf(userStats.get(2)));

                JSONArray data = user.getJSONArray("game_history");
                setChartData(data);

                byte[] tmp = user.getParseFile("profile_photo").getData();
                mProfilePhoto.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(tmp, 0, tmp.length)));

                setLevelIcon(user.getInt("highest_level"));

            }

        }catch(ParseException e){
            e.printStackTrace();
        }catch (JSONException f){

        }

        return rootView;
    }

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

    public void setLevelIcon(int level){
        switch (level){
            case 1:
            case 2:
            case 3:
                mHighLevel.setText(String.valueOf(level));
                mHighLevel.setBackgroundResource(R.mipmap.rank_set1);
                break;
            case 4:
            case 5:
            case 6:

            case 7:
            case 8:
            case 9:

            case 10:
            case 11:
            case 12:

            case 13:
            case 14:
            case 15:

            case 16:
            case 17:
            case 18:

            case 19:
            case 20:
            case 21:

            case 22:
            case 23:
            case 24:

            case 25:
            case 26:
            case 27:

            case 28:
            case 29:
            case 30:

            case 31:
            case 32:
            case 33:

            case 34:
            case 35:
            case 36:

            case 37:
            case 38:
            case 39:

            case 40:

        }
    }
}
