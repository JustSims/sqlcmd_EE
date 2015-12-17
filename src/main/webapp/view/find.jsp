<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        <table border = "1">
            <c:forEach items="${table}" var="row">
                <tr>
                    <c:forEach items="${row}" var="element">
                        <td align="center">
                            ${element}
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        Back to <a href="menu">menu</a><br>
    </body>
</html>
