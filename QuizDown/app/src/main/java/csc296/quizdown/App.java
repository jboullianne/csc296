/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

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
        //Initializes the Parse SDK so app can contact the Parse Database and get application info
        Parse.initialize(this, "ZuFAtG2H0QXwWsSdqQDmC96WJcFXr5AC9QxuA3ml", "MbFXIleSzrXjy95e8De54WlmX7vDqPsvhxOf1INH");

    }
}
