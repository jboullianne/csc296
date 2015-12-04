package csc296.quizdown;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.app.ToolbarActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ImageView mIcon;
    private TextView mTitle;
    private Button mPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIcon = (ImageView) findViewById(R.id.logo_icon);
        mTitle = (TextView) findViewById(R.id.logo_title);
        mPlay = (Button) findViewById(R.id.button_play);


        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet spinAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.spin);
                spinAnim.setTarget(mIcon);
                spinAnim.start();
            }
        });


        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

        AnimatorSet iconAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        iconAnim.setTarget(mIcon);

        AnimatorSet spinAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.spin);
        spinAnim.setTarget(mIcon);

        AnimatorSet titleAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        titleAnim.setTarget(mTitle);

        AnimatorSet playAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.fade_in);
        playAnim.setTarget(mPlay);

        AnimatorSet collab = new AnimatorSet();
        collab.play(spinAnim).with(titleAnim);

        AnimatorSet logoAnim = new AnimatorSet();
        logoAnim.playSequentially(iconAnim, collab, playAnim);
        logoAnim.setStartDelay(1000);
        logoAnim.start();
    }
}
