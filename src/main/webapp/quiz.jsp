<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Опросник по Java</title>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <script src="<c:url value="https://code.jquery.com/jquery-3.6.0.min.js"/>"></script>
</head>
<body>
<h1>Добро пожаловать в опросник по Java</h1>

<c:if test="${quizEnded}">
    <h2>Вы ответили правильно на ${score} вопросов из ${currentIndex + 1} </h2>
    <button onclick="window.location.href='?action=start'">Начать заново</button>
</c:if>

<c:if test="${not quizEnded}">
    <h2>${currentIndex + 1}. ${question.questionText}</h2>
    <form action="quiz" method="get">
        <input type="hidden" name="action" value="next">
        <input type="hidden" name="currentIndex" value="${currentIndex}">

        <c:forEach var="answer" items="${question.answers}" varStatus="status">
            <div>
                <input type="radio" name="answerIndex" value="${status.index}"> ${answer.text}
            </div>
        </c:forEach>

        <button type="submit">Next</button>

    </form>
</c:if>

</body>
</html>
