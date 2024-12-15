package service;

import lombok.Setter;
import model.Answer;
import model.Constants;
import model.Question;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuizService {
    @Setter
    private List<Question> questions;

    /**
     * Load all questions
     *
     * @param session take actual HttpSession
     * @throws IOException if file doesn't exist or other io exception
     */
    public void loadQuestions(HttpSession session) throws IOException {
        if (session.getAttribute(Constants.QUESTIONS) == null) {
            String filePath = session.getServletContext().getRealPath("/questions.json");
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

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
        } else {
            try {
                questions = (List<Question>) session.getAttribute(Constants.QUESTIONS);
            } catch (ClassCastException | NullPointerException e) {
                throw new RuntimeException(e.getMessage() + "...Something's wrong with questions...");
            }
        }
    }

    /**
     * Getting of current question
     *
     * @param currentIndex take current number of question
     * @return Question - current question
     */
    public Question getCurrentQuestion(int currentIndex) {
        if (currentIndex < 0 || currentIndex >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid question index: " + currentIndex);
        }
        return questions.get(currentIndex);
    }

    /**
     * Getting of actual question's number
     *
     * @return actual counter
     */
    public int getQuestionCount() {
        return questions.size();
    }


    /**
     * Checking of answer (right or no)
     *
     * @param currentIndex take current number of question
     * @param answerIndex  take index of answer
     * @return is selected answer correct?
     */
    public boolean checkAnswer(int currentIndex, int answerIndex) {
        if (currentIndex < 0 || currentIndex >= questions.size()) {
            throw new IndexOutOfBoundsException("Invalid question index: " + currentIndex);
        }
        Question question = questions.get(currentIndex);
        if (answerIndex < 0 || answerIndex >= question.getAnswers().size()) {
            throw new IndexOutOfBoundsException("Invalid answer index: " + answerIndex);
        }
        Answer selectedAnswer = question.getAnswers().get(answerIndex);
        return selectedAnswer.isCorrect();
    }
}