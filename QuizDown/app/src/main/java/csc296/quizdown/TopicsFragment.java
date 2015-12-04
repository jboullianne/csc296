package csc296.quizdown;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopicsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TopicsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static String KEY_TOPIC = "csc296.QuizDown.TOPIC_TITLE";
    public static String KEY_ICON = "csc296.QuizDown.TOPIC_ICON";

    private GridLayout mTopicGrid;

    public TopicsFragment() {
        // Required empty public constructor
    }

    public static TopicsFragment newInstance() {
        TopicsFragment topicsFragment = new TopicsFragment();
        return topicsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_topics, container, false);
        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        mTopicGrid = (GridLayout) rootView.findViewById(R.id.gridlayout_topics);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Topics");
        query.whereExists("Title");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                   for(ParseObject topic : scoreList) {
                        Log.d("TopicsFragment", "Displaying Topics");
                       //Icon of Topic
                       final ImageButton topicButton = new ImageButton(getContext());

                       try{
                           byte[] tmp = topic.getParseFile("Icon").getData();
                           final Bitmap icon = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
                           topicButton.setImageBitmap(icon);
                           topicButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

                           topicButton.setBackgroundColor(Color.TRANSPARENT);
                           topicButton.setLayoutParams(new LinearLayout.LayoutParams(280, 280));

                           mTopicGrid.addView(topicButton);
                           GridLayout.LayoutParams params = (GridLayout.LayoutParams) topicButton.getLayoutParams();
                           params.setMarginEnd(15);
                           topicButton.setLayoutParams(params);

                           final String title = topic.getString("Title");

                           topicButton.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                    Log.d("TopicButton", "Topic Button Pressed!");
                                   topicButton.setTransitionName(getString(R.string.transition_name_topic));

                                   Intent intent = new Intent(getActivity(), TopicActivity.class);
                                   intent.putExtra(KEY_TOPIC, title);

                                   ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                           // the context of the activity
                                           getActivity(),

                                           // For each shared element, add to this method a new Pair item,
                                           // which contains the reference of the view we are transitioning *from*,
                                           // and the value of the transitionName attribute
                                           new Pair<View, String>(topicButton,
                                                   getString(R.string.transition_name_topic))
                                   );
                                   ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                               }
                           });
                       }catch(ParseException exp){
                           exp.printStackTrace();
                       }



                   }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
