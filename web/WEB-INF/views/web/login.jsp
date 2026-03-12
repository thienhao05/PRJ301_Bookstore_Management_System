<%-- 
    Document   : login
    Created on : Mar 12, 2026, 4:50:18 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="MainController" method="post">

            Username:
            <input type="text" name="username"> 
            <br>

            Password:
            <input type="password" name="password">

            <input type="hidden" name="action" value="login">

            <button type="submit">Login</button>

        </form>
    </body>
</html>
