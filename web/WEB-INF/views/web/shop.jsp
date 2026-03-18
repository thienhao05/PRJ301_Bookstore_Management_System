<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cửa Hàng Sách - Hào's Bookstore</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <style>
            body { background-color: #f4f7f6; font-family: 'Segoe UI', sans-serif; }
            .book-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
                background: #fff;
                height: 100%;
            }
            .book-card:hover {
                transform: translateY(-8px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .book-cover {
                height: 200px;
                background-color: #e9ecef;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 10px;
                margin-bottom: 12px;
            }
            .filter-card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            }
        </style>
    </head>
    <body>

        <%@include file="../components/web-header.jsp" %>

        <div class="container py-5">

            <%-- Tiêu đề trang --%>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="fw-bold text-dark mb-0">
                        <c:choose>
                            <c:when test="${not empty requestScope.KEYWORD}">
                                Kết quả: "<strong>${requestScope.KEYWORD}</strong>"
                            </c:when>
                            <c:otherwise>
                                Tất Cả Sách
                            </c:otherwise>
                        </c:choose>
                    </h2>
                    <p class="text-muted mb-0">
                        Tìm thấy
                        <strong>${empty BOOK_LIST ? 0 : BOOK_LIST.size()}</strong>
                        cuốn sách
                    </p>
                </div>
                <a href="MainController?action=view"
                   class="btn btn-outline-secondary btn-sm rounded-pill px-3">
                    <i class="bi bi-arrow-clockwise me-1"></i>Làm mới
                </a>
            </div>

            <%-- Thanh tìm kiếm --%>
            <div class="card filter-card p-3 mb-4">
                <form action="MainController" method="GET">
                    <input type="hidden" name="action" value="search">
                    <div class="input-group">
                        <input class="form-control border-end-0 rounded-start-pill px-4"
                               type="search"
                               name="query"
                               value="${requestScope.KEYWORD}"
                               placeholder="Tìm tên sách, tác giả...">
                        <button class="btn btn-primary border-start-0 rounded-end-pill px-4"
                                type="submit">
                            <i class="bi bi-search me-1"></i>Tìm kiếm
                        </button>
                    </div>
                </form>
            </div>

            <%-- Lọc theo danh mục --%>
            <div class="mb-4">
                <div class="d-flex flex-wrap gap-2">
                    <a href="MainController?action=view"
                       class="btn btn-sm ${empty param.categoryId ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">
                        <i class="bi bi-grid me-1"></i>Tất cả
                    </a>
                    <c:forEach var="cat" items="${CATEGORY_LIST}">
                        <a href="MainController?action=view&categoryId=${cat.id}"
                           class="btn btn-sm ${param.categoryId == cat.id ? 'btn-primary' : 'btn-outline-secondary'} rounded-pill px-3">
                            ${cat.name}
                        </a>
                    </c:forEach>
                </div>
            </div>

            <%@include file="../components/message-alert.jsp" %>

            <%-- Danh sách sách --%>
            <div class="row g-4">
                <c:forEach var="book" items="${BOOK_LIST}">
                    <div class="col-6 col-md-4 col-lg-3">
                        <div class="card book-card p-3 shadow-sm">

                            <%-- Ảnh bìa + link chi tiết --%>
                            <a href="MainController?action=bookDetail&id=${book.bookId}"
                               class="text-decoration-none">
                                <div class="book-cover shadow-sm">
                                    <i class="bi bi-book display-4 text-muted opacity-25"></i>
                                </div>
                                <h6 class="fw-bold text-dark text-truncate mb-1">
                                    ${book.title}
                                </h6>
                                <p class="small text-muted mb-2">${book.author}</p>
                            </a>

                            <%-- Badge tồn kho --%>
                            <div class="mb-2">
                                <c:choose>
                                    <c:when test="${book.stock > 5}">
                                        <span class="badge bg-success-subtle text-success border border-success-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            <i class="bi bi-check-circle me-1"></i>Còn hàng
                                        </span>
                                    </c:when>
                                    <c:when test="${book.stock > 0}">
                                        <span class="badge bg-warning-subtle text-warning border border-warning-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            <i class="bi bi-exclamation-circle me-1"></i>Sắp hết: ${book.stock} cuốn
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            <i class="bi bi-x-circle me-1"></i>Hết hàng
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <%-- Giá + nút thêm giỏ hàng --%>
                            <div class="d-flex justify-content-between align-items-center mt-auto">
                                <span class="fw-bold text-primary">
                                    <fmt:formatNumber value="${book.price}" pattern="#,###"/> đ
                                </span>

                                <c:choose>
                                    <%-- Hết hàng -> disable --%>
                                    <c:when test="${book.stock <= 0}">
                                        <button class="btn btn-secondary btn-sm px-3" disabled
                                                title="Hết hàng">
                                            <i class="bi bi-cart-x"></i>
                                        </button>
                                    </c:when>
                                    <%-- Chưa đăng nhập -> về trang login --%>
                                    <c:when test="${empty sessionScope.LOGIN_USER}">
                                        <a href="MainController?action=login"
                                           class="btn btn-outline-primary btn-sm px-3"
                                           title="Đăng nhập để mua hàng">
                                            <i class="bi bi-cart-plus"></i>
                                        </a>
                                    </c:when>
                                    <%-- Đã đăng nhập + còn hàng -> thêm vào giỏ --%>
                                    <c:otherwise>
                                        <a href="MainController?action=addToCart&bookId=${book.bookId}"
                                           class="btn btn-primary btn-sm px-3 shadow-sm"
                                           title="Thêm vào giỏ hàng">
                                            <i class="bi bi-cart-plus"></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <%-- Không có sách nào --%>
                <c:if test="${empty BOOK_LIST}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-search display-1 text-muted opacity-25"></i>
                        <h4 class="mt-3 text-muted">
                            <c:choose>
                                <c:when test="${not empty requestScope.KEYWORD}">
                                    Không tìm thấy sách nào với từ khóa
                                    "<strong>${requestScope.KEYWORD}</strong>"
                                </c:when>
                                <c:otherwise>
                                    Chưa có sách nào trong cửa hàng
                                </c:otherwise>
                            </c:choose>
                        </h4>
                        <a href="MainController?action=view"
                           class="btn btn-primary mt-3 rounded-pill px-4">
                            <i class="bi bi-arrow-clockwise me-1"></i>Xem tất cả sách
                        </a>
                    </div>
                </c:if>
            </div>
        </div>

        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>