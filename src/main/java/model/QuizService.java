package model;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuizService {
    private List<Question> questions;

    public void loadQuestions(HttpSession session) throws IOException {
        if (session.getAttribute(Constants.QUESTIONS) == null) {
            String jsonContent = new String(Files.readAllBytes(Paths.get("questions.json")));
            JSONArray jsonArray = new JSONArray(jsonContent);
            questions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);
                String questionText = questionObject.getString("question");
                JSONArray answersArray = questionObject.getJSONArray("answers");
                List<Answer> answers = new ArrayList<>();
                for (int j = 0; j < answersArray.length(); j++) {
                    JSONObject answerObject = answersArray.getJSONObject(j);
                    boolean isCorrect = answerObject.getBoolean("isCorrect");
                    String answerText = answerObject.getString("text");
                    answers.add(new Answer(isCorrect, answerText));
                }
                questions.add(new Question(questionText, answers));
            }
            session.setAttribute(Constants.QUESTIONS, questions);
            session.setAttribute(Constants.PLAYER, new Player()); // Инициализация игрока
        } else {
            questions = (List<Question>) session.getAttribute(Constants.QUESTIONS);
        }
    }

    public Question getCurrentQuestion(int currentIndex) {
        return questions.get(currentIndex);
    }

    public int getTotalScore(Player player) {
        return player.getScore();
    }

    public void updateScore(Player player, boolean isCorrect) {
        if (isCorrect) {
            player.setScore(player.getScore() + 1);
        }
    }

    public int getNextQuestionIndex(int currentIndex) {
        return currentIndex + 1;
    }

    public boolean hasMoreQuestions(int currentIndex) {
        return currentIndex < questions.size();
    }
}
