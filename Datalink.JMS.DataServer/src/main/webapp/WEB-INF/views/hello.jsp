<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>

<head>
    <title>StoringServer</title>
</head>
<body>
    <header>
        <h1>Storing server</h1>
    </header>
    <hr>
    <div>
        <a href="<c:url value="/"/>start" <c:if test="${status == 'true'}">disabled</c:if>>Strart</a>
        <a href="<c:url value="/"/>stop" <c:if test="${status == 'false'}">disabled</c:if>>Stop</a>
    </div>
</body>
</html>
