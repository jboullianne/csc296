/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import csc296.quizdown.model.DataManager;
import csc296.quizdown.model.Question;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    public static final String KEY_DATA_CLASS = "csc296.quizdown.KEY_DATA_CLASS";

    private static final int ROUND_DELAY = 1500;

    private String topicName;
    private String statsClass;
    private String questionClass;
    private int tNumGames;
    private ImageView mIcon;
    private ImageView mUserIcon;
    private TextView mTopic;
    private LinearLayout mGameLayout;
    private RelativeLayout mIntroLayout;
    private TextView mTimer;
    private TextView mRound;
    private TextView mScore;
    private Button mTimeBar;

    private TextView mQuestion;
    private Button mAnswer1;
    private Button mAnswer2;
    private Button mAnswer3;
    private Button mAnswer4;

    private Question mCurrentQuestion;
    private DataManager dataManager;
    private SoundPool mSoundPool;
    private AssetManager mAssets;
    private int soundId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);



        mIcon = (ImageView) findViewById(R.id.topic_icon);
        mTopic = (TextView) findViewById(R.id.gameview_topic_title);
        mUserIcon = (ImageView) findViewById(R.id.gameview_user_icon);
        mGameLayout = (LinearLayout) findViewById(R.id.gameview_layout);
        mIntroLayout = (RelativeLayout) findViewById(R.id.gameview_intro_layout);
        mTimer = (TextView) findViewById(R.id.gameview_timer);
        mRound = (TextView) findViewById(R.id.gameview_round_tag);
        mScore = (TextView) findViewById(R.id.gameview_score);
        mTimeBar = (Button) findViewById(R.id.gameview_time_bar);

        mQuestion = (TextView) findViewById(R.id.gameview_question);
        mAnswer1 = (Button) findViewById(R.id.gameview_answer1);
        mAnswer2 = (Button) findViewById(R.id.gameview_answer2);
        mAnswer3 = (Button) findViewById(R.id.gameview_answer3);
        mAnswer4 = (Button) findViewById(R.id.gameview_answer4);

        //Starts game round sounds
        String soundNames[];
        mAssets = getApplicationContext().getAssets();
        mSoundPool = new SoundPool.Builder().build();
        try {
            soundNames = mAssets.list("sounds");
            for(String filename : soundNames) {
                String path = "sounds" + "/" + filename;
                Log.d(TAG, filename);

                try {
                    AssetFileDescriptor afd = mAssets.openFd(path);
                    soundId = mSoundPool.load(afd, 1);
                }
                catch(IOException e) {
                    Log.e(TAG, "could not load sound from file: " + path, e);
                }
            }
        }
        catch(IOException ioe) {
            Log.e(TAG, "could not load sound files.", ioe);
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);



        //Gets topic info
        Intent intent = getIntent();
        if(intent != null){

            topicName = intent.getStringExtra(TopicActivity.KEY_TOPIC);
            mTopic.setText(topicName);
            getSupportActionBar().hide(); // Hides the Action bar for a fullscreen game activity
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Topics");
            //gets topic data such as where the questions are stored, topic icon, user data, etc...
            query.whereEqualTo("Title", topicName);
            try{
                ParseObject topic = query.find().get(0);

                statsClass = topic.getString("data_source");
                questionClass = topic.getString("question_source");
                byte[] tmp = topic.getParseFile("Icon").getData();
                mIcon.setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));

                //Displays the user's icon
                ParseFile photo = ParseUser.getCurrentUser().getParseFile("profile_photo");
                if (photo == null) {
                    mUserIcon.setBackgroundColor(Color.TRANSPARENT);
                    mUserIcon.setImageResource(R.mipmap.ic_launcher2);
                } else {
                    Log.d(TAG, "profile photo found");
                    byte[] tmp2 = photo.getData();
                    mUserIcon.setBackground(new BitmapDrawable(BitmapFactory.decodeByteArray(tmp2, 0, tmp2.length)));
                    mUserIcon.setImageResource(R.drawable.user_icon);
                }
            }catch (ParseException e){

            }
        }

        //Controls the games data, and manages what happens in the game. What questions are displayed, answers, score, xp, time...
        dataManager = new DataManager(topicName, statsClass, questionClass);
        dataManager.generateQuestions();
        startNewRound(dataManager.getCurrentRound(), ROUND_DELAY);

        mAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = mCurrentQuestion.isCorrectAnswer(1);

                if (correct) {
                    int timeLeft = dataManager.getTimeLeft();
                    dataManager.setCurrentScore(dataManager.getCurrentScore() + timeLeft);
                    mScore.setText(String.valueOf(dataManager.getCurrentScore()));
                    Log.d(TAG, "Correct Answer!");
                } else {
                    Log.d(TAG, "Incorrect Answer!");
                }
                setCorrectAnswers(1);
                dataManager.setTimeLeft(0);
            }
        });

        mAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = mCurrentQuestion.isCorrectAnswer(2);

                if (correct) {
                    int timeLeft = dataManager.getTimeLeft();
                    int oldScore = dataManager.getCurrentScore();
                    dataManager.setCurrentScore(dataManager.getCurrentScore() + timeLeft);
                    mScore.setText(String.valueOf(dataManager.getCurrentScore()));
                    Log.d(TAG, "Correct Answer!");
                } else {
                    Log.d(TAG, "Incorrect Answer!");
                }
                setCorrectAnswers(2);
                dataManager.setTimeLeft(0);
            }
        });

        mAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = mCurrentQuestion.isCorrectAnswer(3);

                if (correct) {
                    int timeLeft = dataManager.getTimeLeft();
                    int oldScore = dataManager.getCurrentScore();
                    dataManager.setCurrentScore(dataManager.getCurrentScore() + timeLeft);
                    mScore.setText(String.valueOf(dataManager.getCurrentScore()));

                    Log.d(TAG, "Correct Answer!");
                } else {
                    Log.d(TAG, "Incorrect Answer!");
                }
                setCorrectAnswers(3);
                dataManager.setTimeLeft(0);
            }
        });

        mAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = mCurrentQuestion.isCorrectAnswer(4);

                if (correct) {
                    int timeLeft = dataManager.getTimeLeft();
                    int oldScore = dataManager.getCurrentScore();
                    dataManager.setCurrentScore(dataManager.getCurrentScore() + timeLeft);
                    mScore.setText(String.valueOf(dataManager.getCurrentScore()));

                    Log.d(TAG, "Correct Answer!");
                } else {
                    Log.d(TAG, "Incorrect Answer!");
                }
                setCorrectAnswers(4);
                dataManager.setTimeLeft(0);
            }
        });


    }

    /*
    handles all animations for the round (Displaying the answers, question, time left, and finishing the round)
     */
    public void startNewRound(int round, int delay) {



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mTimeBar.setWidth(size.x);

        mRound.setText("ROUND " + round);
        resetAnswers();

        AnimatorSet finalAnim = new AnimatorSet();

        AnimatorSet roundAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in_fast);
        roundAnim.setTarget(mIntroLayout);

        AnimatorSet introAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_out_fast);
        introAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                while(!dataManager.areQuestionsReady()){
                    try {
                        wait(200);
                    }catch (InterruptedException e){

                    }
                }
                setupQuestion();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mGameLayout.setVisibility(View.VISIBLE);
                AnimatorSet introAnim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                introAnim2.setTarget(mGameLayout);
                introAnim2.start();

                AnimatorSet answerAnim1 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                answerAnim1.setTarget(mAnswer1);
                answerAnim1.setDuration(200);

                AnimatorSet answerAnim2 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                answerAnim2.setTarget(mAnswer2);
                answerAnim2.setDuration(200);

                AnimatorSet answerAnim3 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                answerAnim3.setTarget(mAnswer3);
                answerAnim3.setDuration(200);

                AnimatorSet answerAnim4 = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                answerAnim4.setTarget(mAnswer4);
                answerAnim4.setDuration(200);

                AnimatorSet allAnim = new AnimatorSet();
                allAnim.setStartDelay(2000);
                allAnim.playSequentially(answerAnim1, answerAnim2, answerAnim3, answerAnim4);
                allAnim.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        introAnim.setTarget(mIntroLayout);
        introAnim.setStartDelay(delay);

        finalAnim.playSequentially(roundAnim, introAnim);
        finalAnim.start();

        //Sets time in round to 20 seconds
        dataManager.setTimeLeft(20);

        /*
        Controls time and finishes when either user selects an answer or the time runs out.
         */
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run(){
                        mTimer.setText("Time Left: " + dataManager.getTimeLeft());
                        dataManager.setTimeLeft(dataManager.getTimeLeft() - 1);
                        if(dataManager.getTimeLeft() == -1){
                            timer.cancel();
                            endCurrentRound();
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 5500, 1000);
        task.run();

        //Starts all animations
        ObjectAnimator anim = ObjectAnimator.ofInt(mTimeBar, "Width", size.x, 0);
        anim.setDuration(20000);
        anim.setStartDelay(0);
        anim.start();
    }

    //Finishes round and updates all data in the datamanager
    public void endCurrentRound(){

        AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
        anim.setTarget(mGameLayout);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnswer1.setAlpha(0);
                mAnswer2.setAlpha(0);
                mAnswer3.setAlpha(0);
                mAnswer4.setAlpha(0);
                if (dataManager.getCurrentRound() < 7) {
                    dataManager.setCurrentRound(dataManager.getCurrentRound() + 1);
                    mGameLayout.setVisibility(View.GONE);
                    startNewRound(dataManager.getCurrentRound(), ROUND_DELAY);
                }else{
                    Toast.makeText(getApplicationContext(), "Game Finished!", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra(KEY_DATA_CLASS, dataManager.finishGame());
                    setResult(RESULT_OK, data);
                    finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setStartDelay(1500);
        anim.start();

    }

    //Loads question into widgets before they
    public void setupQuestion(){
        mCurrentQuestion = dataManager.getQuestions().get((int) (Math.random() * dataManager.getQuestions().size()));
        Log.d(TAG, "Question is: " + mCurrentQuestion.toString());
        mQuestion.setText(mCurrentQuestion.getQuestion());
        mAnswer1.setText((mCurrentQuestion.getAnswer1()));
        mAnswer2.setText((mCurrentQuestion.getAnswer2()));
        mAnswer3.setText((mCurrentQuestion.getAnswer3()));
        mAnswer4.setText((mCurrentQuestion.getAnswer4()));
    }

    public void setAnswerButtons(){
        int correctAnswer = mCurrentQuestion.getCorrectAnswer();
    }

    //Sets the one answer so when it's clicked it will turn green
    public void setCorrectAnswers(int choice){
        switch(mCurrentQuestion.getCorrectAnswer()){
            case 1:
                mAnswer1.setBackgroundResource(R.drawable.correct_answer);
                break;
            case 2:
                mAnswer2.setBackgroundResource(R.drawable.correct_answer);
                break;
            case 3:
                mAnswer3.setBackgroundResource(R.drawable.correct_answer);
                break;
            case 4:
                mAnswer4.setBackgroundResource(R.drawable.correct_answer);
                break;
        }

        if(!mCurrentQuestion.isCorrectAnswer(choice)){
            switch (choice){
                case 1:
                    mAnswer1.setBackgroundResource(R.drawable.wrong_answer);
                    break;
                case 2:
                    mAnswer2.setBackgroundResource(R.drawable.wrong_answer);
                    break;
                case 3:
                    mAnswer3.setBackgroundResource(R.drawable.wrong_answer);
                    break;
                case 4:
                    mAnswer4.setBackgroundResource(R.drawable.wrong_answer);
                    break;
            }
        }

        mAnswer1.setEnabled(false);
        mAnswer2.setEnabled(false);
        mAnswer3.setEnabled(false);
        mAnswer4.setEnabled(false);

    }

    //Resets the answers for the next round
    public void resetAnswers(){
        mAnswer1.setBackgroundResource(R.drawable.play_button);
        mAnswer2.setBackgroundResource(R.drawable.play_button);
        mAnswer3.setBackgroundResource(R.drawable.play_button);
        mAnswer4.setBackgroundResource(R.drawable.play_button);

        mAnswer1.setEnabled(true);
        mAnswer2.setEnabled(true);
        mAnswer3.setEnabled(true);
        mAnswer4.setEnabled(true);
    }

    //Displays the dialog to ask if the user would actually like to quit
    @Override
    public void onBackPressed(){
        DialogFragment dialog = new QuitDialogFragment();
        dialog.show(getSupportFragmentManager(), "QuitDialog");
    }

    //Quit game and go back to topic activity
    public void quitGame(){
        super.onBackPressed();
    }

    //DialogFragment that prompts the user if they want to quit
    public static class QuitDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to quit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((GameActivity) getActivity()).quitGame();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
