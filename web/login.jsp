<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    model.UserDTO user =
        (model.UserDTO) session.getAttribute("LOGIN_USER");

    if (user != null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>

<h2>Login</h2>

<form action="MainController" method="post">
    Email: <input type="text" name="txtEmail" required /> <br/>
    Password: <input type="password" name="txtPassword" required /> <br/>
    <input type="submit" name="action" value="Login" />
</form>

<%
    String error = (String) request.getAttribute("ERROR");
    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

</body>
</html>