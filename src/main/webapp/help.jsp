<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>SQLCmd</title>
    </head>
    <body>
        Existing commands: <br>
        'connect|databaseName|userName|password' - to connect to your database <br>
        'list' - to show all existing table names<br>
        'clear|tableName' -  to clear whole table<br>
        'create|tableName|column1|value1|column2|value2|...|columnN|ValueN' -  to create record in table<br>
        'find|tableName' - to show table 'tableName' content<br>
        'help' - to show help for this project<br>
        'exit' - to exit the program<br>
        You can go back to <a href="menu">Menu</a><br>
    </body>
</html>

