<%@page import="dto.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Kéo user từ session xuống
    UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");
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
        <!-- Dùng EL thay cho <%= user.getFullName() %> -->
        <h2>Welcome ${sessionScope.LOGIN_USER.fullName}</h2>
        <p>Email: ${sessionScope.LOGIN_USER.email}</p>

        <form action="MainController" method="post">
            <input type="submit" name="action" value="Logout">
        </form>
    </body>
</html>