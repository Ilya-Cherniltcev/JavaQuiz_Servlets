package service;

import model.Answer;
import model.Question;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class QuizServiceTest {
    private QuizService service;
    private List<Question> questions;

    @BeforeEach
    public void setup() {
        service = new QuizService();
        questions = new ArrayList<>();

        String jsonContent = "{\"question\": \"Какой сборщик мусора используется в Java?\", " +
                "\"answers\": [{\"isCorrect\": true, \"text\": \"Garbage Collector\"}, " +
                "{\"isCorrect\": false, \"text\": \"Memory Manager\"}, " +
                "{\"isCorrect\": false, \"text\": \"Auto Cleaner\"}]}";

        JSONObject questionObject = new JSONObject(jsonContent);
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
        service.setQuestions(questions);
    }

    @Test
    public void getCurrentQuestionTest() {
        int currentIndex = 0;
        Question expectedQuestion = questions.get(currentIndex);
        Question actualQuestion = service.getCurrentQuestion(currentIndex);
        Assertions.assertEquals(expectedQuestion, actualQuestion);
    }


    @Test
    public void getCurrentQuestionIfCurrentIndexOutOfRangeTest() {
        int currentIndex = -1;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> service.getCurrentQuestion(currentIndex));
    }

    @Test
    public void getQuestionCountTest() {
        int expectedCount = 1;
        int actualCount = service.getQuestionCount();
        Assertions.assertEquals(expectedCount, actualCount);
    }

    @Test
    public void checkAnswerIfCurrentIndexOutOfRangeTest() {
        int currentIndex = -1;
        int answerIndex = 1;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> service.checkAnswer(currentIndex, answerIndex));
    }

    @Test
    public void checkAnswerIfAnswerIndexOutOfRangeTest() {
        int currentIndex = 0;
        int answerIndex = -1;
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> service.checkAnswer(currentIndex, answerIndex));
    }

    @Test
    public void checkAnswerTest() {
        int currentIndex = 0;
        int answerIndex = 0;
        boolean expectedCheck = true;
        boolean actualCheck = service.checkAnswer(currentIndex, answerIndex);
        Assertions.assertEquals(expectedCheck, actualCheck);
    }
}