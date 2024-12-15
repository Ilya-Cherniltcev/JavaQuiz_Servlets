package servlet;

import model.Constants;
import service.QuizService;

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
            // Loading all questions
            quizService.loadQuestions(session);

            // Logic for beginning of quiz
            session.setAttribute(Constants.CURRENT_INDEX, 0);
            session.setAttribute(Constants.SCORE, 0);
            session.setAttribute(Constants.QUIZ_ENDED, false);

            // Load first question and add to session
            session.setAttribute("question", quizService.getCurrentQuestion(0));

            // Send request to next page
            req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
        } else if ("next".equals(action)) {
            int currentIndex = (int) session.getAttribute(Constants.CURRENT_INDEX);
            int answerIndex;

            try {
                answerIndex = Integer.parseInt(req.getParameter("answerIndex"));
            } catch (NumberFormatException e) {
                // Checking for bad parameter (if it doesn't a number)
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid answer index.");
                return;
            }

            // Checking of answer
            boolean isCorrect = quizService.checkAnswer(currentIndex, answerIndex);
            int score = (int) session.getAttribute(Constants.SCORE);
            if (isCorrect) {
                score++;
            }
            session.setAttribute(Constants.SCORE, score);

            // Go to next question
            currentIndex++;
            if (currentIndex >= quizService.getQuestionCount()) {
                session.setAttribute(Constants.QUIZ_ENDED, true);
                // print final score
                req.setAttribute("quizEnded", true);
                req.setAttribute("score", score);
            } else {
                session.setAttribute(Constants.CURRENT_INDEX, currentIndex);
                // Renew question
                session.setAttribute("question", quizService.getCurrentQuestion(currentIndex));
            }

            // Send request to next page
            req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
        }
    }
}