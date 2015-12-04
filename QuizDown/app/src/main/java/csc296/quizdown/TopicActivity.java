package csc296.quizdown;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class TopicActivity extends AppCompatActivity {

    private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mIcon = (ImageView) findViewById(R.id.topic_icon);

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();

        Log.d("TopicActivity", "Strted the Topic Activity");
        if(intent != null) {

            String title = intent.getStringExtra(TopicsFragment.KEY_TOPIC);
            actionBar.setTitle(title);
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Topics");
            query.whereEqualTo("Title", title);
            try{
                ParseObject topic = query.find().get(0);
                byte[] tmp = topic.getParseFile("Icon").getData();
                mIcon.setImageBitmap(BitmapFactory.decodeByteArray(tmp, 0, tmp.length));
            }catch (ParseException e){

            }
        }
    }
}
