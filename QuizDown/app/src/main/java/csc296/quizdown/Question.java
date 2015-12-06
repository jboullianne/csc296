package csc296.quizdown;

/**
 * Created by JeanMarc on 12/5/15.
 */
public class Question {

    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;

    public Question(String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrectAnswer(int choice){
        return choice == correctAnswer;
    }
}
