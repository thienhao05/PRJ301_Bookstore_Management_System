<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
    if (user != null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Book Store</title>
    </head>
    <body>
        <h2>Welcome to Book Store</h2>
        <a href="login.jsp">Login</a>
    </body>
</html>