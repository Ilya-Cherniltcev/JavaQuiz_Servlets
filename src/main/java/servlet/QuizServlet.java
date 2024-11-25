package servlet;

import model.Constants;
import model.QuizService;
import model.Player;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private final QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String action = req.getParameter("action");

        if ("start".equalsIgnoreCase(action)) {
            quizService.loadQuestions(session);
            req.setAttribute("currentIndex", 0);
        } else if ("next".equalsIgnoreCase(action)) {
            int currentIndex = Integer.parseInt(req.getParameter("currentIndex"));
            Player player = (Player) session.getAttribute(Constants.PLAYER);
            String answerIndex = req.getParameter("answerIndex");
            if (answerIndex != null) {
                int index = Integer.parseInt(answerIndex);
                boolean isCorrect = quizService.getCurrentQuestion(currentIndex).getAnswers().get(index).isCorrect();
                quizService.updateScore(player, isCorrect);
            }
            currentIndex = quizService.getNextQuestionIndex(currentIndex);
            if (!quizService.hasMoreQuestions(currentIndex)) {
                req.setAttribute("score", quizService.getTotalScore(player));
                session.invalidate(); // Сброс сессии после завершения викторины
                req.setAttribute("quizEnded", true);
                currentIndex = -1; // Установим индекс на -1 для отображения результата
            }
            req.setAttribute("currentIndex", currentIndex);
        }
        req.setAttribute("question", quizService.getCurrentQuestion((Integer) req.getAttribute("currentIndex")));
        req.getRequestDispatcher("/quiz.jsp").forward(req, resp);
    }
}