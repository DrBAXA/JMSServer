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
    <c:if test="${status == true}"><p style="color: green">Server is started and connected to JMSServer</p></c:if>
    <c:if test="${status == false}"><p style="color: red">Server is stopped</p></c:if>
    <hr>
    <div>
        <form action="<c:url value="/"/>start">
            <button type="submit" <c:if test="${status == true}">disabled="disabled"</c:if>>Start</button>
        </form>
        <form action="<c:url value="/"/>stop">
            <button type="submit" <c:if test="${status == false}">disabled="disabled"</c:if>>Stop</button>
        </form>
    </div>
    <hr>
    <div>
        <table title="Requests" style="border: solid 1px blue; width:500px" rules="all">
            <caption>Requests statistics</caption>
            <tr style="background-color: lightgray">
                <th>ALL</th>
                <th>GET</th>
                <th>GET_ALL</th>
                <th>PUT</th>
                <th>DELETE</th>
            </tr>
            <tr>
                <td align="center">${get + get_all + put + delete}</td>
                <td align="center">${get}</td>
                <td align="center">${get_all}</td>
                <td align="center">${put}</td>
                <td align="center">${delete}</td>
            </tr>
        </table>
    </div>
</body>
</html>
