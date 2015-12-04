package csc296.quizdown;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by JeanMarc on 12/3/15.
 */
public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZuFAtG2H0QXwWsSdqQDmC96WJcFXr5AC9QxuA3ml", "MbFXIleSzrXjy95e8De54WlmX7vDqPsvhxOf1INH");

    }
}
