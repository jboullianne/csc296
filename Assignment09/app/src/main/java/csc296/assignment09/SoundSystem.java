package csc296.assignment09;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeanMarc on 11/19/15.
 */
public class SoundSystem {
    private static final String TAG = "SoundSystem";
    private static final String SOUNDS_FOLDER = "sounds";

    private AssetManager mAssets;
    private List<Sound> mSounds;

    private SoundPool mSoundPool;

    public SoundSystem(Context context) {
        mAssets = context.getAssets();
        mSounds = new ArrayList<>();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        String[] soundNames;
        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            for(String filename : soundNames) {
                String path = SOUNDS_FOLDER + "/" + filename;
                Log.d(TAG, filename);
                Sound sound = new Sound(path, filename.split("\\.")[0], "Apollo 13", "The Movie");

                try {
                    AssetFileDescriptor afd = mAssets.openFd(sound.getPath());
                    int soundId = mSoundPool.load(afd, 1);
                    sound.setId(soundId);
                    mSounds.add(sound);
                }
                catch(IOException e) {
                    Log.e(TAG, "could not load sound from file: " + sound.getPath(), e);
                }
            }
        }
        catch(IOException ioe) {
            Log.e(TAG, "could not load sound files.", ioe);
        }
    }

    public void play(Sound sound) {
        Integer id = sound.getId();
        if(id != null) {
            mSoundPool.play(
                    id,   // sound id
                    1.0f, // left volume
                    1.0f, // right volume
                    1,    // priority (ignored)
                    0,    // loop counter, 0 for no loop
                    1.0f  // playback rate
            );
        }
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void release() {
        mSoundPool.release();
    }
}
