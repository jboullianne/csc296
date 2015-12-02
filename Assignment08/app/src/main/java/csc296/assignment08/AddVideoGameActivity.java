/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import csc296.assignment08.model.Collection;
import csc296.assignment08.model.VideoGame;

public class AddVideoGameActivity extends AppCompatActivity {

    private EditText mTitle;
    private EditText mPublisher;
    private EditText mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Gets references to EditText Fields
        mTitle = (EditText) findViewById(R.id.edit_text_title);
        mPublisher = (EditText) findViewById(R.id.edit_text_publisher);
        mYear = (EditText) findViewById(R.id.edit_text_year);

    }

    //If user clicks OK then adds the new Video Game to the Collection
    public void finishActivity(View view){
        if(view.getId() == R.id.add_game_ok){
            String title = mTitle.getText().toString();
            String publisher = mPublisher.getText().toString();
            int year = Integer.parseInt(mYear.getText().toString());

            VideoGame game = new VideoGame(title, publisher, year);
            Collection collection = Collection.get(getApplicationContext());
            collection.addVideoGame(game);

            setResult(RESULT_OK);
            finish();
        }
        else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }

}
