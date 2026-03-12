<%-- 
    Document   : home
    Created on : Mar 12, 2026, 4:46:37 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- THÊM DÒNG NÀY ĐỂ KHAI BÁO THƯ VIỆN JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home Page</title>
    </head>
    <body>
        <!-- Sửa "user" thành "LOGIN_USER" để khớp với Controller -->
        <c:if test="${not empty sessionScope.LOGIN_USER}">
            <!-- Gọi ra biến username (chứa full_name từ Database) -->
            <h1>Welcome, ${sessionScope.LOGIN_USER.username}</h1>

            <a href="MainController?action=logout">Logout</a><br/>
            <a href="search.jsp">Search</a>
        </c:if>

        <c:if test="${empty sessionScope.LOGIN_USER}">
            <!-- Nếu chưa đăng nhập thì đẩy về trang login -->
            <c:redirect url="MainController?action=login_page"/>
        </c:if>
        
        <hr>
        <h1>Hello World!</h1>
    </body>
</html>