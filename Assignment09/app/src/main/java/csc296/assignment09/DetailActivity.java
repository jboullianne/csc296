package csc296.assignment09;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getIntent().getExtras() != null){
            String name = getIntent().getExtras().getString(SoundDetailFragment.KEY_SOUND_NAME);
            getSupportFragmentManager().beginTransaction().add(R.id.activity_detail_frame, SoundDetailFragment.newFragment(name)).commit();
        }
    }


    public static Intent newInstance(Context context, Sound sound){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(SoundDetailFragment.KEY_SOUND_NAME, sound.getName());

        return intent;
    }
}
