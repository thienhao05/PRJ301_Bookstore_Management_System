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
        <title>Login</title>
    </head>
    <body>
        <h2>Login</h2>

        <form action="MainController" method="post">
            Email:
            <input type="text" name="txtEmail" required />
            <br/>
            Password:
            <input type="password" name="txtPassword" required />
            <br/>
            <!-- Action truyền về MainController -->
            <input type="submit" name="action" value="Login"/>
        </form>

        <!-- Dùng EL để in lỗi nếu có -->
        <p style="color:red;">${requestScope.ERROR}</p>

    </body>
</html>