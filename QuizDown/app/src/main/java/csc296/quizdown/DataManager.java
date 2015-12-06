package csc296.quizdown;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JeanMarc on 12/5/15.
 */
public class DataManager {

    private static final String TAG = "DATA_MANAGER";
    private static ParseUser currentUser;

    private int currentScore;
    private int currentRound;
    private String currentTopic;
    private int timeLeft;
    private String dataClass;
    private String questionClass;
    private Boolean questionsReady;

    private List<Question> questions;

    public DataManager(String currentTopic, String dataClass, String questionClass){
        currentRound = 1;
        currentScore = 0;
        this.currentTopic = currentTopic;
        this.dataClass = dataClass;
        this.questionClass = questionClass;
        timeLeft = 20;
        questions = new LinkedList<>();
        questionsReady = false;
        generateQuestions();
    }

    public static ParseUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(ParseUser currentUser) {
        DataManager.currentUser = currentUser;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public String getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(String currentTopic) {
        this.currentTopic = currentTopic;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void generateQuestions(){
        questionsReady = false;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(questionClass);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject tmp: list){
                    String question = tmp.getString("question");
                    String answer1 = tmp.getString("answer1");
                    String answer2 = tmp.getString("answer2");
                    String answer3 = tmp.getString("answer3");
                    String answer4 = tmp.getString("answer4");
                    int correctAnswer = tmp.getInt("correct_answer");
                    questions.add(new Question(question, answer1, answer2, answer3, answer4, correctAnswer));
                }
                questionsReady = true;
                Log.d(TAG, questions.toString());
            }
        });
    }

    public List<Question> getQuestions(){
        return questions;
    }

    public boolean areQuestionsReady(){
        return questionsReady;
    }

}
