<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>

<head>
    <script>
        function getId(){
            var url ='http://' + window.location.hostname + (window.location.port ? ':' + window.location.port: '') + '/users/' + document.getElementById('userId').value;
            window.location.href = url;
        }
    </script>
    <title>UsersManaging</title>
</head>
<body>
    <header>
        <h1>UsersManaging</h1>
    </header>
    <hr>
    <br>
    <c:if test="${not empty result}">
        <p>${result}</p>
        <hr>
        <br>
    </c:if>
    <c:if test="${not empty error}">
        <p>${error}</p>
        <hr>
        <br>
    </c:if>
    <div>
        <form:form modelAttribute="user" method="POST" action="/users">
            <form:hidden path="id" required="required"/>
            <form:input path="name" required="required"/>
            <form:input path="surname" required="required"/>
            <form:button type="submit"/>
        </form:form>
    </div>
    <hr>
    <br>
    <form onsubmit="getId()" action="#">
        <label for="userId">Id</label><input id="userId"/>
        <button type="submit">Get</button>
    </form>
    <c:if test="${not empty requestedUser}">
        <span>${requestedUser.id}</span><span>${requestedUser.name}</span><span>${requestedUser.surname}</span>
        <hr>
        <br>
    </c:if>
    <hr>
    <br>
    <form action="/users" method="GET">
        <button onclick="">Get all</button>
    </form>
    <c:if test="${not empty users}">
        <table >
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Surname</th>
            </tr>
            <c:forEach var="user" items="${users}" >
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.surname}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

</body>
</html>
