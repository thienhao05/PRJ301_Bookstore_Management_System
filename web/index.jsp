<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    model.UserDTO user = (model.UserDTO) session.getAttribute("LOGIN_USER");

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