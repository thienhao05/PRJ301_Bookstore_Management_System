<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <%-- Phần Head giữ nguyên --%>
    <head>
        <meta charset="UTF-8">
        <title>Cửa Hàng - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <%-- Style của Hào rất đẹp, giữ nguyên nhé --%>
    </head>
    <body>
        <%-- Logic xử lý Navbar và Content --%>
        
        <%-- Sửa lại dòng include này nếu pagination.jsp nằm cùng thư mục --%>
        <div class="mt-5 d-flex justify-content-center">
            <c:set var="targetAction" value="shop" scope="request"/>
            <%@include file="../components/pagination.jsp" %>
        </div>
    </body>
</html>