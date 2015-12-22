/*
Jean-Marc Boullianne
jboullia@u.rochester.edu
Project 03: QuizDown
 */

package csc296.quizdown.model;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JeanMarc on 12/5/15.
 */

/*
Holds all data for current game going on.
    - Updates User Data when game is finished, or when the user quits
    - Updates Topic class data as well as Overall User Data
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

    //Grabs Questions from Parse Database in the cloud
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

    //updates all user data after game is finished
    public String finishGame(){

        /*
        Update
            - Level
            - Experience
            - Stats
            - Game History
            - Update Overall Stats
         */
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(dataClass);
            query.whereEqualTo("username", currentUser.getUsername());
            ParseObject topicData = query.getFirst();
            topicData.put("experience", topicData.getInt("experience") + currentScore);
            topicData.put("level", getNewLevel(topicData.getInt("experience")));
            topicData.put("game_history", updateGameHistory(topicData.getJSONArray("game_history")));
            topicData.put("total_games", topicData.getInt("total_games") + 1);

            Log.d(TAG, String.valueOf(topicData.getInt("level")));
            Log.d(TAG, String.valueOf(topicData.getInt("experience")));
            Log.d(TAG, topicData.getJSONArray("game_history").toString());

            topicData.save();
            new updateUserTask().execute(topicData);
            return topicData.getClassName();
        }catch (ParseException e){

        }

        return null;
    }

    //helper method for updating User Level
    public int getNewLevel(int exp){
        return (exp + currentScore) / 100;
    }

    //updates user game history so it can be correctly displyed in the graph
    public JSONArray updateGameHistory(JSONArray gameHistory){

        List<Integer> history = new LinkedList<>();
        try {

            for (int i = 0; i < gameHistory.length(); i++) {
                history.add(gameHistory.getInt(i));
            }
            if (gameHistory.length() == 10) {
                history.add(currentScore);
                history.remove(0);
            } else {
                history.add(currentScore);
            }
        }catch (JSONException e){
            Log.d(TAG, "Error updated game history");
        }

        JSONArray updatedHistory = new JSONArray(history);
        return updatedHistory;
    }

    //Updates the user in the background that way the user can do other things in the UI
    private class updateUserTask extends AsyncTask<ParseObject, Integer, Void> {
        protected Void doInBackground(ParseObject... topic) {

            ParseUser updatedUser = currentUser;
            updatedUser = updateOverallGameHistory(updatedUser);
            updatedUser = updateOverallGameCount(updatedUser);
            updatedUser = updateOverallGameCount(updatedUser);
            updatedUser = updateBestTopic(updatedUser, topic[0]);
            updatedUser = updateOverallHighestScore(updatedUser);
            updatedUser = updateOverallExperience(updatedUser);

            updatedUser.saveInBackground();
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Long result) {

        }
    }

    //Updates overall game history for the home screen graph
    public ParseUser updateOverallGameHistory(ParseUser user){
        JSONArray gameHistory = user.getJSONArray("game_history");
        List<Integer> history = new LinkedList<>();
        try {

            for (int i = 0; i < gameHistory.length(); i++) {
                history.add(gameHistory.getInt(i));
            }
            if (gameHistory.length() == 10) {
                history.add(currentScore);
                history.remove(0);
            } else {
                history.add(currentScore);
            }
        }catch (JSONException e){
            Log.d(TAG, "Error updated game history");
        }

        JSONArray updatedHistory = new JSONArray(history);
        user.put("game_history", updatedHistory);

        return user;
    }

    //Updates Overall game Count
    public ParseUser updateOverallGameCount(ParseUser user){
        int gameCount = user.getInt("total_games");
        user.put("total_games", gameCount+1);
        return user;
    }

    //updates the user overall best Topic, which is displayed on the home screen
    public ParseUser updateBestTopic(ParseUser user, ParseObject topicData){
        if(user.getInt("highest_level") < topicData.getInt("level")) {
            user.put("best_topic", currentTopic);
            user.put("highest_level", topicData.getInt("level"));
        }
        return user;
    }

    //Updates the users overall high game score which is displayed on the home screen
    public ParseUser updateOverallHighestScore(ParseUser user){
        if(currentScore > user.getInt("highest_score"))
            user.put("highest_score", currentScore);
        return user;
    }

    //updates the users overall game experience which is used for the leaderboards
    public ParseUser updateOverallExperience(ParseUser user){
        user.put("overall_experience", user.getInt("overall_experience") + currentScore);
        return user;
    }

}
