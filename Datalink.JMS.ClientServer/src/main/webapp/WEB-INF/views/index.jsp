<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<script src="<c:url value="/resources"/>/jQuery/jquery.min.js"></script>
<html>

<head>
    <script>
        function getId(){
            var url ='http://' + window.location.hostname + (window.location.port ? ':' + window.location.port: '') + '/users/' + $('#userId').val();
            window.location.href = url;
        }

        function deleteId(){
            var url ='/users/' + $('#delId').val();
            $.ajax({
                url : url,
                method : 'DELETE',
                statusCode: {
                    200: function(data){
                        $('#result').text(data).css('color', 'green')
                    },
                    500:function(data){
                        $('#result').text(data.responseText).css('color', 'red')
                    }
                }
            })
        }
    </script>
    <title>UsersManaging</title>
</head>
<body>
    <header>
        <h1>UsersManaging</h1>
    </header>
    <hr>
    <p id="result" style="color: ${resultStatus}">
    <c:if test="${not empty result}">
        ${result}
        <hr>
    </c:if>
    </p>
    <p>Delete user by id</p>
    <form id="delForm" action="#" onsubmit="deleteId()" method="delete">
        <label for="delId">Id</label><input id="delId" required="required"/>
        <button type="submit">Delete</button>
    </form>
    <hr>
    <div>
        <p>Add user</p>
        <form:form modelAttribute="user" method="POST" action="/users">
            <form:hidden path="id" required="required"/>
            <label for="name">Name</label><form:input path="name" required="required"/>
            <label for="surname">Surname</label><form:input path="surname" required="required"/>
            <form:button type="submit">Add</form:button>
        </form:form>
    </div>
    <hr>
    <p>Get user by id</p>
    <form onsubmit="getId()" action="#">
        <label for="userId">Id</label><input id="userId" required="required"/>
        <button type="submit">Get</button>
    </form>
    <c:if test="${not empty requestedUser}">
        <table style="border: solid 1px blue; width:500px" rules="all">
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Surname</th>
            </tr>
            <tr>
                <td align="center">${requestedUser.id}</td>
                <td align="center">${requestedUser.name}</td>
                <td align="center">${requestedUser.surname}</td>
            </tr>
        </table>
    </c:if>
    <hr>
    <p>Get all users</p>
    <form action="/users" method="GET">
        <button onclick="">Get all</button>
    </form>
    <c:if test="${not empty users}">
        <table style="border: solid 1px blue; width:500px" rules="all">
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Surname</th>
            </tr>
            <c:forEach var="user" items="${users}" >
                <tr>
                    <td align="center">${user.id}</td>
                    <td align="center">${user.name}</td>
                    <td align="center">${user.surname}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

</body>
</html>
