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
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.app.ToolbarActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_ACTIVITY";

    private ImageView mIcon;
    private TextView mTitle;
    private Button mPlay;
    private LinearLayout mLoginLayout;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Button mLoginCancel;
    private Button mSignUp;
    private LinearLayout mActionLayout;
    private LinearLayout mSignupLayout;
    private Button mSignUpCommit;
    private EditText mSignupUsername;
    private EditText mSignupPassword;
    private EditText mSignupEmail;
    private Button mSignupCancel;
    private AssetManager mAssets;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIcon = (ImageView) findViewById(R.id.logo_icon);
        mTitle = (TextView) findViewById(R.id.logo_title);
        mPlay = (Button) findViewById(R.id.login_play);
        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLogin = (Button) findViewById(R.id.login_login);
        mSignUp = (Button) findViewById(R.id.login_signup);
        mActionLayout = (LinearLayout) findViewById(R.id.login_action_layout);
        mSignupLayout = (LinearLayout) findViewById(R.id.signup_layout);
        mSignUpCommit = (Button) findViewById(R.id.signup_signup);
        mSignupCancel = (Button) findViewById(R.id.signup_cancel);
        mSignupUsername = (EditText) findViewById(R.id.signup_username);
        mSignupPassword = (EditText) findViewById(R.id.signup_password);
        mSignupEmail = (EditText) findViewById(R.id.signup_email);
        mLoginCancel = (Button) findViewById(R.id.login_cancel);
        mAssets = getApplicationContext().getAssets();

        //Starts the music for the application
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer().create(getApplicationContext(), R.raw.login_music);
            mMediaPlayer.setVolume(1, 1);
            mMediaPlayer.start();
        }


        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet spinAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.spin);
                spinAnim.setTarget(mIcon);
                spinAnim.start();
            }
        });


        //Loads the sign in prompt when the user presses Login
        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimatorSet playButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                playButtonAnim.setTarget(mPlay);

                AnimatorSet signUpButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                signUpButtonAnim.setTarget(mSignUp);

                AnimatorSet actionAnim = new AnimatorSet();
                actionAnim.playTogether(playButtonAnim,signUpButtonAnim);


                mLoginLayout.setVisibility(View.VISIBLE);
                AnimatorSet loginFieldAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                loginFieldAnim.setTarget(mLoginLayout);

                AnimatorSet loginAnim = new AnimatorSet();
                loginAnim.playSequentially(actionAnim, loginFieldAnim);
                loginAnim.start();
            }
        });

        //Goes back to main menu when user presses cancel
        mLoginCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet signupAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                signupAnim.setTarget(mLoginLayout);

                mPlay.setVisibility(View.VISIBLE);
                mSignUp.setVisibility(View.VISIBLE);

                AnimatorSet playButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                playButtonAnim.setTarget(mPlay);

                AnimatorSet signUpButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                signUpButtonAnim.setTarget(mSignUp);

                AnimatorSet actionAnim = new AnimatorSet();
                actionAnim.playTogether(playButtonAnim, signUpButtonAnim);

                AnimatorSet loginAnim = new AnimatorSet();
                loginAnim.playSequentially(signupAnim, actionAnim);
                loginAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                loginAnim.start();
            }
        });

        //Loads the signup menu prompt
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet playButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                playButtonAnim.setTarget(mPlay);

                AnimatorSet signUpButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                signUpButtonAnim.setTarget(mSignUp);

                AnimatorSet actionAnim = new AnimatorSet();
                actionAnim.playTogether(playButtonAnim,signUpButtonAnim);


                mSignupLayout.setVisibility(View.VISIBLE);
                AnimatorSet loginFieldAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                loginFieldAnim.setTarget(mSignupLayout);

                AnimatorSet loginAnim = new AnimatorSet();
                loginAnim.playSequentially(actionAnim, loginFieldAnim);
                loginAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSignUp.setVisibility(View.GONE);
                        mPlay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                loginAnim.start();
            }
        });

        //Tries to signup the user with a new account
        mSignUpCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySignup(mSignupUsername.getText().toString(),
                        mSignupPassword.getText().toString(),
                        mSignupEmail.getText().toString());
            }
        });

        //takes the user back to main menu
        mSignupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet signupAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_out_fast);
                signupAnim.setTarget(mSignupLayout);

                mPlay.setVisibility(View.VISIBLE);
                mSignUp.setVisibility(View.VISIBLE);

                AnimatorSet playButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                playButtonAnim.setTarget(mPlay);

                AnimatorSet signUpButtonAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.fade_in_fast);
                signUpButtonAnim.setTarget(mSignUp);

                AnimatorSet actionAnim = new AnimatorSet();
                actionAnim.playTogether(playButtonAnim, signUpButtonAnim);

                AnimatorSet loginAnim = new AnimatorSet();
                loginAnim.playSequentially(signupAnim, actionAnim);
                loginAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSignupLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                loginAnim.start();

            }
        });


        //tries to login the user
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });

        //Hides action bar for fullscreen activity
        getSupportActionBar().hide();

        AnimatorSet iconAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        iconAnim.setTarget(mIcon);

        AnimatorSet spinAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.spin);
        spinAnim.setTarget(mIcon);

        AnimatorSet titleAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        titleAnim.setTarget(mTitle);

        AnimatorSet playAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        playAnim.setTarget(mActionLayout);

        AnimatorSet collab = new AnimatorSet();
        collab.play(spinAnim).with(titleAnim);

        //Starts the whole login screen animation system
        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.playSequentially(iconAnim, collab, playAnim);
        logoAnim.setStartDelay(1000);
        logoAnim.start();
    }

    //Contacts the Parse Database to see if user logged in with correct credentials
    public void tryLogin(String username, String password){

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e == null){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //tries to create a new user in the Parse Database
    public void trySignup(String username, String password, String email){

        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("total_games", 0);
        user.put("best_topic", "~");
        user.put("flair_description", "");
        user.put("highest_score", 0);
        user.put("highest_level", 1);
        user.put("game_history", new JSONArray());

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("data_manager");
        query.whereExists("num_users");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null) {
                    int numUsers = list.get(0).getInt("num_users");
                    user.put("overall_rank", numUsers + 1);
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {


                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                Log.d(TAG, e.getMessage());
                                String message = "";
                                if (e.getMessage().equalsIgnoreCase("java.lang.IllegalArgumentException: Username cannot be missing or blank"))
                                    message += "Username cannot be blank";
                                if (e.getMessage().equalsIgnoreCase("java.lang.IllegalArgumentException: Password cannot be missing or blank"))
                                    message += "Password cannot be blank";
                                if (e.getMessage().contains("username") && e.getMessage().contains("already taken"))
                                    message += "Username already taken";
                                if (e.getMessage().contains("has already been taken"))
                                    message += "Email address has already been taken";

                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    ParseObject object = list.get(0);
                    object.put("num_users", numUsers + 1);
                    object.saveInBackground();
                }
                else{
                    Log.d(TAG, e.getMessage());
                }
            }
        });


    }
}
