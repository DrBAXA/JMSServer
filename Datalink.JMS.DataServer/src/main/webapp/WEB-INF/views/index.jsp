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
        <form action="<c:url value="/"/>start">
            <button type="submit" <c:if test="${status == 'true'}">disabled="disabled"</c:if>>Start</button>
        </form>
        <form action="<c:url value="/"/>stop">
            <button type="submit" <c:if test="${status == 'false'}">disabled="disabled"</c:if>>Stop</button>
        </form>
    </div>
    <div>
        <table title="Requests">
            <tr>
                <th>ALL</th>
                <th>GET</th>
                <th>GET_ALL</th>
                <th>PUT</th>
                <th>DELETE</th>
            </tr>
        </table>
    </div>
</body>
</html>
