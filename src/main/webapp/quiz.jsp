<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Java Quiz</title>
</head>
<body>
<h1>Java Quiz</h1>

<c:if test="${quizEnded}">
    <h2>Your score: ${score}</h2>
    <button onclick="window.location.href='?action=start'">Начать заново</button>
</c:if>

<c:if test="${not quizEnded}">
    <h2>${question.questionText}</h2>
    <form action="quiz" method="get">
        <input type="hidden" name="action" value="next">
        <input type="hidden" name="currentIndex" value="${currentIndex}">
        <c:forEach var="answer" items="${question.answers}">
            <div>
                <input type="radio" name="answerIndex" value="${loop.index}"> ${answer.text}
            </div>
        </c:forEach>
        <button type="submit">Next</button>
    </form>
</c:if>

</body>
</html>
