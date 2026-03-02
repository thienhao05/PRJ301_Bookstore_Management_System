<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    model.UserDTO user =
        (model.UserDTO) session.getAttribute("LOGIN_USER");

    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>

<h2>Welcome <%= user.getFullName() %></h2>
<p>Email: <%= user.getEmail() %></p>
<p>Role ID: <%= user.getRoleId() %></p>

<form action="MainController" method="post">
    <input type="submit" name="action" value="Logout" />
</form>

</body>
</html>