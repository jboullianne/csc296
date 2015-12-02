/*
Jean-Marc Boullianne
CSC 296: Assignment08
Fall 2015
 */

package csc296.assignment08;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class VideoGameRecyclerActivity extends AppCompatActivity {

    private static final String TAG = "VG_RECYCLER_ACTIVITY";
    private static final int RC_ADD_VIDEO_GAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_game_recycler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Video Game List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Adds recyclerView Fragment to activity
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.recycler_view_frame, new VideoGameRecyclerFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video_game_recycler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.ic_new_video_game){
            Log.d(TAG, "New Video Game Icon Pressed");
            Intent intent = new Intent(this, AddVideoGameActivity.class);
            startActivityForResult(intent, RC_ADD_VIDEO_GAME);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RC_ADD_VIDEO_GAME && resultCode == RESULT_OK) {
            Log.d(TAG, "New Video Game Added");

        }
    }

}
