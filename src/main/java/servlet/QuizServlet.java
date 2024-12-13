package servlet;

import model.Constants;
import model.QuizService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {

    private QuizService quizService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.quizService = new QuizService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        if ("start".equals(action)) {
            // Загружаем все вопросы
            quizService.loadQuestions(session);

            // Логика для начала опроса
            session.setAttribute(Constants.CURRENT_INDEX, 0);
            session.setAttribute(Constants.SCORE, 0);
            session.setAttribute(Constants.QUIZ_ENDED, false);

            // Загружаем первый вопрос и добавляем в сессию
            session.setAttribute("question", quizService.getCurrentQuestion(0));

            // Отправляем запрос на следующую страницу
            req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
        } else if ("next".equals(action)) {
            int currentIndex = (int) session.getAttribute(Constants.CURRENT_INDEX);
            int answerIndex;

            try {
                answerIndex = Integer.parseInt(req.getParameter("answerIndex"));
            } catch (NumberFormatException e) {
                // Обработка исключения, если параметр не является числом
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid answer index.");
                return;
            }

            // Проверяем ответ
            boolean isCorrect = quizService.checkAnswer(currentIndex, answerIndex);
            int score = (int) session.getAttribute(Constants.SCORE);
            if (isCorrect) {
                score++;
            }
            session.setAttribute(Constants.SCORE, score);

            // Переходим к следующему вопросу
            currentIndex++;
            if (currentIndex >= quizService.getQuestionCount()) {
                session.setAttribute(Constants.QUIZ_ENDED, true);
                // Выводим финальный счет
                req.setAttribute("quizEnded", true);
                req.setAttribute("score", score);
            } else {
                session.setAttribute(Constants.CURRENT_INDEX, currentIndex);
                // Обновляем вопрос
                session.setAttribute("question", quizService.getCurrentQuestion(currentIndex));
            }

            // Отправляем запрос на следующую страницу
            req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
        }
    }
}