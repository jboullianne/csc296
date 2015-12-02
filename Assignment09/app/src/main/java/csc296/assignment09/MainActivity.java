package csc296.assignment09;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SoundListFragment.DetailItemClickListener{

    private static final String TAG = "MainActivity";
    private SoundListFragment soundListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);


        //Checks to see if there is already an instance of the SoundListFragment
        soundListFragment = (SoundListFragment) getSupportFragmentManager().findFragmentById(R.id.master_frame);

        //If no SoundListFragment has been created, then it creates a new one
        if(soundListFragment == null) {
            Log.d(TAG, "Creating new SoundList, with SoundSystem");
            soundListFragment = new SoundListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.master_frame, soundListFragment).commit();
        }
    }

    public void OnDetailItemClicked(Sound sound){
        Log.d(TAG, sound.getName() + " Clicked.");

        //If device is not using master_detail layout  currently
        if(findViewById(R.id.detail_frame) == null){
            Log.d(TAG, "PHONE LAYOUT IN USE");
            Toast.makeText(this, "Playing " + sound.getName() + "!", Toast.LENGTH_SHORT).show();
            startActivity(DetailActivity.newInstance(this, sound));
        }else{ //If device is utilizing the master detail layout
            Log.d(TAG, "TABLET LAYOUT IN USE");
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_frame, SoundDetailFragment.newFragment(sound.getName())).commit();
        }
    }
}
