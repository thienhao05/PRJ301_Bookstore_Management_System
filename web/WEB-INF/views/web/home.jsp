<%-- 
    Document   : home
    Created on : Mar 12, 2026, 4:46:37 AM
    Author     : PC (Hào)
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Trang Chủ - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body {
                background-color: #f4f7f6;
                font-family: 'Segoe UI', sans-serif;
            }
            .hero-section {
                background: linear-gradient(135deg, #0d6efd 0%, #003da5 100%);
                color: white;
                padding: 60px 0;
                border-radius: 0 0 50px 50px;
            }
            .order-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
                background: #fff;
                height: 100%;
            }
            .order-card:hover {
                transform: translateY(-10px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .book-cover {
                height: 250px;
                background-color: #e9ecef;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 10px;
                margin-bottom: 15px;
            }
            .btn-add-cart {
                border-radius: 10px;
                font-weight: 600;
            }
        </style>
    </head>
    <body>

        <%@include file="web-header.jsp" %>

        <section class="hero-section shadow-sm">
            <div class="container text-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.LOGIN_USER}">
                        <h1 class="display-4 fw-bold mb-3">Chào mừng trở lại, ${sessionScope.LOGIN_USER.fullName}!</h1>
                    </c:when>
                    <c:otherwise>
                        <h1 class="display-4 fw-bold mb-3">Khám Phá Kho Tàng Tri Thức</h1>
                    </c:otherwise>
                </c:choose>
                <p class="lead opacity-75 mb-4">Hàng ngàn đầu sách mới đang chờ đợi bạn tại Hào's Bookstore.</p>
                <div class="d-flex justify-content-center gap-2">
                    <a href="#book-list" class="btn btn-light btn-lg px-4 rounded-pill fw-bold">Mua sắm ngay</a>
                    <c:if test="${empty sessionScope.LOGIN_USER}">
                        <a href="login.jsp" class="btn btn-outline-light btn-lg px-4 rounded-pill">Đăng nhập</a>
                    </c:if>
                </div>
            </div>
        </section>

        <div class="container py-5" id="book-list">
            <div class="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <h2 class="fw-bold text-dark mb-0">Sách Mới Nhất</h2>
                    <p class="text-muted mb-0">Những tác phẩm vừa cập bến cửa hàng</p>
                </div>
                <a href="MainController?action=allBooks" class="text-primary fw-bold text-decoration-none">
                    Xem tất cả <i class="bi bi-arrow-right"></i>
                </a>
            </div>

            <%@include file="message-alert.jsp" %>

            <div class="row g-4">
                <%-- Loop qua danh sách sách từ requestScope --%>
                <c:forEach var="book" items="${requestScope.BOOK_LIST}">
                    <div class="col-6 col-md-4 col-lg-3">
                        <div class="card order-card p-3">
                            <a href="MainController?action=bookDetail&id=${book.bookId}" class="text-decoration-none">
                                <div class="book-cover shadow-sm">
                                    <i class="bi bi-book display-4 text-muted opacity-25"></i>
                                </div>
                                <h6 class="fw-bold text-dark text-truncate mb-1">${book.title}</h6>
                                <p class="small text-muted mb-2">${book.author}</p>
                            </a>
                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <span class="fw-bold text-primary">
                                    <fmt:formatNumber value="${book.price}" pattern="#,###"/> đ
                                </span>
                                <a href="MainController?action=addToCart&bookId=${book.bookId}" class="btn btn-primary btn-sm btn-add-cart px-3 shadow-sm">
                                    <i class="bi bi-cart-plus"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <%-- Fallback nếu không có dữ liệu --%>
                <c:if test="${empty requestScope.BOOK_LIST}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-inbox display-1 text-muted opacity-25"></i>
                        <p class="text-muted mt-3">Hiện chưa có sách nào trong cửa hàng. Hao hãy vào trang Admin để thêm nhé!</p>
                    </div>
                </c:if>
            </div>
        </div>

        <%@include file="web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>