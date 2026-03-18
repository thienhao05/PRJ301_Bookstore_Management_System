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
            body { background-color: #f4f7f6; font-family: 'Segoe UI', sans-serif; }

            /* Hero */
            .hero-section {
                background: linear-gradient(135deg, #0d6efd 0%, #003da5 100%);
                color: white;
                padding: 70px 0;
                border-radius: 0 0 50px 50px;
            }

            /* Book card */
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

            /* News card */
            .news-card {
                border: none;
                border-radius: 15px;
                transition: 0.3s;
                background: #fff;
                height: 100%;
                overflow: hidden;
            }
            .news-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.1);
            }
            .news-thumb {
                height: 160px;
                background: linear-gradient(135deg, #e3f0ff 0%, #cce0ff 100%);
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .news-content-text {
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }

            /* Section title */
            .section-title {
                font-weight: 700;
                color: #212529;
                margin-bottom: 0;
            }

            /* Stats bar */
            .stats-bar {
                background: white;
                border-radius: 20px;
                box-shadow: 0 5px 20px rgba(0,0,0,0.08);
                padding: 20px 30px;
                margin-top: -30px;
                position: relative;
                z-index: 10;
            }
            .stat-item {
                text-align: center;
            }
            .stat-number {
                font-size: 1.8rem;
                font-weight: 700;
                color: #0d6efd;
            }
            .stat-label {
                font-size: 0.85rem;
                color: #6c757d;
            }
        </style>
    </head>
    <body>

        <%@include file="../components/web-header.jsp" %>

        <%-- ==================== HERO ==================== --%>
        <section class="hero-section shadow-sm">
            <div class="container text-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.LOGIN_USER}">
                        <h1 class="display-5 fw-bold mb-3">
                            Chào mừng trở lại,
                            <span style="color:#ffd700;">
                                ${sessionScope.LOGIN_USER.fullName}!
                            </span>
                        </h1>
                    </c:when>
                    <c:otherwise>
                        <h1 class="display-5 fw-bold mb-3">
                            Khám Phá Kho Tàng Tri Thức
                        </h1>
                    </c:otherwise>
                </c:choose>
                <p class="lead opacity-75 mb-4">
                    Hàng ngàn đầu sách mới đang chờ đợi bạn tại Hào's Bookstore
                </p>
                <div class="d-flex justify-content-center gap-3 flex-wrap">
                    <a href="MainController?action=view"
                       class="btn btn-light btn-lg px-5 rounded-pill fw-bold shadow-sm">
                        <i class="bi bi-shop me-2"></i>Mua sắm ngay
                    </a>
                    <c:if test="${empty sessionScope.LOGIN_USER}">
                        <a href="MainController?action=login"
                           class="btn btn-outline-light btn-lg px-5 rounded-pill fw-bold">
                            <i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập
                        </a>
                    </c:if>
                </div>
            </div>
        </section>

        <%-- ==================== STATS BAR ==================== --%>
        <div class="container">
            <div class="stats-bar">
                <div class="row g-3 text-center">
                    <div class="col-6 col-md-3 stat-item">
                        <div class="stat-number">
                            ${not empty BOOK_LIST ? BOOK_LIST.size() : 0}+
                        </div>
                        <div class="stat-label">Đầu sách</div>
                    </div>
                    <div class="col-6 col-md-3 stat-item">
                        <div class="stat-number">100%</div>
                        <div class="stat-label">Chính hãng</div>
                    </div>
                    <div class="col-6 col-md-3 stat-item">
                        <div class="stat-number">
                            <i class="bi bi-truck text-primary" style="font-size:1.5rem;"></i>
                        </div>
                        <div class="stat-label">Miễn phí vận chuyển</div>
                    </div>
                    <div class="col-6 col-md-3 stat-item">
                        <div class="stat-number">
                            <i class="bi bi-headset text-primary" style="font-size:1.5rem;"></i>
                        </div>
                        <div class="stat-label">Hỗ trợ 24/7</div>
                    </div>
                </div>
            </div>
        </div>

        <%-- ==================== SÁCH MỚI NHẤT ==================== --%>
        <div class="container py-5">
            <div class="d-flex justify-content-between align-items-end mb-4">
                <div>
                    <h2 class="section-title">Sách Mới Nhất</h2>
                    <p class="text-muted mb-0">Những tác phẩm vừa cập bến cửa hàng</p>
                </div>
                <a href="MainController?action=view"
                   class="text-primary fw-bold text-decoration-none">
                    Xem tất cả <i class="bi bi-arrow-right"></i>
                </a>
            </div>

            <%@include file="../components/message-alert.jsp" %>

            <div class="row g-4">
                <c:forEach var="book" items="${BOOK_LIST}">
                    <div class="col-6 col-md-4 col-lg-3">
                        <div class="card book-card p-3 shadow-sm">
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
                                        <span class="badge bg-success-subtle text-success
                                                     border border-success-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            Còn hàng
                                        </span>
                                    </c:when>
                                    <c:when test="${book.stock > 0}">
                                        <span class="badge bg-warning-subtle text-warning
                                                     border border-warning-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            Sắp hết: ${book.stock} cuốn
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger-subtle text-danger
                                                     border border-danger-subtle rounded-pill px-2"
                                              style="font-size:11px;">
                                            Hết hàng
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
                                    <c:when test="${book.stock <= 0}">
                                        <button class="btn btn-secondary btn-sm px-3" disabled>
                                            <i class="bi bi-cart-x"></i>
                                        </button>
                                    </c:when>
                                    <c:when test="${empty sessionScope.LOGIN_USER}">
                                        <a href="MainController?action=login"
                                           class="btn btn-outline-primary btn-sm px-3"
                                           title="Đăng nhập để mua hàng">
                                            <i class="bi bi-cart-plus"></i>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="MainController?action=addToCart&bookId=${book.bookId}"
                                           class="btn btn-primary btn-sm px-3 shadow-sm">
                                            <i class="bi bi-cart-plus"></i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty BOOK_LIST}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-inbox display-1 text-muted opacity-25"></i>
                        <p class="text-muted mt-3">
                            Hiện chưa có sách nào. Hao hãy vào Admin để thêm nhé!
                        </p>
                    </div>
                </c:if>
            </div>
        </div>

        <%-- ==================== TIN TỨC ==================== --%>
        <c:if test="${not empty NEWS_LIST}">
            <div class="bg-white py-5">
                <div class="container">
                    <div class="d-flex justify-content-between align-items-end mb-4">
                        <div>
                            <h2 class="section-title">Tin Tức & Sự Kiện</h2>
                            <p class="text-muted mb-0">Cập nhật những thông tin mới nhất</p>
                        </div>
                        <a href="MainController?action=viewNews"
                           class="text-primary fw-bold text-decoration-none">
                            Xem tất cả <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>

                    <div class="row g-4">
                        <c:forEach var="news" items="${NEWS_LIST}">
                            <div class="col-md-4">
                                <div class="card news-card shadow-sm">
                                    <div class="news-thumb">
                                        <i class="bi bi-newspaper display-3
                                                   text-primary opacity-25"></i>
                                    </div>
                                    <div class="card-body p-3">
                                        <div class="text-muted small mb-2">
                                            <i class="bi bi-clock me-1"></i>
                                            <fmt:formatDate value="${news.created_at}"
                                                            pattern="dd/MM/yyyy"/>
                                        </div>
                                        <h6 class="fw-bold text-dark mb-2">
                                            ${news.title}
                                        </h6>
                                        <p class="small text-muted mb-3 news-content-text">
                                            ${news.content}
                                        </p>
                                        <a href="MainController?action=newsDetail&id=${news.news_id}"
                                           class="btn btn-outline-primary btn-sm rounded-pill px-3">
                                            Đọc tiếp <i class="bi bi-arrow-right ms-1"></i>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:if>

        <%-- ==================== BANNER ĐĂNG KÝ ==================== --%>
        <c:if test="${empty sessionScope.LOGIN_USER}">
            <div class="container py-5">
                <div class="card border-0 shadow-sm text-center p-5"
                     style="border-radius:20px;
                            background:linear-gradient(135deg,#0d6efd 0%,#003da5 100%);">
                    <h3 class="fw-bold text-white mb-2">
                        Tham gia cùng Hào's Bookstore!
                    </h3>
                    <p class="text-white opacity-75 mb-4">
                        Đăng ký ngay để nhận ưu đãi độc quyền và theo dõi đơn hàng dễ dàng
                    </p>
                    <div class="d-flex justify-content-center gap-3">
                        <a href="MainController?action=register"
                           class="btn btn-light btn-lg px-5 rounded-pill fw-bold shadow-sm">
                            <i class="bi bi-person-plus me-2"></i>Đăng ký miễn phí
                        </a>
                        <a href="MainController?action=login"
                           class="btn btn-outline-light btn-lg px-5 rounded-pill fw-bold">
                            <i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập
                        </a>
                    </div>
                </div>
            </div>
        </c:if>

        <%@include file="../components/web-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>